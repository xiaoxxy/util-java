package tools.ssdb;

import java.io.IOException;

import org.nutz.ssdb4j.SSDBs;
import org.nutz.ssdb4j.spi.Response;
import org.nutz.ssdb4j.spi.SSDB;
import org.nutz.ssdb4j.spi.SSDBException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.ObjectMapper;

import tools.common.ClassHelper;
import tools.common.ConfigFileParseHelper;
import tools.log.Log;


public final class SSDBUtils {

	public static SSDB ssdb = null;

	public static void start() {

		// 载入文件
		Log.info("连接SSDB服务器...");

		Document configElement = ConfigFileParseHelper.getRootElement();
		NodeList ssdbNodeList = configElement.getElementsByTagName("ssdb");
		Element item = null;

		item = (Element) ssdbNodeList.item(0);
		String host = item.getAttribute("host").trim();
		String passwd = item.getAttribute("passwd");
		int port = Integer.parseInt(item.getAttribute("port").trim());
		int timeout = Integer.parseInt(item.getAttribute("timeout").trim());
		//ssdb = SSDBs.simple(host, port, timeout);
		ssdb = SSDBs.pool(host, port, timeout, null);

		if (passwd.length() != 0)
			ssdb.auth(passwd);
		ssdb.ping();
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
			start();
			Object tryAgain = ClassHelper.tryAgain(e, new Class[] { String.class, String.class, Object.class },
					new Object[] { name, key, val });
			if (tryAgain == null) {
				Log.error(e);
			} else {
				return (boolean) tryAgain;
			}
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
			start();
			Object tryAgain = ClassHelper.tryAgain(e, new Class[] { String.class, String.class, Object.class },
					new Object[] { name, key, val });
			if (tryAgain == null) {
				Log.error(e);
			} else {
				return (boolean) tryAgain;
			}
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
			start();
			Object tryAgain = ClassHelper.tryAgain(e, new Class[] { String.class, String.class, Object.class },
					new Object[] { key, val });
			if (tryAgain == null) {
				Log.error(e);
			} else {
				return (boolean) tryAgain;
			}
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
			start();
			Object tryAgain = ClassHelper.tryAgain(e, new Class[] { String.class, String.class, Class.class },
					new Object[] { name, key, clazz });
			if (tryAgain == null) {
				Log.error(e);
			}
			return (T) tryAgain;
		}
	}
	
	public static SSDB getInstance(){
		if(ssdb == null)
			start();
		try {
			if ( !"ok".equals(ssdb.ping().stat))
				start();
		}catch (Exception e){
			start();
		}
		return ssdb;
	}
}
