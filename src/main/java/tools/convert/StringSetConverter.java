package tools.convert;

import tools.common.StringHelper;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;


public class StringSetConverter{

	public static final String SEPARATOR = ",";

	public static String convertToString(Set<String> set) {
		if (set == null)
			return null;
		if (set.isEmpty())
			return "";
		return StringHelper.join(set, SEPARATOR);
	}

	public static Set<String> convertToSet(String string) {
		if (string == null)
			return null;
		Set<String> set = new LinkedHashSet<>();
		if (StringHelper.isNotBlank(string))
			set.addAll(Arrays.asList(string.split(SEPARATOR)));
		return set;
	}

}