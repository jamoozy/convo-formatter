package exception;
public class ExistingFileException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public ExistingFileException(String msg)
	{
		super(msg);
	}
}