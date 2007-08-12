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
		TrillianReader tr = new TrillianReader();
		
		try
		{
			// Reads the file and loads it into the tr object.
			tr.loadFile("./Trillian example.txt");
			System.out.println("File loaded.");
		}
		catch (IOException e)
		{
			System.err.println("Unexpected exception: " + e.getClass().getSimpleName());
			e.printStackTrace();
			System.exit(0);
		}
		
		Iterator<Session> iter = tr.iterator();

		// TODO: Enclose this in a further loop to handle the case where
		// Trillian wants to have one file but AIM wants to have multiple files.
		
		// The first one is a special call.
		Session session = iter.next();
		DeadAIMWriter writer = new DeadAIMWriter(session.getMySN());
		writer.makeFile("./", session);

		// Each subsiquent session is another call.
		while (iter.hasNext())
			writer.addSession(iter.next());

		writer.closeFile();
		
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
