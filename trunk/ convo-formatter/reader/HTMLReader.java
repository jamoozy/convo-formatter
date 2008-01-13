
package reader;

import java.io.IOException;
import java.util.Iterator;

import formatter.FontState;
import formatter.Session;

/**
 * Defines the required methods for a class that Reads from a log file
 * formatted in a certain way.
 * 
 * @author Andrew Correa
 */
public abstract class HTMLReader implements Reader
{
	/** Keeps track of what state the font is in. */
	protected FontState fs;

	/**
	 * Creates a new <code>FontState</code> object.
	 */
	public HTMLReader()
	{
		fs = new FontState();
	}

	protected Object[] _extractTag(String line)
	{
		return null;
	}

	/**
	 * A single tag is represented with this enum.
	 */
	protected enum Tag
	{
	}
}
