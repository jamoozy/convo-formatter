package reader;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import formatter.Session;

// TODO: actually write this.
public class DeadAIMReader implements Reader
{
	private Vector<Session> sessions;  // The Sessions stored in this log file.

	/**
	 * Loads the {@link Session}s of this file into this class.
	 * 
	 * @param filename The name of the file to laod.
	 * @return <code>true</code> if the file was loaded correctly, <code>false</code> otherwise.
	 */
	public boolean loadFile(String filename) throws IOException
	{
		// TODO Auto-generated method stub
		return false;
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
