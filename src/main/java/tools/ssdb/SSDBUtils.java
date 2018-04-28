package ms.auth.common;

import java.io.IOException;

import tools.common.PlatformProp;
import org.nutz.ssdb4j.SSDBs;
import org.nutz.ssdb4j.spi.Response;
import org.nutz.ssdb4j.spi.SSDB;
import org.nutz.ssdb4j.spi.SSDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class SSDBUtils {

	protected static final Logger logger = LoggerFactory.getLogger(SSDBUtils.class);

	public static SSDB ssdb = null;

	public static void start() {

		// 载入文件
		logger.info("连接SSDB服务器...");

		String host= PlatformProp.getProperty("ssdb.host");
		String passwd = PlatformProp.getProperty("ssdb.password");
		int port = Integer.parseInt(PlatformProp.getProperty("ssdb.port"));
		int timeout =  Integer.parseInt(PlatformProp.getProperty("ssdb.timeout"));
		ssdb = SSDBs.pool(host, port, timeout,null);
		// 或者指定连接池配置
		//		GenericObjectPool.Config config = new GenericObjectPool.Config();
		//		config.lifo = true;
		//		config.maxActive = 3;
		//		config.maxIdle = 3;
		//		config.maxWait = 1000 * 5;
		//		config.minEvictableIdleTimeMillis = 1000 * 10;
		//		config.minIdle = 3;
		//		config.numTestsPerEvictionRun = 100;
		//		config.softMinEvictableIdleTimeMillis = 1000 * 10;
		//		config.testOnBorrow = true;
		//		config.testOnReturn = true;
		//		config.testWhileIdle = true;
		//		config.timeBetweenEvictionRunsMillis = 1000 * 5;
		//		config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
		//		ssdb = SSDBs.pool(host, port, timeout,config);

		if (passwd.length() != 0)
			ssdb.auth(passwd);
		ssdb.ping();
		logger.info("连接SSDB服务器完成");
	}

	/**
	 * 
	 * @param name
	 *            hashmap的名称
	 * @param key
	 *            hashmap的键
	 * @param val
	 *            hashmap的值 <b>可序列化对象</b>
	 * @return
	 */
	public static boolean hset(String name, String key, Object val) {
		try {
			ObjectMapper writeValueAsString = new ObjectMapper();
			String vsStr = writeValueAsString.writeValueAsString(val);
			if ("ok".equals(ssdb.hset(name, key, vsStr).stat)) {
				return true;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (SSDBException e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 
	 * @param name
	 *            hashmap的名称
	 * @param key
	 *            hashmap的键
	 * @param val
	 *            hashmap的值 <b>可序列化对象</b>
	 * @return
	 */
	public static boolean hincr(String name, String key, int val) {
		try {
			if ("ok".equals(ssdb.hincr(name, key, val).stat)) {
				return true;
			}
		} catch (SSDBException e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	/**
	 * @param key
	 *            hashmap的键
	 * @param val
	 *            hashmap的值 <b>可序列化对象</b>
	 * @return
	 */
	public static boolean expire(String key, int val) {
		try {
			if ("ok".equals(ssdb.expire(key, val).stat)) {
				return true;
			}
		} catch (SSDBException e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 
	 * @param name
	 *            hashmap的名称
	 * @param key
	 *            hashmap的键
	 * @param clazz
	 *            hashmap的值 <b>反序列化后返回</>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T hget(String name, String key, Class<T> clazz) {
		try {
			ObjectMapper writeValueAsString = new ObjectMapper();
			Response hget = ssdb.hget(name, key);
			if (hget.notFound()) {
				return null;
			}
			String vsStr = hget.asString();

			return writeValueAsString.readValue(vsStr, clazz);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (SSDBException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public static SSDB getInstance(){
		if(ssdb == null)
			start();
		return ssdb;
	}

}

