package tools.convert;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tools.common.StringHelper;


public class StringListConverter {

	public static final String SEPARATOR = ",";

	public static String convertToString(List<String> list) {
		if (list == null)
			return null;
		if (list.isEmpty())
			return "";
		return StringHelper.join(list, SEPARATOR);
	}

	public static List<String> convertToList(String string) {
		if (string == null)
			return null;
		List<String> list = new ArrayList<>();
		if (StringHelper.isNotBlank(string))
			list.addAll(Arrays.asList(string.split(SEPARATOR)));
		return list;
	}

}