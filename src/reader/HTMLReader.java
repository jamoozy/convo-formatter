
package reader;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

// Xamal XML parser.
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;

// Swing-dependent version of an HTML parser.
//import javax.swing.text.*;
//import javax.swing.text.html.HTMLDocument;
//import javax.swing.text.html.HTMLEditorKit;
//import javax.swing.text.html.parser.Parser;

//import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

// XML elements.
import org.htmlcleaner.BaseToken;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.ContentToken;
import org.htmlcleaner.EndTagToken;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
//import org.xml.sax.SAXException;

// HTML elements.
//import org.w3c.dom.html.*;


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
   * The Sessions stored in this log file.
   */
  protected Vector<Session> sessions;

//  /**
//   * The link to the file passed to {@link #loadFile(String)}. Should only
//   * be read in {@link #loadFile(String)}.  Will be null elsewhere.
//   */
//  protected BufferedReader reader;
//
//  /**
//   * The next line of input of input from the file passed to {@link #loadFile(String)}.
//   * Should only be referenced in {@link #loadFile(String)}.
//   */
//  protected String line;
//
//  /**
//   * The line number that {@link #line} represents in the file.
//   */
//  protected int lineNumber;

  /**
   * Creates a new <code>FontState</code> object.
   */
  protected HTMLReader()
  {
    fs = new FontState();
    sessions = new Vector<Session>();
//    reader = null;
//    line = null;
//    lineNumber = 0;
  }

  public Iterator<Session> iterator()
  {
    return sessions.iterator();
  }

  /**
   * This must be called at the top of the child class's <code>loadFile(String)</code>
   * method.
   *
   * @param filename The name of the file to open and read from.
   *
   * @see Reader.loadFile(String)
   */
  public boolean loadFile(String filename) throws IOException
  {
    CleanerProperties cp = new CleanerProperties();
    cp.setOmitComments(false);
    cp.setOmitDeprecatedTags(false);
    cp.setOmitUnknownTags(false);
    cp.setPruneTags("er?");
    cp.setTreatDeprecatedTagsAsContent(false);
    cp.setTreatUnknownTagsAsContent(false);
    cp.setUseEmptyElementTags(true);
    HtmlCleaner cleaner = new HtmlCleaner(cp);

    File file = new File(filename);
    if (!file.exists())
      System.err.println("No such file! " + filename);
    else
      System.out.println("File: " + file.getAbsolutePath());
    return loadDocument(cleaner.clean(file));
//    try
//    {
//      parser.parse(filename);
//    }
//    catch (SAXException saxe)
//    {
//      System.err.println("Encountered SAX exception:\n" + saxe.getMessage());
//      throw new IOException("SAX exception.");
//    }
//    return loadDocument(parser.getDocument());
//    try
//    {
//      HTMLEditorKit.ParserCallback callback =
//        new HTMLEditorKit.ParserCallback()
//        {
//          public void flush()
//          {
//            HTMLReader.this.flush();
//          }
//
//          public void handleComment(char[] data, int pos)
//          {
//            HTMLReader.this.handleComment(data, pos);
//          }
//
//          public void handleEndOfLineString(String eol)
//          {
//            HTMLReader.this.handleEndOfLineString(eol);
//          }
//
//          public void handleEndTag(HTML.Tag t, int pos)
//          {
//            HTMLReader.this.handleEndTag(t, pos);
//          }
//
//          public void handleError(String errorMsg, int pos)
//          {
//            HTMLReader.this.handleError(errorMsg, pos);
//          }
//
//          public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos)
//          {
//            HTMLReader.this.handleSimpleTag(t, a, pos);
//          }
//
//          public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos)
//          {
//            HTMLReader.this.handleStartTag(t, a, pos);
//          }
//
//          public void handleText(char[] data, int pos)
//          {
//            HTMLReader.this.handleText(data, pos);
//          }
//        };
//      DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
//      DocumentBuilder bldr = fac.newDocumentBuilder();
//      bldr.parse(filename);
//      return true;
//    }
//    catch (SAXException saxe)
//    {
//      System.err.println(saxe.getMessage());
//      return false;
//    }
//    catch (ParserConfigurationException pce)
//    {
//      System.err.println(pce.getMessage());
//      return false;
//    }
  }
  
  abstract protected boolean loadDocument(TagNode root);

  protected void flush() { }

  protected void handleComment(char[] data, int pos) { }

  protected void handleEndOfLineString(String eol) { }

  protected void handleEndTag(HTML.Tag t, int pos) { }

  protected void handleError(String errorMsg, int pos) { }

  protected void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) { }

  protected void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) { }

  protected void handleText(char[] data, int pos) { }


  /**
   * Loads the document root into the {@link #sessions} array.
   * @param root The root of the document.
   * @return <code>true</code> on success, <code>false</code> o.w.
   */
//  protected abstract boolean loadDocument(TagNode root);

  /**
   * Gets the next line of the file. This updates the {@link #line} and
   * {@link #lineNumber} members.
   *
   * @throws IOException if an I/O error occurs.
   */
//  protected void getNextLine() throws IOException
//  {
//    do
//    {
//      line = reader.readLine().trim();
//      lineNumber++;
//    }
//    while (line != null && line.equals(""));
//  }

  /**
   * Takes the passed {@link String} object and updates the {@link #fs} object
   * with the state of the font. There must be a valid tag at the front of
   * the string. Recognized tags currently include:
   * <ul>
   * <li> <code>&lt;b&gt;</code> for bold text.
   * <li> <code>&lt;em&gt;</code> for emphasized text.
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
//  protected String extractTag(String line) throws IllegalArgumentException, FileFormatException
//  {
//    // Eliminate errors due to whitespace before they start!
//    line = line.trim().toLowerCase();
//
//    // Make sure this actually starts a tag.
//    if (line.charAt(0) != '<')
//      throw new IllegalArgumentException("Line does not begin with tag.");
//
//    // Determine if this is a closing tag.
//    int i = 1;
//    boolean close = false;
//    if (line.charAt(1) == '/')
//    {
//      close = true;
//      i++;
//    }
//
//    // Determine which tag it is.
//    switch (line.charAt(i))
//    {
//      case 'b':
//        return line.substring(_bTag(line, close)).trim();
//
//      case 'e':
//      case 'i':
//        return line.substring(_iTag(line, close)).trim();
//
//      case 'u':
//        return line.substring(_uTag(line, close)).trim();
//
//      case 'f':
//        return line.substring(_fontTag(line, close)).trim();
//    }
//
//    throw new IllegalArgumentException("Unrecognized tag:"+line);
//  }



  ////////////////////////////////////////////////////////////////////////////////
  // ------------------------------- Tag Eating ------------------------------- //
  ////////////////////////////////////////////////////////////////////////////////

  /**
   * Eats the passed tag from {@link #line}. The <code>tag</code> must be the
   * first thing to occur at the beginning of {@link #line} (not including
   * whitespace) otherwise the method will return <code>null</code>.
   *
   * @param tag The tag to be eaten.
   * @param errmsg The error message to put in the FileFormatException if it
   * needs to be thrown.
   *
   * @throws IOException if an I/O error occurs.
   * @throws FileFormatException If <code>errmsg</code> is not <code>null</code>
   * and the line did not start with the tag.
   */
//  protected void eat(String tag, String errmsg) throws IOException, FileFormatException
//  {
//    if (line.substring(0,tag.length()).toLowerCase().equals(tag.toLowerCase()))
//      line = line.substring(tag.length()).trim();
//    else if (errmsg == null)
//      line = null;
//    else
//      throw new FileFormatException("line " + lineNumber + ": " + errmsg);
//    if (line.equals(""))
//      getNextLine();
//  }

  /**
   * Eats the body tag and parses the parameters it contains.
   *
   * @throws IOException If an I/O error occurs.
   * @throws FileFormatException If the <code>&lt;body&gt;</code> tag is
   * formatted in an unexpected way.
   */
//  protected void eatBodyTag() throws IOException, FileFormatException
//  {
//    if (line.substring(0,5).equals("<body"))
//    {
//      if (line.charAt(5) == '>')
//        line = line.substring(6);
//
//      for (int i = 5; i < line.length(); i++)
//      {
//        if (Character.isWhitespace(line.charAt(i)))
//        {
//          // Ignore white space.
//          continue;
//        }
//        else if (Character.isLetter(line.charAt(i)))
//        {
//          // "bgcolor" is a recognized property.
//          if (line.substring(i,i+7).toLowerCase().equals("bgcolor"))
//          {
//            // Find the equals (=) sign.
//            i += 7;
//            while (line.charAt(i) == ' ' || line.charAt(i) == '\t') i++;
//            if (line.charAt(i++) != '=')
//              throw _makeFFE("Excpected \"=\":", line, i);
//            while (line.charAt(i) == ' ' || line.charAt(i) == '\t') i++;
//
//            // Make sure it has a quote.
//            if (line.charAt(i) != '"')
//              throw _makeFFE("\" expected:", line, i);
//
//            // Check if it's numeric representation.
//            if (line.charAt(i+1) == '#')
//            {
//              if (line.charAt(i+8) != '"')
//                throw _makeFFE("Unterminated string.", line, i);
//
//              String RRGGBB = line.substring(i+2, i+8);
//              int rgb = Integer.parseInt(RRGGBB, 16);
//              fs.pushBGColor(new Color(rgb));
//
//              i += 8;
//            }
//            // Otherwise it better be a recognized string.
//            else
//            {
//              // Find the 2nd quote.
//              int start = i+1;
//              int end = i+1;
//              while (line.charAt(end) != '"') end++;
//
//              // List of all the strings we recognize as colors.
//              String color = line.substring(start, end).trim();
//              if (color.equals("black"))
//                fs.pushBGColor(Color.black);
//              else if (color.equals("blue"))
//                fs.pushBGColor(Color.blue);
//              else if (color.equals("red"))
//                fs.pushBGColor(Color.red);
//              else if (color.equals("green"))
//                fs.pushBGColor(Color.green);
//              else if (color.equals("yellow"))
//                fs.pushBGColor(Color.yellow);
//              else if (color.equals("orange"))
//                fs.pushBGColor(Color.orange);
//              else if (color.equals("gray"))
//                fs.pushBGColor(Color.gray);
//              else if (color.equals("cyan"))
//                fs.pushBGColor(Color.cyan);
//              else if (color.equals("white"))
//                fs.pushBGColor(Color.white);
//              else
//                throw _makeFFE("Unrecognized color:", line, i+2);
//
//              // update i.
//              i = end+1;
//            }
//          }
//          else
//          {
//            throw _makeFFE("Unrecognized property:", line, i);
//          }
//        }
//        else if (line.charAt(i) == '>')
//        {
//          line = line.substring(i+1).trim();
//          if (line.equals(""))
//            getNextLine();
//          return;
//        }
//        else
//        {
//          throw _makeFFE("Unrecognized or unexpected symbol:", line, i);
//        }
//      }
//    }
//
//    throw _makeFFE("Malformatted file.", line, 0);
//  }

  /**
   * Parse a <code>&lt;b%gt;</code> or <code>&lt;/b%gt;</code> tag.
   *
   * @param line <code>String</code> of the line to get the tag from.
   * @param close <code>true</code> if this tag is an end-tag.
   * @return The index of the value after the tag closes.
   *
   * @throws FileFormatException If a parsing error occurs.
   */
//  private int _bTag(String line, boolean close) throws FileFormatException
//  {
//    if (close)
//    {
//      String tag = line.substring(0,4);
//      if (tag.equals("</b>"))
//      {
//        fs.setNotBold();
//        return 4;
//      }
//
//      throw _makeFFE("Invalid </b> tag:",line,0);
//    }
//    else
//    {
//      String tag = line.substring(0,3);
//      if (tag.equals("<b>"))
//      {
//        fs.setBold();
//        return 3;
//      }
//
//      throw _makeFFE("Invalid <b> tag:",line,0);
//    }
//  }

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
//  private int _iTag(String line, boolean close) throws FileFormatException
//  {
//    if (close)
//    {
//      String iTag = line.substring(0,4);
//      String emTag = line.substring(0,5);
//      if (iTag.equals("</i>"))
//      {
//        fs.setNotItalicized();
//        return 4;
//      }
//      else if (emTag.equals("</em>"))
//      {
//        fs.setNotItalicized();
//        return 5;
//      }
//
//      throw _makeFFE("Invalid </i> or </em> tag:",line,0);
//    }
//    else
//    {
//      String iTag = line.substring(0,3);
//      String emTag = line.substring(0,4);
//      if (iTag.equals("<i>"))
//      {
//        fs.setItalicized();
//        return 3;
//      }
//      else if (emTag.equals("<em>"))
//      {
//        fs.setItalicized();
//        return 4;
//      }
//
//      throw _makeFFE("Invalid <i> or <em> tag:",line,0);
//    }
//  }

  /**
   * Parse a <code>&lt;u%gt;</code> or <code>&lt;/u%gt;</code> tag.
   *
   * @param line <code>String</code> of the line to get the tag from.
   * @param close <code>true</code> if this tag is an end-tag.
   * @return The index of the value after the tag closes.
   *
   * @throws FileFormatException If something goes wrong during parsing.
   */
//  private int _uTag(String line, boolean close) throws FileFormatException
//  {
//    if (close)
//    {
//      String tag = line.substring(0,4);
//      if (tag.equals("</u>"))
//      {
//        fs.setNotUnderlined();
//        return 4;
//      }
//
//      throw _makeFFE("Invalid </u> tag:",line,0);
//    }
//    else
//    {
//      String tag = line.substring(0,3);
//      if (tag.equals("<u>"))
//      {
//        fs.setUnderlined();
//        return 3;
//      }
//
//      throw _makeFFE("Invalid <u> tag:",line,0);
//    }
//  }


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
//  private int _fontTag(String line, boolean close) throws FileFormatException
//  {
//    if (close)
//    {
//      String tag = line.substring(0,7);
//      if (tag.equals("</font>"))
//      {
//        fs.popFont();
//        return 7;
//      }
//
//      throw new IllegalArgumentException("Invalid </font> tag:"+line);
//    }
//    else
//    {
//      String tag = line.substring(0,6);
//      if (tag.equals("<font>"))
//      {
//        // No need to do anything to the font stack as nothing has changed.
//        return 6;
//      }
//      else if (tag.equals("<font "))
//      {
//        int s = -1;
//        String f = null;
//        Color c = null;
//
//        int i = 6;
//        for (; i < line.length(); i++)
//        {
//          if (Character.isWhitespace(line.charAt(i)))
//          {
//            // Ignore whitespace.
//            continue;
//          }
//          else if (Character.isLetter(line.charAt(i)))
//          {
//            if (line.substring(i,i+4).toLowerCase().equals("size"))
//            {
//              if (s != -1)
//                throw _makeFFE("Multiple font size definitions:",line,i+4);
//
//              int start = i+4;
//              while (Character.isWhitespace(line.charAt(start)) ||
//                  line.charAt(start) == '=') start++;
//              if (line.charAt(start) == '\"')
//              {
//                int after = line.indexOf('\"', start+1);
//                s = Integer.parseInt(line.substring(start+1,after));
//                i = after;
//              }
//              else
//              {
//                int after1 = line.indexOf(' ', start+1);
//                int after2 = line.indexOf('>', start+1);
//                int after = (after1 < after2 ? after1 : after2);
//                s = Integer.parseInt(line.substring(start,after));
//                i = after-1;
//              }
//              continue;
//            }
//            else if (line.substring(i,i+4).equals("face"))
//            {
//              if (f != null)
//                throw _makeFFE("Multiple font face definitions:",line,i+4);
//
//              int start = i+4;
//              while (Character.isWhitespace(line.charAt(start)) ||
//                  line.charAt(start) == '=') start++;
//              if (line.charAt(start) != '\"')
//                throw _makeFFE("Missing quote:",line,start);
//
//              int end = start+1;
//              while (line.charAt(end) == ' ' || line.charAt(end) == '\t' ||
//                  Character.isWhitespace(line.charAt(end))) end++;
//              if (line.charAt(end) != '\"')
//                throw _makeFFE("Missing quote:",line,end);
//
//              f = line.substring(start+1,end).trim();
//              i = end;
//              continue;
//            }
//            else if (line.substring(i,i+5).equals("color"))
//            {
//              if (c != null)
//                throw _makeFFE("Multiple font color definitions:",line,i+5);
//
//              int start = i+5;
//              while (Character.isWhitespace(line.charAt(start)) ||
//                  line.charAt(start) == '=') start++;
//              if (line.charAt(start) != '\"')
//                throw _makeFFE("Missing quote:",line,start);
//
//              int end = start+1;
//              while (line.charAt(end) == ' ' || line.charAt(end) == '\t' ||
//                  Character.isWhitespace(line.charAt(end))) end++;
//              if (line.charAt(end) != '\"')
//                throw _makeFFE("Missing quote:",line,end);
//
//              if (line.charAt(start+1) == '#')
//              {
//                c = new Color(Integer.parseInt(line.substring(start+2,start+8), 16));
//              }
//              else
//              {
//                String color = line.substring(start+1, end).trim();
//                if (color.equals("black"))
//                  c = Color.black;
//                else if (color.equals("blue"))
//                  c = Color.blue;
//                else if (color.equals("red"))
//                  c = Color.red;
//                else if (color.equals("green"))
//                  c = Color.green;
//                else if (color.equals("yellow"))
//                  c = Color.yellow;
//                else if (color.equals("orange"))
//                  c = Color.orange;
//                else if (color.equals("gray"))
//                  c = Color.gray;
//                else if (color.equals("cyan"))
//                  c = Color.cyan;
//                else if (color.equals("white"))
//                  c = Color.white;
//                else
//                  throw _makeFFE("Unrecognized color:"+color,line,start+1);
//              }
//
//              i = end;
//            }
//            else
//            {
//              throw _makeFFE("Unrecognized attribute:",line,i);
//            }
//          }
//          else if (line.charAt(i) == '>')
//          {
//            fs.pushFont(s, f, c);
//            return i+1;
//          }
//          else
//          {
//            throw _makeFFE("Invalid or mal-formatted <font> tag:",line,i);
//          }
//        }
//      }
//
//      throw new IllegalArgumentException("Invalid or mal-formatted <font> tag:"+line);
//    }
//  }

  /**
   * Builds an {@link FileFormatException} that looks pretty.
   *
   * @param msg The message to give the exception.
   * @param line The line that caused the exception.
   * @param idx The index in the line that caused the exception.
   */
//  private FileFormatException _makeFFE(String msg, String line, int idx)
//  {
//    StringBuffer buf = new StringBuffer();
//    buf.append(msg);
//    buf.append("\n\n  ");
//    buf.append(line);
//    buf.append("\n  ");
//    for (int i = 0 ; i < idx; i++)
//      buf.append(' ');
//    buf.append('^');
//    return new FileFormatException(buf.toString());
//  }
}
