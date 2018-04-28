package tools.log;

public class Log {
	private Log() {
	}

	private static Logger _logger = null;

	public static Logger getLogger() {

		if (_logger == null)
			_logger = JDKLogger.getInstant();

		return _logger;
	}

	public static void setLogger(Logger logger) {
		_logger = logger;
	}

	public static void info(String msg) {
		getLogger().info(msg);
	}

	public static void debug(String msg) {
		getLogger().debug(msg);
	}

	public static void warn(String msg) {
		getLogger().warn(msg);
	}

	public static void error(String msg) {
		getLogger().error(msg);
	}

	public static void error(Throwable thrown) {
		getLogger().error(thrown);
	}

	public static void trace(String msg) {
		getLogger().trace(msg);
	}

	public static void fatal(String msg) {
		getLogger().fatal(msg);
	}

}
