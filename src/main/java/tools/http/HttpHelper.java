package tools.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import tools.log.Log;


public final class HttpHelper {
	/**
	 * URLDecode
	 * 
	 * @param s
	 * @param enc :  The name of a supported
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decode(String s, String enc) throws UnsupportedEncodingException {
		return java.net.URLDecoder.decode(s, enc);

	}

	/**
	 * URLEncode
	 * 
	 * @param s
	 * @param enc
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String s, String enc) throws UnsupportedEncodingException {
		return java.net.URLEncoder.encode(s, enc);
	}

	/**
	 * 
	 * @param url
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, String charset) throws IOException {
		return get(url, charset, 0);
	}

	/**
	 * 
	 * @param url
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, String charset, int timeout) throws IOException {
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if (timeout != 0) {
				connection.setConnectTimeout(timeout);
				connection.setReadTimeout(timeout);
			}
			connection.connect();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				Log.error(e2.toString());
			}
		}
		return result;
	}

	/**
	 * 
	 * @param url
	 * @param param
	 * @param charset
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> param, String charset) {
		return post(url, param, charset, 0);
	}

	/**
	 * 
	 * @param url
	 * @param param
	 * @param charset
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static String post(String url, Map<String, String> param, String charset, int timeout) {

		StringBuffer httpString = new StringBuffer();

		StringBuffer buffer = new StringBuffer();
		if (param != null && !param.isEmpty()) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue())).append("&");

			}
			buffer.deleteCharAt(buffer.length() - 1);
		}
		httpString.append(url + "?" + buffer + System.lineSeparator());

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		long start = System.currentTimeMillis();
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if (timeout != 0) {
				conn.setConnectTimeout(timeout);
				conn.setReadTimeout(timeout);
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(buffer);
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			httpString.append(System.currentTimeMillis() - start + "|" + result);
			Log.info(httpString.toString());
		}
		return result;
	}

	/**
	 * 
	 * @param url
	 * @param param
	 * @param charset
	 */
	public static String sendPostUrl(String url, String param, String charset) {

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 
	 * @param url
	 * @param param
	 * @param charset
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static String postDeflate(String url, Map<String, String> param, String charset, int timeout) {

		StringBuffer httpString = new StringBuffer();

		StringBuffer buffer = new StringBuffer();
		if (param != null && !param.isEmpty()) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue())).append("&");

			}
			buffer.deleteCharAt(buffer.length() - 1);
		}

		httpString.append(url + "?" + buffer + System.lineSeparator());

		PrintWriter out = null;
		BufferedReader in = null;
		String result = null;

		long start = System.currentTimeMillis();
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Accept-Encoding", "deflate");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if (timeout != 0) {
				conn.setConnectTimeout(timeout);
				conn.setReadTimeout(timeout);
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(buffer);
			out.flush();
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());

			ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
			byte[] temp = new byte[1024];
			int size = 0;
			while ((size = bis.read(temp)) != -1) {
				bos.write(temp, 0, size);
			}
			bis.close();
			byte[] data = bos.toByteArray();
			byte[] output = decompress(data);
			result = new String(output);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			httpString.append(System.currentTimeMillis() - start + "|" + result);
			Log.info(httpString.toString());
		}
		return result;
	}

	/**
	 * 
	 * @param url
	 * @param param
	 * @param charset
	 * @throws IOException
	 */
	public static String postUrl(String url, String param, String charset) throws IOException {

		return postUrl(url, param, charset, 0);
	}

	/**
	 * 
	 * @param url
	 * @param param
	 * @param charset
	 * @throws IOException
	 */
	public static String postUrl(String url, String param, String charset, int timeout) throws IOException {

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if (timeout != 0) {
				conn.setConnectTimeout(timeout);
				conn.setReadTimeout(timeout);
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
			OutputStream outputStream = conn.getOutputStream();
			out = new PrintWriter(new OutputStreamWriter(outputStream, "utf-8"));
			out.print(param);
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	private static final int BUFFER_SIZE = 4096;

	private static byte[] decompress(byte[] data) throws IOException, DataFormatException {

		Inflater inflater = new Inflater();
		inflater.setInput(data);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[BUFFER_SIZE];
		while (!inflater.finished()) {
			int count = inflater.inflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		byte[] output = outputStream.toByteArray();
		outputStream.close();
		return output;
	}

}
