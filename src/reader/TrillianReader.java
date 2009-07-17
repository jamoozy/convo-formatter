package reader;

import java.io.IOException;

import formatter.Date;
import formatter.Notification;
import formatter.Message;
import formatter.Session;
import formatter.Timestamp;

/**
 * The <code>TrillianReader</code> reads a Trillian log file and converts its
 * contents into multiple {@link Session}s.
 *
 * @author Andrew Correa
 */
public class TrillianReader extends AbstractReader
{
  /**
   * Creates a new TrillianReader().  Nothing special here.
   */
  public TrillianReader()  {}

  /**
   * Parse this line.
   *
   * @param lineNumber line number we're on -- used to report which line an error ocurred on.
   * @param line The text of the line to parse.
   *
   * @throws FileFormatException When the file is not formattec correctly.
   */
  protected void _parseLine(int lineNumber, String line) throws FileFormatException
  {
    line = line.trim();

    if (line.equals("")) return;

    switch (line.charAt(0))
    {
      case ' ':
      case '\n':
      case '\r':
        // Ignore blank space.
        break;

      // Session start and end.
      case 'S':
        if (line.substring(8,13).equals("Start"))
        {
          // Find the places where the two SNs are written.
          int before = line.indexOf('(');
          int between = line.indexOf(':');
          int after = line.indexOf(')');

          // Store the two SNs
          String mySN = line.substring(before+1, between);
          String yourSN = line.substring(between+1, after);

          // Find the year, month, date (day of the month), and day (day of the week).
          Date date = _readDate(line, after+3);

          // Make sure a different Session isn't already active.  This can happen when
          // the user was unexpectedly disconnected.
          if (Session.sessionIsActive())
          {
            Session.getSession().add(new Notification(Notification.Type.END_SESSION, date, null));
            Session.closeSession();
          }

          // Create the new Session.
          sessions.add(Session.makeSession(mySN, yourSN, date));

          // Build the Timestamp for when this was created.
          int hour = Integer.parseInt(line.substring(after+14, after+16));
          int minute = Integer.parseInt(line.substring(after+17, after+19));
          int second = Integer.parseInt(line.substring(after+20, after+22));

          // Add the "we're started now" Notification.
          Session.getSession().add(new Notification(Notification.Type.START_SESSION, date, new Timestamp(hour, minute, second)));

        }
        else if (line.substring(8,13).equals("Close"))
        {
          // Fine where the SN stops (hence the length).
          int after = line.indexOf(')');

          // Build the Date.
          Date date = _readDate(line, after+3);

          // Build the Timestamp.
          Timestamp ts = new Timestamp(
                   Integer.parseInt(line.substring(after+14, after+16)),
                   Integer.parseInt(line.substring(after+17, after+19)),
                   Integer.parseInt(line.substring(after+20, after+22)));

          // Check that there is a Session active.
          if (!Session.sessionIsActive()) return;

          // Add the end-session Notification.
          try
          {
            Session.getSession().add(new Notification(Notification.Type.END_SESSION, date, ts));
            Session.getSession().setDate(date);
          }
          catch (IllegalArgumentException e)
          {
            throw new FileFormatException("Error on line " + lineNumber + ", date is too early.");
          }

          // Close the Session.
          Session.closeSession();
        }
        return;

      case '[':
        // This is a message.  Here, the first thing in the line is the time it happened.
        Timestamp ts = new Timestamp(
            Integer.parseInt(line.substring(1,3)),   // hour
            Integer.parseInt(line.substring(4,6)),   // minute
            Integer.parseInt(line.substring(7,9)));  // second

        // Determine if this is an actual message, or if it is not.
        if (line.charAt(11) == '*')
        {
          if (line.contains(" signed on at "))
          {
            int start = line.lastIndexOf('"') + 15;
            Date date = _readDate(line, start);
            Session.getSession().add(new Notification(Notification.Type.BUDDY_LOG_ON, date, ts));
          }
          else if (line.contains(" signed off at "))
          {
            int start = line.lastIndexOf('"') + 16;
            Date date = _readDate(line, start);
            Session.getSession().add(new Notification(Notification.Type.BUDDY_LOG_OFF, date, ts));
          }
          else if (line.contains("You have been disconnected."))
          {
            Date date = _readDate(line, 43);
            Session.getSession().add(new Notification(Notification.Type.DISCONNECT, date, ts));
          }
          else if (line.contains("Auto"))
          {
            Session current = Session.getSession();
            if (line.substring(34,36).equals("to"))         // Auto-message from me.
            {
              int startSN = line.indexOf(':', 37);
              String message = line.substring(startSN+2);
              current.add(new Message(current.getMySN(), ts, message, true));
            }
            else if (line.substring(39,43).equals("from"))  // Auto-message from you.
            {
              int startSN = line.indexOf(':', 37);
              String message = line.substring(startSN+2);
              current.add(new Message(current.getYourSN(), ts, message, true));
            }
          }
        }
        else // Message from a user.
        {
          int endSN = line.indexOf(':', 11);     // End of this SN
          String sn = line.substring(11,endSN);  // SN of this guy.
          String msg = line.substring(endSN+2);  // The message.

          Session.getSession().add(new Message(sn, ts, msg, false));
        }
        return;

      default:
        // In this case, the line did not show any particular signs of being anything, really.
        // Therefore, it's either a continuation of the previous Message or this file is
        // mal-formatted.  If the last Event added to the current Session is not a Message,
        // then an IllegalArgumentException will be thrown.
        try
        {
          Session.getSession().append(line);
        }
        catch (IllegalArgumentException iae)
        {
          throw new FileFormatException("Error on line " + lineNumber + ", format is off.");
        }
    }
  }

  /**
   * Reads in the date given a line and starting point of the Date to be read.
   *
   * @param line The line on which the date info lies.
   * @param start The start on the line where the date info starts.
   *
   * @return The <code>Date</code> on the given line at the given point.
   */
  private Date _readDate(String line, int start)
  {
    int year = Integer.parseInt(line.substring(start+20, start+24));
    int month = Date.monthToInt(line.substring(start+4, start+7));
    int date = Integer.parseInt(line.substring(start+8, start+10));
    String day = Date.dayToFullName(line.substring(start, start+3));

    return new Date(year, month, date, day);
  }

  public static void main(String[] args)
  {
    TrillianReader tr = new TrillianReader();

    try
    {
      tr.loadFile("./Trillian example.txt");
    }
    catch (IOException e)
    {
      System.err.println("Unexpected exception: " + e.getClass().getSimpleName());
      e.printStackTrace();
    }

    System.out.println("Done.");
  }
}
