package reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.Iterator;
import java.util.Vector;

import formatter.Date;
import formatter.FontState;
import formatter.Session;

// TODO: actually write this.
public class DeadAIMReader extends HTMLReader
{
	private Vector<Session> sessions;  // The Sessions stored in this log file.
	private FontState fs;              // keeps track of how the HTML tags have effected text.

	/**
	 * Loads the {@link Session}s of this file into this class.
	 * 
	 * @param filename The name of the file to laod.
	 * @return <code>true</code> if the file was loaded correctly, <code>false</code> otherwise.
	 */
	public boolean loadFile(String filename) throws IOException
	{
		BufferedReader reader;

		try
		{
			reader = new BufferedReader(new FileReader(filename));
		}
		catch (FileNotFoundException fnfe)
		{
			throw new FileNotFoundException("File " + filename + " does not exist!");
		}

		sessions = new Vector<Session>();

		// Determine the date from the name of the file.
		int start1 = filename.lastIndexOf('/');
		int start2 = filename.lastIndexOf('\\');
		int start = (start1 > start2 ? start1+1 : start2+1);
		Date date = new Date(Integer.parseInt(filename.substring(start+0,start+4)),
							Integer.parseInt(filename.substring(start+5,start+7)),
							Integer.parseInt(filename.substring(start+8,start+10)),
							filename.substring(start+12,filename.indexOf(']', start+13)));
		System.out.println("Determined Date:"+date.toString());

		// Eat up some of the header stuff.
		int lineNumber = 1;
		String line = reader.readLine();
		line = eat(line, "<html>", "No <html> tag found.");
		while (line.trim().equals("")) { line = reader.readLine(); lineNumber++; }
		line = eat(line, "<head>", "No <head> tag found.");
		while (line.trim().equals("")) { line = reader.readLine(); lineNumber++; }
		line = eat(line, "<title>", "No <head> tag found.");
		while (line.trim().equals("")) { line = reader.readLine(); lineNumber++; }
		
		// Find out who I'm speaking with -- it's the 3rd word in the sequence:
		// Talk with [sn] on [date]
		line = eat(line, "Talk", "\"Talk\" expected.");
		while (line.trim().equals("")) { line = reader.readLine(); lineNumber++; }
		line = eat(line, "with", "\"with\" expected.");
		while (line.trim().equals("")) { line = reader.readLine(); lineNumber++; }
		String yourSN = line.trim().substring(0, line.trim().indexOf(' '));
		System.out.println("Determined yourSN:"+yourSN);
		line = eat(line, yourSN, "Could not read SN correctly on line "+lineNumber);
		while (line.trim().equals("")) { line = reader.readLine(); lineNumber++; }
		line = eat(line, "on", "\"on\" expectd.");
		while (line.trim().equals("")) { line = reader.readLine(); lineNumber++; }
		line = eat(line, date.toString(), "Could not read date correctly:"+lineNumber);
		while (line.trim().equals("")) { line = reader.readLine(); lineNumber++; }
		
		// Get the background color (we ignore it for now)
		// FIXME: The bg color is being ignored ... this cannot stand!
		eatBodyTag(line);

		//Session.makeSession(mySN, yourSN, date);

//		for (int i = 1; next != null; i++)
//		{
//			_parseLine(i, next);
//			next = reader.readLine();
//		}

		return false;
	}

	/**
	 * Parse a single line of text.
	 * @param lineNumber
	 * @param text
	 */
	private void _parseLine(int lineNumber, String text)
	{
		
	}

	/**
	 * Returns an <code>Iterator</code> of the <code>Session</code>s
	 * in the last-read file.
	 * 
	 * @return The <code>Iterator</code>.
	 */
	public Iterator<Session> iterator()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
