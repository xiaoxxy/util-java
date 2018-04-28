package tools.common;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tools.log.Log;

public class StringHelper {
	public static String encode(String str) {
		byte[] b = Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8));
		String s = new String(b);
		return s;
	}

	public static String decode(String str) {
		byte[] b = Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
		String s = new String(b);
		return s;
	}

	public static String MD5For32(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes("utf-8"));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();

		} catch (NoSuchAlgorithmException e) {
			Log.error(e);
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			Log.error(e);
			throw new RuntimeException(e);
		}
	}

	public static String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	/*
	 * 将Map对象按照key=value单元组合成字符串，组合时按照key排序号，各单元之间以'&'连接
	 */
	public static String MapSortToString(Map<String, String> params, boolean isEncode) {
		StringBuffer sb = new StringBuffer();
		if (null != params && params.size() > 0) {
			Object[] keyArr = params.keySet().toArray();
			Arrays.sort(keyArr);
			Object o = null;
			for (int i = 0; i < keyArr.length; i++) {
				o = keyArr[i];
				sb.append(o);
				sb.append("=");
				if (isEncode) {
					try {
						sb.append(java.net.URLEncoder.encode(params.get(o) == null ? "" : params.get(o).toString(),
								"UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					sb.append(params.get(o) == null ? "" : params.get(o).toString());
				}
				if (i < (keyArr.length - 1)) {
					sb.append("&");
				}
			}
			keyArr = null;
		}
		return sb.toString();
	}

	public static String unicodeToString(String str) {
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);

		while (matcher.find()) {
			char ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch + "");
		}
		return str;
	}

	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static String nullToEmpty(String string) {
		return (string == null) ? "" : string;
	}

	public static String nullToEmpty(Object obj) {
		return (obj == null) ? "" : obj.toString();
	}

	public static String emptyToNull(String string) {
		return isNullOrEmpty(string) ? null : string;
	}

	public static boolean isNullOrEmpty(String string) {
		return string == null || string.length() == 0; // string.isEmpty() in
														// Java 6
	}

	public static String padStart(String string, int minLength, char padChar) {

		checkNotNull(string);

		if (string.length() >= minLength) {
			return string;
		}
		StringBuilder sb = new StringBuilder(minLength);
		for (int i = string.length(); i < minLength; i++) {
			sb.append(padChar);
		}
		sb.append(string);
		return sb.toString();
	}

	public static String padEnd(String string, int minLength, char padChar) {

		checkNotNull(string);

		if (string.length() >= minLength) {
			return string;
		}
		StringBuilder sb = new StringBuilder(minLength);
		sb.append(string);
		for (int i = string.length(); i < minLength; i++) {
			sb.append(padChar);
		}
		return sb.toString();
	}

	public static <T> T checkNotNull(T reference) {
		if (reference == null) {
			throw new NullPointerException();
		}
		return reference;
	}

	public static String defaultString(String v) {
		if (isNullOrEmpty(v))
			return "";
		return v;
	}

	public static String defaultString(String v, String dv) {
		if (isNullOrEmpty(v))
			return dv;
		return v;
	}

	public static boolean isNotBlank(String v) {
		return !isNullOrEmpty(v);
	}

	private static Map<String, String> parseKeyValuePair(String str, String itemSeparator) {
		Pattern KVP_PATTERN = Pattern.compile("([_.a-zA-Z0-9][-_.a-zA-Z0-9]*)[=](.*)");
		String[] tmp = str.split(itemSeparator);
		Map<String, String> map = new HashMap<String, String>(tmp.length);
		for (int i = 0; i < tmp.length; i++) {
			Matcher matcher = KVP_PATTERN.matcher(tmp[i]);
			if (matcher.matches() == false)
				continue;
			map.put(matcher.group(1), matcher.group(2));
		}
		return map;
	}

	public static String getQueryStringValue(String qs, String key) {
		Map<String, String> map = StringHelper.parseQueryString(qs);
		return map.get(key);
	}

	public static Map<String, String> parseQueryString(String qs) {
		if (qs == null || qs.length() == 0)
			return new HashMap<String, String>();
		return parseKeyValuePair(qs, "\\&");
	}

	public static String join(Collection<String> coll, String split) {
		if (coll.isEmpty())
			return "";

		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (String s : coll) {
			if (isFirst)
				isFirst = false;
			else
				sb.append(split);
			sb.append(s);
		}
		return sb.toString();
	}

	public static boolean equals(CharSequence cs1, CharSequence cs2) {
		return cs1 == cs2 ? true
				: (cs1 != null && cs2 != null ? (cs1 instanceof String && cs2 instanceof String ? cs1.equals(cs2)
						: regionMatches(cs1, false, 0, cs2, 0, Math.max(cs1.length(), cs2.length()))) : false);
	}

	public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
		return str1 != null && str2 != null ? (str1 == str2 ? true
				: (str1.length() != str2.length() ? false : regionMatches(str1, true, 0, str2, 0, str1.length())))
				: str1 == str2;
	}

	static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring, int start,
			int length) {
		if (cs instanceof String && substring instanceof String) {
			return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
		} else {
			int index1 = thisStart;
			int index2 = start;
			int tmpLen = length;

			while (tmpLen-- > 0) {
				char c1 = cs.charAt(index1++);
				char c2 = substring.charAt(index2++);
				if (c1 != c2) {
					if (!ignoreCase) {
						return false;
					}

					if (Character.toUpperCase(c1) != Character.toUpperCase(c2)
							&& Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
						return false;
					}
				}
			}

			return true;
		}
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("^[-//+]?[0-9]+[//.]?[0-9]*$");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static boolean writeContentToFile(String content, String filePath) {
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "utf-8"));
			out.write("\n" + content);
			out.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			return false;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

}
