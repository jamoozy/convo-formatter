package reader.lexer.html;

public class UnknownTagException extends RuntimeException
{
	private static final long serialVersionUID = 1231203426024358852L;

	public UnknownTagException()
	{
		super();
	}
	
	public UnknownTagException(String msg)
	{
		super(msg);
	}
}
