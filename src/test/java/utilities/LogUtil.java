package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author TestLink
 *
 */
public class LogUtil {

	public LogUtil() {
	}

	private static ThreadLocal<Logger> logger = new ThreadLocal<Logger>();

	/**
	 * @return the logger
	 */
	private static ThreadLocal<Logger> getLogger() {
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	private static void setLogger(Class<?> c) {
		logger.set(LogManager.getLogger(c));
	}

	/**
	 * @param clazz
	 * @param message
	 */
	public static void infoLog(Class<?> clazz, String message) {
		setLogger(clazz);
		getLogger().get().info(message);

	}

	/**
	 * @param clazz
	 * @param message
	 * @param t
	 */
	public static void errorLog(Class<?> clazz, String message, Throwable t) {
		setLogger(clazz);
		getLogger().get().error(message, t);
		getLogger().get().error("----------------------------------------------------------------------");

	}

	/**
	 * @param clazz
	 * @param message
	 */
	public static void errorLog(Class<?> clazz, String message) {
		setLogger(clazz);
		getLogger().get().error(message);
		getLogger().get().error("-----------------------------------------------------------------------");

	}

}
