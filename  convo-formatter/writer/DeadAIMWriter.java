package writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import java.awt.Color;
import java.awt.Font;

//import javax.swing.JOptionPane;

import exception.ExistingFileException;
import exception.NoFileException;
import formatter.Date;
import formatter.Event;
import formatter.Message;
import formatter.Notification;
import formatter.Session;
import formatter.Timestamp;

/**
 * Writes files in the DeadAIM format.  The file's name is determined by the date.  This format is in HTML.
 * 
 * @author Andrew Correa
 */
public class DeadAIMWriter implements Writer
{
	////////////////////////////////////////////////////////////////////////////
	// ------------------------------ Static -------------------------------- //
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Computes what the filename of log generated at the given date would be.
	 * 
	 * @param session The <code>Session</code> associated with this file.
	 * @return The name of the file.
	 */
	public static String fileName(Session session)
	{
		Date date = session.getDate();
		return String.format("%d-%02d-%02d [%s].htm", date.getYear(), date.getMonth(), date.getDate(), date.getDay());
	}
	
	

	////////////////////////////////////////////////////////////////////////////
	// ------------------------------ Object -------------------------------- //
	////////////////////////////////////////////////////////////////////////////
	
	private FileWriter writer;  // Thing we write to.
	private Date date;          // The date of this file.
	private String me;          // The name of the screen name to be red.

	/**
	 * Initializes a new <code>DeadAIMWriter</code> object.
	 * 
	 * @param name Name of "my screen name" in this conversation.
	 */
	public DeadAIMWriter(String name)
	{
		writer = null;
		me = name;
	}
	
	/**
	 * <p>Creates a new log file with the given <code>Session</code>.  This function
	 * creates a new file with the name appropriate to the {@link Date} the
	 * <code>Session</code> took place.  It then adds the <code>Session</code>
	 * to the created file.</p>
	 * <p>If an I/O error occurs while adding the file, this method prints out an
	 * error message and returns without finishing the operation.</p>
	 * 
	 * @param dir Name of the containing directory.
	 * @param session Data to store in the file.
	 */
	public void makeFile(String dir, Session session)
	{
		if (writer != null)
			throw new ExistingFileException("Already writing file: " + writer);
		
		// Store the date for comparison with other sessions.
		date = session.getDate();
		String name = String.format("%s%d-%02d-%02d [%s].htm",
									dir,
									date.getYear(),
									date.getMonth(),
									date.getDate(),
									date.getDay());

		File file = new File(name);
		System.out.println("Writing to file:" + file.getAbsolutePath());

		// If the file alredy exists, prompt for overwrite.
//		if (file.exists())
//		{
//			System.out.println("file:" + file.getName());
//			if (JOptionPane.showConfirmDialog(null, "File exists! Overwrite?", "File Exists", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
//			{
//				System.out.println("aborting...");
//				return false;
//			}
//		}
		// Nevermind ... just append regardless.
		
		// Create a new file writer.
		try
		{
			if (!file.exists())
			{
				file.createNewFile();
				writer = new FileWriter(file);
			}
			else // append
			{
				writer = new FileWriter(file, true);
				writer.write("\n\n<!-- New conversation -->\n\n");
			}
			writer.write("<html>\n<head>\n<title>\nTalk with " + session.getYourSN() + " on " + session.getDate().toString() + "\n</title>\n</head>\n\n<body bgcolor=\"#ffffff\">\n\n");
		}
		catch (IOException e) 
		{
			System.err.println("Could not open file.  Perhaps it is being used by another program?");
			e.printStackTrace();
			return;
		}
		
		addSession(session);
	}
	
	/**
	 * Adds a session to the file accessed via the filewriter.
	 * 
	 * @param session The Session to write to the file.
	 * 
	 * @throws IllegalArgumentException if the date passed in represents
	 *         a time different the date already associated with the
	 *         <code>Session</code>.
	 */
	public void addSession(Session session)
	{
		if (!session.getDate().equals(date))
			throw new IllegalArgumentException("Passed date preceeds current date.");

		Iterator<Event> events = session.iterator();
		
		while (events.hasNext())
			addEvent(events.next());
	}

	/**
	 * Adds a message to the html file with the appropriate tags.
	 * 
	 * @param message The message to put next in the file.
	 */
	private void addEvent(Event event)
	{
		if (writer == null)
			throw new NoFileException("No file is currently open.");

		if (event instanceof Message)
		{
			Message message = ((Message)event);
			String msg = message.getMessage();

			// Convert from newlines to the propper html break tag.
			msg.replace("\n", "<br />");

			try
			{
				// Write the message.
				writer.write(String.format("%s<b><font color=\"#%s\">%s <font size=1>(%d:%02d:%02d %s)</font>:</font></b> <font color =\"#%s%s%s\" face=\"%s\">%s</font>%s\n",
						(message.isAutoGenerated())? "<hr />" : "",
						(message.getSender().equals(me))? "ff0000" : "0000ff",
						((message.isAutoGenerated())? "Auto response from " : "") + message.getSender(),
						message.getTimestamp().getHour12(),
						message.getTimestamp().getMinute(),
						message.getTimestamp().getSecond(),
						message.getTimestamp().getAMPM(),
						asByte(message.getColor().getRed()),
						asByte(message.getColor().getGreen()),
						asByte(message.getColor().getBlue()),
						message.getFont().getFontName(),
						msg.replace("\n", "<br /><br />"),
						(message.isAutoGenerated())? "<hr />" : "<br />"));
			}
			catch (IOException e)
			{
				System.err.println("Unknown error ocurred while writing to file.");
				e.printStackTrace();
			}
		}
		else if (event instanceof Notification)
		{
			Notification n = ((Notification)event);

			if (n.getType() == Notification.Type.END_SESSION) try
			{
				Timestamp ts = n.getTimestamp();
				if (ts != null)
				{
					writer.write(String.format("<hr><b>Session concluded at %d:%02d:%02d %s</b><hr>\n", 
						                        ts.getHour12(),
						                        ts.getMinute(),
						                        ts.getSecond(),
						                        ts.getAMPM()));
				}
				else
				{
					writer.write("<hr><b>Session concluded at ??:??:?? ??</b><hr>\n");
				}
			}
			catch (IOException e)
			{
				System.err.println("Unknown error ocurred while writing to file.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns a byte value for this integer as a <code>{@link String}</code>.
	 * <p>
	 * So:<br />
	 * <ul>
	 *    <li>0   -> "00"</li>
	 *    <li>255 -> "ff"</li>
	 *    <li>etc...</li>
	 * </ul>
	 * 
	 * @param x Number to convert to a <code>String</code>.
	 * @return A two-digit hex value in a <code>String</code>.
	 */
	private String asByte(int x)
	{
		return Integer.toHexString(x & 0xff);
	}

	/**
	 * Finishes up writing the file, adds ending tags, and closes the stream.
	 * 
	 * @return <code>true</code> if the file was successfully closed.  <code>false</code> otherwise.
	 */
	public boolean closeFile()
	{
		if (writer != null) try
		{
			writer.write("\n</body></html>\n");
			writer.flush();
			writer.close();
			writer = null;
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		} else return true;
	}

	/**
	 * Used only to test this class.
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args)
	{
		DeadAIMWriter writer = new DeadAIMWriter("jamoozy");
		
		Timestamp ts1 = new Timestamp(20,31,40);
		Timestamp ts2 = new Timestamp(20,32,32);
		Timestamp ts3 = new Timestamp(20,32,43);
		
		// Create my session with Julian.
		Session s1 = Session.makeSession("jamoozy", "AIMSprudeldudel", new Date(2005, 9, 21, "[day]"));
		s1.add(new Message("jamoozy", ts1, "Back in Deutschland?", new Color(4*16, 0, 8*16), new Font("Arial", Font.PLAIN, 12), false));
		s1.add(new Message("AIMSprudeldudel", ts2, "denver", new Color(0, 0, 0), new Font("Times", Font.PLAIN, 12), false));
		s1.add(new Message("jamoozy", ts3, "What are you doing there?", new Color(4*16, 0, 8*16), new Font("Arial", Font.PLAIN, 12), false));
		Session.closeSession();
		
		// Write a test file.
		System.out.println("Test 1...");
		try {
			writer.makeFile("C:\\temp", s1);
		} catch (Exception e) {
			System.err.println("Error during Test 1: " + e.getMessage());
		}

		// Meant to test exception handling -- no longer used.
//		System.out.println("Test 2...");
//		try {
//			if (writer.makeFile("C:\\temp", new Date(2005, 9, 22)))
//			{
//				try {
//					writer.newFile("C:\\temp", new Date(2005, 9, 23));
//					System.err.println("Did not throw exception on 2nd call!");
//				} catch (ExistingFileException e) {}
//				
//				writer.closeFile();
//			}
//		} catch (ExistingFileException e) {
//			System.err.println("Threw exception on first call!");
//		}
//			
//		System.out.println("Test 3...");
//		try {
//			writer.addEvent(new Message("AIMSprudeldudel", ts2, "denver", new Color(0, 0, 0), new Font("Times", Font.PLAIN, 12), false));
//			System.err.println("Did not throw exception");
//		} catch (NoFileException e) {}

		System.out.println("Done.");
	}
}
