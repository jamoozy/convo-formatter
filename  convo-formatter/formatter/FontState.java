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
	 * Maximum number of consecutive <code>set</code>s this object can
	 * keep track of before overflowing.
	 */
	public static int MAX_DEPTH = 10;

	// These values form a "stack" of sorts, which allow multiple
	// nexted HTML formatting tags to be propperly kept track of.
	private int size[];
	private String typeface[];
	private Color color[];

	// These keep track of how far in each stack we are by pointing
	// to the element one past the top-most element.
	private int sizei, typefacei, colori;
	
	// These are meant to keep track of the number of times bold,
	// italics and underline tags have been activated or deactivated.
	// If the number is positive, then the value is true.  When 0,
	// the value is false.  If the number is ever negative, then an
	// error has occured (probably because of a mal-formatted file.).
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
		sizei = typefacei = colori = 1;
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
		sizei = typefacei = colori = 1;
	}

	public void setBold() { bold++; }

	public void setNotBold() { bold--; }

	public boolean isBold() { return bold != 0; }

	public void setItalicized() { italic++; }

	public void setNotItalicized() { italic--; }

	public boolean isItalicized() { return italic != 0; }

	public void setUnderlined() { underline++; }

	public void setNotUnderlined() { underline--; }

	public boolean isUnderlined() { return underline != 0; }

	public void pushFontSize(int s)
	{
		if (sizei == MAX_DEPTH)
			throw new RuntimeException("Stack full");

		size[sizei++] = s;
	}

	public void popFontSize()
	{
		if (sizei == 1)
			throw new RuntimeException("Stack almost empty!");

		sizei--;
	}

	public int getFontSize()
	{
		return size[sizei-1];
	}

	public void pushFontFace(String s)
	{
		if (sizei == MAX_DEPTH)
			throw new RuntimeException("Stack full");

		typeface[typefacei++] = s;
	}

	public void popFontFace()
	{
		if (typefacei == 1)
			throw new RuntimeException("Stack almost empty!");

		typefacei--;
	}

	public String getFontFace()
	{
		return typeface[typefacei-1];
	}
	
	public void pushColor(Color c)
	{
		if (sizei == MAX_DEPTH)
			throw new RuntimeException("Stack full");

		color[colori++] = c;
	}

	public void popColor()
	{
		if (colori == 1)
			throw new RuntimeException("Stack almost empty!");

		colori--;
	}

	public Color getColor()
	{
		return color[colori-1];
	}
}
