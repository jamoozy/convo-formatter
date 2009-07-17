package reader;

import java.io.IOException;

public class FileFormatException extends IOException
{
  private static final long serialVersionUID = -3045348347986573107L;

  public FileFormatException() { super(); }
  public FileFormatException(String msg) { super(msg); }
}
