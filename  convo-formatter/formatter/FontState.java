package formatter;

import java.awt.Color;



/**
 * This class is meant to keep track of what the HTML tags of an HTML document
 * have altered the state of the text into.  In other words, this class makes
 * it trivial to keep track of what the HTML tags have commanded the browser
 * to do.  This is useful in this program because it allows for an easy way
 * of keeping track of what to put in new message parts. 
 */
public class FontState
{
	/**
	 * Maximum number of consecutive calls to <code>set</code> methods
	 * this object can keep track of before overflowing. Currently 10.
	 */
	public static int MAX_DEPTH = 10;

	// These values form a "stack" of sorts, which allow multiple
	// nexted HTML formatting tags to be propperly kept track of.
	private int size[];
	private String typeface[];
	private Color color[];

	// This keeps track of how far in each of the above stacks we are
	// by pointing to the element one past the top-most element.
	private int fonti;
	
	// These are meant to keep track of the number of times bold,
	// italics and underline tags have been activated or deactivated.
	// If the number is positive, then the value is true.  When 0,
	// the value is false.  If the number is ever negative, then an
	// error has occured (probably because of a mal-formatted file).
	private int bold, italic, underline;
	
	/**
	 * Creates a new <code>FontState</code> with all the default values in place.
	 */
	public FontState()
	{
		size = new int[MAX_DEPTH];
		typeface = new String[MAX_DEPTH];

		size[0] = 12;
		typeface[0] = "arial";
		color[0] = Color.black;

		bold = italic = underline = 0;
		fonti = 1;
	}
	
	/**
	 * Creates a new <code>FontState</code> with the passed two default values.
	 * 
	 * @param defaultSize The default (starting) size.
	 * @param defaultTypeface The default (starting) typeface.
	 * @param defaultColor The default (starting) color.
	 */
	public FontState(int defaultSize, String defaultTypeface, Color defaultColor)
	{
		size = new int[MAX_DEPTH];
		typeface = new String[MAX_DEPTH];

		size[0] = defaultSize;
		typeface[0] = defaultTypeface;
		color[0] = defaultColor;

		bold = italic = underline = 0;
		fonti = 1;
	}

	/**
	 * Register that another <code>&lt;b&gt;</code> or
	 * <code>&lt;em&gt;</code> tag has been seen.
	 */
	public void setBold() { bold++; }

	/**
	 * Register that another <code>&lt;/b&gt;</code> or
	 * <code>&lt;/em&gt;</code>tag has been seen.
	 */
	public void setNotBold() { bold--; }

	/** Determines if text has been set to bold. */
	public boolean isBold() { return bold > 0; }

	/** Register that another <code>&lt;i&gt;</code> tag has been seen. */
	public void setItalicized() { italic++; }

	/** Register that another <code>&lt;/i&gt;</code> tag has been seen. */
	public void setNotItalicized() { italic--; }

	/** Determines if text has been set to italized. */
	public boolean isItalicized() { return italic > 0; }

	/** Register that another <code>&lt;u&gt;</code> tag has been seen. */
	public void setUnderlined() { underline++; }

	/** Register that another <code>&lt;/u&gt;</code> tag has been seen. */
	public void setNotUnderlined() { underline--; }

	/** Determines if text has been set to underlined. */
	public boolean isUnderlined() { return underline > 0; }

	/**
	 * Puts another font on the stack of fonts.
	 *
	 * @param s The new font size. <code>-1</code> means "use last"
	 * @param f The new font face. <code>null</code> means "use last"
	 * @param c The new font color. <code>null</code> means "use last"
	 *
	 * @throws RuntimeException if too man different font changes have been pushed.
	 */
	public void pushFont(int s, String f, Color c)
	{
		if (fonti == MAX_DEPTH)
			throw new RuntimeException("Stack full");

		size[fonti] = s;
		typeface[fonti] = f;
		color[fonti++] = c;
	}

	/**
	 * Removes the top-most font from the stack.
	 *
	 * @throws RuntimeException if the stack is empty (there are
	 * no font sizes to pop).
	 */
	public void popFont()
	{
		if (fonti == 1)
			throw new RuntimeException("Stack empty!");

		fonti--;
	}

	/**
	 * Get the current font size.
	 * @return The current font size.
	 */
	public int getFontSize()
	{
		// Guaranteed to have something in the 0th entry.
		return size[fonti-1];
	}

	/**
	 * Get the current font face.
	 * @return The name of the current font face.
	 */
	public String getFontFace()
	{
		// Guaranteed to have at least one.
		return typeface[fonti-1];
	}
	
	/**
	 * Get the current color.
	 * @return The current color.
	 */
	public Color getFontColor()
	{
		// Guaranteed to have at least one.
		return color[fonti-1];
	}
}
