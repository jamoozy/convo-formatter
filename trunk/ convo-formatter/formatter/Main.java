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
			tr.loadFile("./Trillian example.txt");
		}
		catch (IOException e)
		{
			System.err.println("Unexpected exception: " + e.getClass().getSimpleName());
			e.printStackTrace();
		}
		
		Iterator<Session> iter = tr.iterator();
		
		while (iter.hasNext())
		{
			Session session = iter.next();
			DeadAIMWriter writer = new DeadAIMWriter(session.getMySN());
			
			writer.newFile("./", session.getDate());
			
			Iterator<Event> events = session.iterator();
			
			while (events.hasNext())
				writer.addEvent(events.next());
			
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
