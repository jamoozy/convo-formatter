package reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import exception.FileFormatException;
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
public class TrillianReader implements Reader
{
	private Vector<Session> sessions;  // The sessions in the last-read file.

	/**
	 * Creates a new TrillianReader().  Nothing special here.
	 */
	public TrillianReader()	{}
	
	/**
	 * Loads the file with the given URL.
	 * 
	 * @param name The name of the file to open.
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 * 
	 * @throws IOException if any other IO error occurs during reading.
	 * @throws FileNotFoundException if the file can't be found.
	 * @throws FileFormatException if the file's format is bad.
	 * 
	 * @see Reader#loadFile(String)
	 */
	public boolean loadFile(String name) throws IOException
	{
		BufferedReader reader;
		
		try
		{
			reader = new BufferedReader(new FileReader(name));
		}
		catch (FileNotFoundException fnfe)
		{
			throw new FileNotFoundException("File " + name + " does not exist!");
		}
		
		sessions = new Vector<Session>();
		String next = reader.readLine();
		
		for (int i = 1; next != null; i++)
		{
			parseLine(i, next);
			next = reader.readLine();
		}
		
		return true;
	}
	
	/**
	 * Parse this line.
	 *
	 * @param lineNumber line number we're on -- used to report which line an error ocurred on.
	 * @param line The text of the line to parse.
	 *
	 * @throws FileFormatException When the file is not formattec correctly.
	 */
	private void parseLine(int lineNumber, String line) throws FileFormatException
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
					int year = Integer.parseInt(line.substring(after+23, after+27));
					int month = Date.monthToInt(line.substring(after+7, after+10));
					int date = Integer.parseInt(line.substring(after+11, after+13));
					String day = Date.dayToFullName(line.substring(after+3, after+6));
					
					// Make sure a different Session isn't already active.  This can happen when
					// the user was unexpectedly disconnected.
					if (Session.isActiveSession())
					{
						Session.getSession().add(new Notification(Notification.Type.END_SESSION, null));
						Session.closeSession();
					}
						
					// Create the new Session.
					sessions.add(Session.makeSession(mySN, yourSN, new Date(year, month, date, day)));

					// Build the Timestamp for when this was created.
					int hour = Integer.parseInt(line.substring(after+14, after+16));
					int minute = Integer.parseInt(line.substring(after+17, after+19));
					int second = Integer.parseInt(line.substring(after+20, after+22));
					
					// Add the "we're started now" Notification.
					Session.getSession().add(new Notification(Notification.Type.START_SESSION, new Timestamp(hour, minute, second)));

				}
				else if (line.substring(8,13).equals("Close"))
				{
					// Fine where the SN stops (hence the length).
					int after = line.indexOf(')');
					
					// Build the Timestamp.
					int hour = Integer.parseInt(line.substring(after+14, after+16));
					int minute = Integer.parseInt(line.substring(after+17, after+19));
					int second = Integer.parseInt(line.substring(after+20, after+22));

					// Check that there is a Session active.
					if (!Session.isActiveSession()) return;
					
					// Add the end-session Notification.
					Session.getSession().add(new Notification(Notification.Type.END_SESSION, new Timestamp(hour, minute, second)));

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
						Session.getSession().add(new Notification(Notification.Type.BUDDY_LOG_ON, ts));
					else if (line.contains(" signed off at "))
						Session.getSession().add(new Notification(Notification.Type.BUDDY_LOG_OFF, ts));
					else if (line.contains("You have been disconnected."))
						Session.getSession().add(new Notification(Notification.Type.DISCONNECT, ts));
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
	 * Returns an <code>{@link Iterator}&lt;{@link Session}&gt;</code> of the
	 * messages in the file last read by this <code>TrillanReader</code>
	 * @return The messages in the last-read file.
	 */
	public Iterator<Session> iterator()
	{
		return sessions.iterator();
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
