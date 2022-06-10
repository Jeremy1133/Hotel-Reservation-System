package project;

public class EmptySetException extends Exception
{
	String message;
	public EmptySetException()
	{
		message  = "Entry Not Database";
	}
	public EmptySetException(String m)
	{
		message = m;
	}
	
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String m)
	{
		message = m;
	}
}
