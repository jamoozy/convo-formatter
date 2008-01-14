
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
	/**
	 * Keeps track of what state the font is in. It is updated by calling
	 * {@link #extractTag(String)} with {@link String} objects containing
	 * HTML tags at the front. Do not alter this variable directly unless
	 * you really know what you're doing.
	 */
	protected FontState fs;

	/**
	 * Creates a new <code>FontState</code> object.
	 */
	public HTMLReader()
	{
		fs = new FontState();
	}

	/**
	 * Takes the passed {@link String} object and updates the {@link #fs} object
	 * with the state of the font. There must be a valid tag at the front of
	 * the string. Recognized tags currently include:
	 * <ul>
	 * <li> <code>&lt;b&gt;</code> tag - for bold text.
	 * <li> <code>&lt;em&gt;</code> tag - for emphasised text.
	 * <li> <code>&lt;i&gt;</code> tag - for italicized text.
	 * <li> <code>&lt;u&gt;</code> tag - for underlined text.
	 * <li> <code>&lt;font&gt;</code> tag - for font size, color, and face.
	 * </ul>
	 * The resulting changes are recorded with the {@link #fs} object.
	 *
	 * @param line The {@link String} with the tag at the front of it.
	 * @return The rest of the {@link String} minus the tag.
	 *
	 * @throws IllegalArgumentException if <code>line</code> does not have
	 * an HTML tag at the front of it.
	 */
	protected String extractTag(String line) throws IllegalArgumentException
	{
		// For ease of use.
		char[] str = line.trim().toCharArray();

		if (str[0] != '<')
			throw new IllegalArgumentException("Line does not begin with tag.");

		// Determine if this is a closing tag.
		int i = 1;
		boolean close = false;
		if (str[1] == '/')
		{
			close = true;
			i++;
		}

		switch (str[i])
		{
			case 'b':
			case 'B':
				return line.substring(_bTag(line.trim(), close)).trim();

			case 'e':
			case 'E':
			case 'i':
			case 'I':
				return line.substring(_iTag(line.trim(), close)).trim();

			case 'u':
			case 'U':
				return line.substring(_uTag(line.trim(), close)).trim();

			case 'f':
			case 'F':
				return line.substring(_fontTag(line.trim(), close)).trim();
		}

		throw new IllegalArgumentException("Unrecognized tag:"+line);
	}



	////////////////////////////////////////////////////////////////////////////////
	// ------------------------------- Tag Eating ------------------------------- //
	////////////////////////////////////////////////////////////////////////////////

	/**
	 * Parse a <code>&lt;b%gt;</code> or <code>&lt;/b%gt;</code> tag.
	 *
	 * @param line <code>String</code> of the line to get the tag from.
	 * @param close <code>true</code> if this tag is an end-tag.
	 * @return The index of the value after the tag closes.
	 *
	 * @throws IllegalArgumentException if the <code>String</code> does not
	 * have a valid tag at the front.
	 */
	private int _bTag(String line, boolean close) throws IllegalArgumentException
	{
		if (close)
		{
			String tag = line.substring(0,4).toLowerCase();
			if (tag.equals("</b>"))
			{
				fs.setNotBold();
				return 4;
			}
			else
			{
				throw new IllegalArgumentException("Invalid </b> tag:"+line);
			}
		}
		else
		{
			String tag = line.substring(0,3).toLowerCase();
			if (tag.equals("<b>"))
			{
				fs.setBold();
				return 3;
			}
			else
			{
				throw new IllegalArgumentException("Invalid <b> tag:"+line);
			}
		}
	}

	/**
	 * Parse a <code>&lt;i%gt;</code> or <code>&lt;/i%gt;</code> or
	 * <code>&lt;em%gt;</code> or <code>&lt;/em%gt;</code> tag.
	 *
	 * @param line <code>String</code> of the line to get the tag from.
	 * @param close <code>true</code> if this tag is an end-tag.
	 * @return The index of the value after the tag closes.
	 *
	 * @throws IllegalArgumentException if the <code>String</code> does not
	 * have a valid tag at the front.
	 */
	private int _iTag(String line, boolean close)
	{
		if (close)
		{
			String iTag = line.substring(0,4).toLowerCase();
			String emTag = line.substring(0,5).toLowerCase();
			if (iTag.equals("</i>"))
			{
				fs.setNotItalicized();
				return 4;
			}
			else if (emTag.equals("</em>"))
			{
				fs.setNotItalicized();
				return 5;
			}
			else
			{
				throw new IllegalArgumentException("Invalid </i> or </em> tag:"+line);
			}
		}
		else
		{
			String iTag = line.substring(0,3).toLowerCase();
			String emTag = line.substring(0,4).toLowerCase();
			if (iTag.equals("<i>"))
			{
				fs.setItalicized();
				return 3;
			}
			else if (emTag.equals("<em>"))
			{
				fs.setItalicized();
				return 4;
			}
			else
			{
				throw new IllegalArgumentException("Invalid <i> or <em> tag:"+line);
			}
		}
	}

	/**
	 * Parse a <code>&lt;u%gt;</code> or <code>&lt;/u%gt;</code> tag.
	 *
	 * @param line <code>String</code> of the line to get the tag from.
	 * @param close <code>true</code> if this tag is an end-tag.
	 * @return The index of the value after the tag closes.
	 *
	 * @throws IllegalArgumentException if the <code>String</code> does not
	 * have a valid tag at the front.
	 */
	private int _uTag(String line, boolean close)
	{
		if (close)
		{
			String tag = line.substring(0,4).toLowerCase();
			if (tag.equals("</u>"))
			{
				fs.setNotUnderlined();
				return 4;
			}
			else
			{
				throw new IllegalArgumentException("Invalid </u> tag:"+line);
			}
		}
		else
		{
			String tag = line.substring(0,3).toLowerCase();
			if (tag.equals("<u>"))
			{
				fs.setUnderlined();
				return 3;
			}
			else
			{
				throw new IllegalArgumentException("Invalid <u> tag:"+line);
			}
		}
	}

	/**
	 * Parse a <code>&lt;font%gt;</code> or <code>&lt;/font%gt;</code> tag.
	 *
	 * @param line <code>String</code> of the line to get the tag from.
	 * @param close <code>true</code> if this tag is an end-tag.
	 * @return The index of the value after the tag closes.
	 *
	 * @throws IllegalArgumentException if the <code>String</code> does not
	 * have a valid tag at the front.
	 */
	private int _fontTag(String line, boolean close)
	{
		if (close)
		{
		}
		else
		{
		}
		
		return 0;
	}
}
