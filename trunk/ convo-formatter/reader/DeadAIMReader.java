package reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.Iterator;
import java.util.Vector;

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
		String next = reader.readLine();

		for (int i = 1; next != null; i++)
		{
			_parseLine(i, next);
			next = reader.readLine();
		}

		return false;
	}

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
