package tools.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JDKLogger implements tools.log.Logger {

	private Logger log = null;

	private JDKLogger() {
	}

	public static JDKLogger getInstant() {

		JDKLogger jdkLogger = new JDKLogger();
		jdkLogger.log = Logger.getLogger("frame");
		return jdkLogger;
	}

	public void info(String msg) {

		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

		log.logp(Level.INFO, stackTrace[3].getClassName(),
				stackTrace[3].getMethodName() + ":" + stackTrace[3].getLineNumber(), msg);
	}

	public void debug(String msg) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		log.logp(DEBUG, stackTrace[3].getClassName(),
				stackTrace[3].getMethodName() + ":" + stackTrace[3].getLineNumber(), msg);
	}

	public void warn(String msg) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		log.logp(Level.WARNING, stackTrace[3].getClassName(),
				stackTrace[3].getMethodName() + ":" + stackTrace[3].getLineNumber(), msg);
	}

	public void error(String msg) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		log.logp(ERROR, stackTrace[3].getClassName(),
				stackTrace[3].getMethodName() + ":" + stackTrace[3].getLineNumber(), msg);
	}

	public void trace(String msg) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		log.logp(TRACE, stackTrace[3].getClassName(),
				stackTrace[3].getMethodName() + ":" + stackTrace[3].getLineNumber(), msg);
	}

	public void fatal(String msg) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		log.logp(FATAL, stackTrace[3].getClassName(),
				stackTrace[3].getMethodName() + ":" + stackTrace[3].getLineNumber(), msg);
	}
	
	public void error(Throwable thrown) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		log.logp(ERROR, stackTrace[3].getClassName(),
				stackTrace[3].getMethodName() + ":" + stackTrace[3].getLineNumber(), getTrace(thrown));
		
	}


	private static String getTrace(Throwable t) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		t.printStackTrace(writer);
		StringBuffer buffer = stringWriter.getBuffer();
		return buffer.toString();
	}
	
	private final static Level DEBUG = new FrameLevel("DEBUG", 810);
	private final static Level ERROR = new FrameLevel("ERROR", 820);
	private final static Level TRACE = new FrameLevel("TRACE", 830);
	private final static Level FATAL = new FrameLevel("FATAL", 840);

	public static class FrameLevel extends Level {
		private static final long serialVersionUID = 2217795606430134573L;

		protected FrameLevel(String name, int value) {
			super(name, value);
		}

	}


}
