package writer;

import formatter.Session;

/**
 * Defines the methods required in a class that writes to a file
 * in a specific logging format.
 * 
 * @author Andrew Correa
 */
public interface Writer
{
	/**
	 * Creates a new file with the given {@link Session}, and stores it
	 * in the given parent directory.
	 * 
	 * @param dir The parent directory of the file to be created.
	 * @param session The {@link Session} object to create a new log from.
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean makeFile(String dir, Session session);
}