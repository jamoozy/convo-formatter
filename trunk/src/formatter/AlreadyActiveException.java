package formatter;

public class AlreadyActiveException extends RuntimeException
{

  private static final long serialVersionUID = -8719548393879810627L;

  public AlreadyActiveException() { super(); }
  public AlreadyActiveException(String s) { super(s); }
}
