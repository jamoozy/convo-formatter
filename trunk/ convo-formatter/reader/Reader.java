package reader;

import java.io.IOException;
import java.util.Iterator;

import formatter.Session;

/**
 * Defines the required methods for a class that Reads from a log file
 * formatted in a certain way.
 * 
 * @author Andrew Correa
 */
public interface Reader
{
	/**
	 * Loads a file into this class.  Once loaded you can get an {@link Iterator}
	 * of (@link Session}s with the {@link #iterator()} method.
	 * 
	 * @param filename The file to load from.
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 * 
	 * @throws IOException When an I/O error occurs.
	 * 
	 * @see {@link #iterator()}
	 */
	public boolean loadFile(String filename) throws IOException;
	
	/**
	 * Gets the {@link Iterator} that iterates through the stored {@link Session}s.
	 * 
	 * @return An {@link Iterator} of {@link Session}s that were stored in the file.
	 */
	public Iterator<Session> iterator();
}