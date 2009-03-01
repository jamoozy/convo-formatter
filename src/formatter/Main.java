package formatter;

import java.io.IOException;
import java.util.Iterator;


import reader.DeadAIMReader;
import reader.TrillianReader;
import ui.GUI;
import writer.DeadAIMWriter;


/**
 * Serves as a tester class to be executed on the CLI. Currently supports the
 * following flags:
 * <ul>
 * <li> <code>--trillian-deadaim</code> to test the conversion from Trillian
 * text format to DeadAIM HTML format. This requires two additional arguments:
 * The relative name of the Trillian input file (e.g.
 * <code>in/TrillEx.txt</code>) and the output directory (e.g.
 * <code>out/</code>)</li>
 * <li> <code>--read-deadaim</code> to test the DeadAIM HTML read capability.
 * This requires one additional argument: the name of the input file. </li>
 * </ul>
 */
public class Main
{
	/**
	 * Evaluate the argument(s) and proceed accordingly.
	 */
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			System.out.println("Too few arguments.");
			return;
		}

		if (args[0].equalsIgnoreCase("--trillian-deadaim"))
			tril2Daim(args);
		else if (args[0].equalsIgnoreCase("--read-deadaim"))
			testDeadAim(args);
		else
			System.out.println("Unrecognized command");
	}

	/**
	 * An example of how to read a DeadAIM file.
	 */
	public static void testDeadAim(String[] args)
	{
		if (args.length != 2)
		{
			System.err.println("DeadAIM HTML reader test usage:");
			System.err.println("\t1st arg: " + args[0]);
			System.err.println("\t2nd arg: [DeadAIM file]");
			return;
		}

		DeadAIMReader reader = new DeadAIMReader();
		try
		{
			reader.loadFile(args[1]);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * An example of how to read from a Trillian log file using the TrillianReader
	 * and using the generated session date to write to a file in DeadAIM log
	 * format using the DeadAIMwriter class.
	 * 
	 * @param args The command line arguments.  This assumes that <code>args[1]</code>
	 *             is the name of the file to read from and <code>args[2]</code> is
	 *             the directory to write to.  Further, it assumes that
	 *             <code>args[1]</code> is in Trillian log format.
	 * 
	 * @throws IllegalArgumentException If there are not exactly 3 arguments.
	 */
	public static void tril2Daim(String[] args)
	{
		if (args.length != 3)
		{
			System.err.println("Trillian to DeadAIM conversion test usage:");
			System.err.println("\t1st arg: " + args[0]);
			System.err.println("\t2nd arg: [Trillian file]");
			System.err.println("\t3rd arg: [output directory]");
			return;
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
			return;
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
