
package reader;

import java.awt.Color;
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
	 * you really know what you are doing and you have a good reason.
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
	 * <li> <code>&lt;b&gt;</code> for bold text.
	 * <li> <code>&lt;em&gt;</code> for emphasised text.
	 * <li> <code>&lt;i&gt;</code> for italicized text.
	 * <li> <code>&lt;u&gt;</code> for underlined text.
	 * <li> <code>&lt;font&gt;</code> for font size, color, and face.
	 * </ul>
	 * The resulting changes are recorded with the {@link #fs} object.
	 *
	 * @param line The {@link String} with the tag at the front of it.
	 * @return The rest of the {@link String} minus the tag.
	 *
	 * @throws IllegalArgumentException if <code>line</code> does not have
	 * an HTML tag at the front of it.
	 * @throws FileFormatException if <code>line</code> is mal-formatted HTML.
	 */
	protected String extractTag(String line) throws IllegalArgumentException, FileFormatException
	{
		// Eliminate errors due to whitespace before they start!
		line = line.trim().toLowerCase();

		// Make sure this actually starts a tag.
		if (line.charAt(0) != '<')
			throw new IllegalArgumentException("Line does not begin with tag.");

		// Determine if this is a closing tag.
		int i = 1;
		boolean close = false;
		if (line.charAt(1) == '/')
		{
			close = true;
			i++;
		}

		// Determine which tag it is.
		switch (line.charAt(i))
		{
			case 'b':
				return line.substring(_bTag(line, close)).trim();

			case 'e':
			case 'i':
				return line.substring(_iTag(line, close)).trim();

			case 'u':
				return line.substring(_uTag(line, close)).trim();

			case 'f':
				return line.substring(_fontTag(line, close)).trim();
		}

		throw new IllegalArgumentException("Unrecognized tag:"+line);
	}



	////////////////////////////////////////////////////////////////////////////////
	// ------------------------------- Tag Eating ------------------------------- //
	////////////////////////////////////////////////////////////////////////////////

	/**
	 * Eats the passed tag from the passed line. The <code>tag</code> must be the
	 * first thing to occur at the beginning of <code>line</code> (not including
	 * whitespace) otherwise the method will return <code>null</code>.
	 * 
	 * @param line The line to eat away at.
	 * @param tag The tag to be eaten.
	 * 
	 * @return The line without the tag or <code>null</code> if the line did not
	 * start with the tag and <code>errmsg</code> was <code>null</code>.
	 * @throws FileFormatException If <code>errmsg</code> is not <code>null</code>
	 * and the line did not start with the tag.
	 */
	protected String eat(String line, String tag, String errmsg) throws FileFormatException
	{
		line = line.trim();
		if (line.substring(0,tag.length()).toLowerCase().equals(tag.toLowerCase()))
			return line.substring(tag.length());

		if (errmsg == null)
			return null;
		else
			throw new FileFormatException(errmsg);
	}
	
	protected String eatBodyTag(String line) throws FileFormatException
	{
		line = line.trim().toLowerCase();
		if (line.substring(0,5).equals("<body"))
		{
			if (line.charAt(5) == '>')
				return line.substring(6);

			for (int i = 5; i < line.length(); i++)
			{
				if (line.charAt(i) == ' ' || line.charAt(i) == '\t' ||
						line.charAt(i) == '\r' || line.charAt(i) == '\n')
				{
					continue;
				}
				else if (line.charAt(i) >= 'a' && line.charAt(i) <= 'z')
				{
					// TODO write this.
				}
				else
				{
					throw _makeFFE("Unrecognized property", line, i);
				}
			}
		}

		throw new FileFormatException("Malformatted file.");
	}
	
	/**
	 * Parse a <code>&lt;b%gt;</code> or <code>&lt;/b%gt;</code> tag.
	 *
	 * @param line <code>String</code> of the line to get the tag from.
	 * @param close <code>true</code> if this tag is an end-tag.
	 * @return The index of the value after the tag closes.
	 *
	 * @throws FileFormatException If a parsing error occurs.
	 */
	private int _bTag(String line, boolean close) throws FileFormatException
	{
		if (close)
		{
			String tag = line.substring(0,4);
			if (tag.equals("</b>"))
			{
				fs.setNotBold();
				return 4;
			}

			throw _makeFFE("Invalid </b> tag:",line,0);
		}
		else
		{
			String tag = line.substring(0,3);
			if (tag.equals("<b>"))
			{
				fs.setBold();
				return 3;
			}

			throw _makeFFE("Invalid <b> tag:",line,0);
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
	 * @throws FileFormatException If a parsing error occurs.
	 */
	private int _iTag(String line, boolean close) throws FileFormatException
	{
		if (close)
		{
			String iTag = line.substring(0,4);
			String emTag = line.substring(0,5);
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

			throw _makeFFE("Invalid </i> or </em> tag:",line,0);
		}
		else
		{
			String iTag = line.substring(0,3);
			String emTag = line.substring(0,4);
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

			throw _makeFFE("Invalid <i> or <em> tag:",line,0);
		}
	}

	/**
	 * Parse a <code>&lt;u%gt;</code> or <code>&lt;/u%gt;</code> tag.
	 *
	 * @param line <code>String</code> of the line to get the tag from.
	 * @param close <code>true</code> if this tag is an end-tag.
	 * @return The index of the value after the tag closes.
	 *
	 * @throws FileFormatException If something goes wrong during parsing.
	 */
	private int _uTag(String line, boolean close) throws FileFormatException
	{
		if (close)
		{
			String tag = line.substring(0,4);
			if (tag.equals("</u>"))
			{
				fs.setNotUnderlined();
				return 4;
			}

			throw _makeFFE("Invalid </u> tag:",line,0);
		}
		else
		{
			String tag = line.substring(0,3);
			if (tag.equals("<u>"))
			{
				fs.setUnderlined();
				return 3;
			}

			throw _makeFFE("Invalid <u> tag:",line,0);
		}
	}


	/**
	 * Parse a <code>&lt;font%gt;</code> or <code>&lt;/font%gt;</code> tag.
	 *
	 * @param line <code>String</code> of the line to get the tag from.
	 * @param close <code>true</code> if this tag is an end-tag.
	 * @return The index of the character after the tag closes.
	 *
	 * @throws FileFormatException if the <code>String</code> has something
	 * cooky in it.
	 */
	private int _fontTag(String line, boolean close) throws FileFormatException
	{
		if (close)
		{
			String tag = line.substring(0,7);
			if (tag.equals("</font>"))
			{
				fs.popFont();
				return 7;
			}

			throw new IllegalArgumentException("Invalid </font> tag:"+line);
		}
		else
		{
			String tag = line.substring(0,6);
			if (tag.equals("<font>"))
			{
				// No need to do anything to the font stack as nothing has changed.
				return 6;
			}
			else if (tag.equals("<font "))
			{
				int s = -1;
				String f = null;
				Color c = null;
				
				int i = 6;
				for (; i < line.length(); i++)
				{
					if (line.charAt(i) == ' ' || line.charAt(i) == '\n' ||
							line.charAt(i) == '\r' || line.charAt(i) == '\t')
					{
						// Ignore whitespace.
						continue;
					}
					else if ('a' <= line.charAt(i) && line.charAt(i) <= 'z')
					{
						if (line.substring(i,i+4).equals("size"))
						{
							if (s != -1)
								throw _makeFFE("Multiple font size definitions:",line,i+4);

							int start = i+4;
							while (line.charAt(start) == ' ' || line.charAt(start) == '\n' ||
									line.charAt(start) == '\r' || line.charAt(start) == '\t' ||
									line.charAt(start) == '=') start++;
							if (line.charAt(start) == '\"')
							{
								int after = line.indexOf('\"', start+1);
								s = Integer.parseInt(line.substring(start+1,after));
								i = after;
							}
							else
							{
								int after1 = line.indexOf(' ', start+1);
								int after2 = line.indexOf('>', start+1);
								int after = (after1 < after2 ? after1 : after2);
								s = Integer.parseInt(line.substring(start,after));
								i = after-1;
							}
							continue;
						}
						else if (line.substring(i,i+4).equals("face"))
						{
							if (f != null)
								throw _makeFFE("Multiple font face definitions:",line,i+4);

							int start = i+4;
							while (line.charAt(start) == ' ' || line.charAt(start) == '\n' ||
									line.charAt(start) == '\r' || line.charAt(start) == '\t' ||
									line.charAt(start) == '=') start++;
							if (line.charAt(start) != '\"')
								throw _makeFFE("Missing quote:",line,start);

							int end = start+1;
							while (line.charAt(end) == ' ' || line.charAt(end) == '\t' ||
									(line.charAt(end) >= 'a' && line.charAt(end) <= 'z')) end++;
							if (line.charAt(end) != '\"')
								throw _makeFFE("Missing quote:",line,end);

							f = line.substring(start+1,end).trim();
							i = end;
							continue;
						}
						else if (line.substring(i,i+5).equals("color"))
						{
							if (c != null)
								throw _makeFFE("Multiple font color definitions:",line,i+5);

							int start = i+5;
							while (line.charAt(start) == ' ' || line.charAt(start) == '\n' ||
									line.charAt(start) == '\r' || line.charAt(start) == '\t' ||
									line.charAt(start) == '=') start++;
							if (line.charAt(start) != '\"')
								throw _makeFFE("Missing quote:",line,start);

							int end = start+1;
							while (line.charAt(end) == ' ' || line.charAt(end) == '\t' ||
									(line.charAt(end) >= 'a' && line.charAt(end) <= 'z')) end++;
							if (line.charAt(end) != '\"')
								throw _makeFFE("Missing quote:",line,end);

							if (line.charAt(start+1) == '#')	
							{
								c = new Color(Integer.parseInt(line.substring(start+2,start+4), 16),
										Integer.parseInt(line.substring(start+4,start+6), 16),
										Integer.parseInt(line.substring(start+6,start+8), 16));
							}
							else
							{
								String color = line.substring(start+1, end).trim();
								if (color.equals("black"))
									c = Color.black;
								else if (color.equals("blue"))
									c = Color.blue;
								else if (color.equals("red"))
									c = Color.red;
								else if (color.equals("green"))
									c = Color.green;
								else if (color.equals("yellow"))
									c = Color.yellow;
								else if (color.equals("orange"))
									c = Color.orange;
								else if (color.equals("gray"))
									c = Color.gray;
								else if (color.equals("cyan"))
									c = Color.cyan;
								else if (color.equals("white"))
									c = Color.white;
								else
									throw _makeFFE("Unrecognized color:"+color,line,start+1);
							}

							i = end;
						}
						else
						{
							throw _makeFFE("Unrecognized attribute:",line,i);
						}
					}
					else if (line.charAt(i) == '>')
					{
						fs.pushFont(s, f, c);
						return i+1;
					}
					else
					{
						throw _makeFFE("Invalid or mal-formatted <font> tag:",line,i);
					}
				}
			}

			throw new IllegalArgumentException("Invalid or mal-formatted <font> tag:"+line);
		}
	}
	
	/**
	 * Builds an {@link FileFormatException} that looks pretty.
	 * 
	 * @param msg The message to give the exception.
	 * @param line The line that caused the exception.
	 * @param idx The index in the line that caused the exception.
	 */
	private FileFormatException _makeFFE(String msg, String line, int idx)
	{
		StringBuffer buf = new StringBuffer();
		buf.append(msg);
		buf.append("\n\n  ");
		buf.append(line);
		buf.append("\n  ");
		for (int i = 0 ; i < idx; i++)
			buf.append(' ');
		buf.append('^');
		return new FileFormatException(buf.toString());
	}
}
