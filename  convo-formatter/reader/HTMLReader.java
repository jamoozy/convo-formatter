
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
public abstract class HTMLReader implements Reader
{
	protected Object[] _extractTag(String line)
	{
		return null;
	}

	protected class Tag
	{
	}
}
