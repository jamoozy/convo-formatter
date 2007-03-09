package exception;

public class NoFileException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public NoFileException(String s)
	{
		super(s);
	}
}
