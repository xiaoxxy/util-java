package tools.log;

public interface Logger {
	
	public void info(String msg);

	public void debug(String msg);

	public void warn(String msg);

	public void error(String msg);

	public void error(Throwable thrown);

	public void trace(String msg);

	public void fatal(String msg);

}
