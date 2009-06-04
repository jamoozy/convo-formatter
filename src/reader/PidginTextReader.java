package reader;

import java.io.IOException;

import java.util.Iterator;
import java.util.Vector;

import formatter.Date;
import formatter.Event;
import formatter.Message;
import formatter.Session;
import formatter.Timestamp;

public class PidginTextReader extends AbstractReader
{
  public PidginTextReader() {}

  protected void _parseLine(int lineNumber, String line) throws FileFormatException
  {
    if (lineNumber == 1)
    {
      // Should read "Conversation with xXx at Day ?? Mon YYYY 6-digit time with
      // ':'s separating hours from minutes from seconds, 
      String[] parts = line.split(" ");
      Session.makeSession(parts[12], parts[2],
          Date.makeDateMDY(parts[5], parts[6], parts[7]));
      sessions.add(Session.getSession());
    }
    else
    {
      Session session = sessions.lastElement();

      // Get the time at the front (correctly).
      line = line.trim();
      int cpIndex    = line.indexOf(")");
      int colonIndex = line.indexOf(":");

      // This is a '(' in the body of the message.
      if (cpIndex > colonIndex) cpIndex = -1;

      // If one of these are true, then we found the SN between the ')' (or the
      // beginning of the line) and the ':'.
      if ((colonIndex == session.getYourSN().length() &&
           session.getYourSN().equals(line.substring(
               cpIndex == -1 ? 0 : cpIndex + 1, colonIndex).trim()))
       || (colonIndex == session.getMySN().length() &&
           session.getMySN().equals(line.substring(
               cpIndex == -1 ? 0 : cpIndex + 1, colonIndex).trim())))
      {
        System.out.println("Adding message.");
        session.add(new Message(line.substring(
                  cpIndex + 1, colonIndex).trim(),
              cpIndex == -1 ? null :
                  Timestamp.parseTimestamp(line.substring(0, cpIndex)),
              line.substring(colonIndex + 1).trim(), false));
      }
      else
      {
        System.out.println("Appending to message.");
        session.append(line);
      }
    }
  }

  /**
   * Test this shit out, yo.
   */
  public static void main(String[] args) throws IOException
  {
    if (args.length == 0)
      System.out.println("You didn't pass me any arguments, so I can't start.");

    for (int i = 0; i < args.length; i++)
    {
      System.out.println("Reading file \"" + args[i] + "\"");

      PidginTextReader preader = new PidginTextReader();
      preader.loadFile(args[i]);

      Iterator<Session> siter = preader.iterator();
      for (int s = 1; siter.hasNext(); s++)
      {
        Session session = siter.next();
        System.out.println(" Session " + s + ", " + session + ":");

        Iterator<Event> eiter = session.iterator();
        while (eiter.hasNext())
          System.out.println("  " + eiter.next());
      }
    }
  }
}
