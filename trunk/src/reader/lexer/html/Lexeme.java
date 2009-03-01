package reader.lexer.html;

/**
 * This class represents a single token of information in a file.
 * 
 * @author Andrew Correa
 */

// TODO: write this.
public class Lexeme
{
  private Type type;
  private String message;
  
  /**
   * Convenience Constructor.&nbspSame as <code>Lexeme(t,null)</code>.
   * 
   * @param t The type of the <code>Lexeme</code>.
   */
  public Lexeme(Type t)
  {
    this(t,null);
  }
  
  /**
   * Constructs a new <code>Lexeme</code>.
   * 
   * @param t The <code>Type</code> of the object.
   * @param s The <code>String</code> associated with certain <code>Type</code>s.
   */
  public Lexeme(Type t, String s)
  {
    type = t;
    message = s;
  }
  
  /**
   * These are known used tags for an HTML document.
   * 
   * @author Andrew
   */
  public enum Type
  {
    bgcolor(true),
    body(true),
    br(true),
    color(true),
    eof(false),
    face(true),
    font(true),
    gt(false),
    lang(true),
    lt(false),
    newline(false),
    text(false),     // Like the contents of a message.
    span(true),
    style(true),
//    string(false),   // I.e. a value for an HTML feild.
    size(true);
    
    private boolean html;
    
    private Type(boolean html)
    {
      this.html = html;
    }
    
    public static Type toType(String s)
    {
      for (Type t : values())
        if (t.name().toLowerCase().equals(s))
          if (t.isHTML())
            return t;

      throw new UnknownTagException("Unknown tag: " + s);
    }
    
    public boolean isHTML()
    {
      return html;
    }
  }
}
