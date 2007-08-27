package formatter;

import java.io.IOException;
import java.util.Iterator;


import reader.TrillianReader;
import ui.GUI;
import writer.DeadAIMWriter;

public class Main
{
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			System.out.println("Too few arguments.");
			return;
		}
		
		if (args[0].equalsIgnoreCase("--trillian-deadaim"))
			tril2Daim(args);
		else if (args[0].equalsIgnoreCase("--dead-aim-test"))
			testDeadAim(args);
	}
	
	public static void testDeadAim(String[] args)
	{
		System.err.println("DeadAIM log testing is not yet functional!");
	}
	
	/**
	 * An example of how to read from a Trillian log file using the TrillianReader
	 * and using the generated session date to write to a file in DeadAIM log
	 * format using the DeadAIMwriter class.
	 * 
	 * @param args The command line arguments.  This assumes that <code>args[1]</code>
	 *             is the name of the file to read from and <code>args[2]</code> is
	 *             the directory to write to.  Further, it assumes that the file is
	 *             is in Trillian log format.
	 * 
	 * @throws IllegalArgumentException If there are not exactly 3 arguments.
	 */
	public static void tril2Daim(String[] args)
	{
		if (args.length != 3)
		{
			System.err.println("got args:");
			for (int i = 0; i < args.length; i++)
				System.err.println(args[i]);
			throw new IllegalArgumentException("Argument list is insufficient.  Expected 3 args, got " + args.length);
		}
		
		TrillianReader tr = new TrillianReader();
		
		try
		{
			// Reads the file and loads it into the tr object.
			tr.loadFile(args[1]);
			System.out.println("File loaded.");
		}
		catch (IOException e)
		{
			System.err.println("Unexpected exception: " + e.getClass().getSimpleName());
			e.printStackTrace();
			System.exit(0);
		}
		
		Iterator<Session> iter = tr.iterator();

		// Rhis serves mainly as an example of how to convert from Trillian plain text
		// files to DeadAIM HTML files.

		// The first one is a special call.
		Session session = null;
		if (iter.hasNext())
			session = iter.next();

		Date currDate = null;

		// If the session is null then that means we've used up the last of the sessions.
		// If the last-used date and the date of the current session are the same, that
		// means the last Session and the current one are the same.
		while (session != null && session.getDate() != currDate)
		{
			currDate = session.getDate();
			DeadAIMWriter writer = new DeadAIMWriter(session.getMySN());
			writer.makeFile(args[2], session);
	
			// Each subsiquent session is another call.
			while (iter.hasNext() && (session = iter.next()).getDate().equals(currDate))
			{
				writer.addSession(session);
				session = null;    // Adding this line makes the outer loop work ;-)
			}

			writer.closeFile();
		}
		
		System.out.println("Done.");
	}
	
	/**
	 * Creates and runs a new GUI.
	 */
	public static void runGUI()
	{
		javax.swing.SwingUtilities.invokeLater(new GUI());
	}
}
