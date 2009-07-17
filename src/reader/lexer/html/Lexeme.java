package reader.lexer.html;

/**
 * This class represents a single token of information in a file.
 *
 * @author Andrew Correa
 */

// TODO: write this.
public class Lexeme
{
  /** The type of this lexeme. */
  private Type type;

  /** A message to describe this Lexeme a bit more. */
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
   * Gets the type of this lexeme.
   */
  public Type getType()
  {
    return type;
  }

  /**
   * Gets a message/description for this lexeme.
   */
  public String getMessage()
  {
    return message;
  }

  /**
   * These are known used tags for an HTML document.
   *
   * @author jamoozy
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
    size(true);

    private boolean html;

    /**
     * Create a new type.
     */
    private Type(boolean html)
    {
      this.html = html;
    }

    /**
     * Converts a string to a type.
     */
    public static Type toType(String s)
    {
      for (Type t : values())
        if (t.name().toLowerCase().equals(s))
          if (t.isHTML())
            return t;

      throw new UnknownTagException("Unknown tag: " + s);
    }

    /**
     * Is this HTML ... ?
     */
    public boolean isHTML()
    {
      return html;
    }
  }
}
