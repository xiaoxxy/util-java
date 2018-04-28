package tools.common;

import java.sql.Timestamp;
import java.util.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class DateHelper {

	private static final java.util.Map<String, String> FORMAT_MAP = new java.util.concurrent.ConcurrentHashMap<String, String>();

	static {

		FORMAT_MAP.put("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d+", "yyyy-MM-dd'T'HH:mm:ss.SSS");
		FORMAT_MAP.put("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}", "yyyy-MM-dd'T'HH:mm:ss");

		FORMAT_MAP.put("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}.\\d+", "yyyy-MM-dd HH:mm:ss.SSS");
		FORMAT_MAP.put("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}", "yyyy-MM-dd HH:mm:ss");

		FORMAT_MAP.put("\\d{4}\\d{2}\\d{2}\\d{2}\\d{2}\\d{2}", "yyyyMMddHHmmss");

		FORMAT_MAP.put("\\d{4}-\\d{2}-\\d{2}", "yyyy-MM-dd");

		FORMAT_MAP.put("\\d{4}\\d{2}\\d{2}", "yyyyMMdd");

	}

	public static Date parse(String dateString) throws ParseException {

		if (dateString.matches("\\d{13}")) {
			return new Date(Long.parseLong(dateString));
		}

		for (Entry<String, String> entry : FORMAT_MAP.entrySet()) {
			if (Pattern.matches(entry.getKey(), dateString)) {
				return new SimpleDateFormat(entry.getValue()).parse(dateString);
			}
		}
		return null;
	}

	public static Date parse(String dateString, String pattern) throws ParseException {

		return new SimpleDateFormat(pattern).parse(dateString);
	}

	public static String format(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}

	public static Date addYears(final Date date, final int amount) {
		return add(date, Calendar.YEAR, amount);
	}

	public static Date addMonths(final Date date, final int amount) {
		return add(date, Calendar.MONTH, amount);
	}

	public static Date addWeeks(final Date date, final int amount) {
		return add(date, Calendar.WEEK_OF_YEAR, amount);
	}

	public static Date addDays(final Date date, final int amount) {
		return add(date, Calendar.DAY_OF_MONTH, amount);
	}

	public static Date addHours(final Date date, final int amount) {
		return add(date, Calendar.HOUR_OF_DAY, amount);
	}

	public static Date addMinutes(final Date date, final int amount) {
		return add(date, Calendar.MINUTE, amount);
	}

	public static Date addSeconds(final Date date, final int amount) {
		return add(date, Calendar.SECOND, amount);
	}

	public static Date addMilliseconds(final Date date, final int amount) {
		return add(date, Calendar.MILLISECOND, amount);
	}

	private static Date add(final Date date, final int calendarField, final int amount) {
		if (date == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		final Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	public static long currentTimestamp() {
		long now = System.currentTimeMillis();
		return now;
	}

	public static long dateStrToTimestamp(String dateStr) {
		Timestamp ts = new Timestamp(0);
		try {
			ts = Timestamp.valueOf(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ts.getTime();
	}

	public static int todayLeftSeconds() {
		long now = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long end = cal.getTimeInMillis();
		return (int) (end - now)/1000;
	}
	
	 /**
     * 获得yymmdd类型的日期
     * @param date
     * @return
     */
    @SuppressWarnings("deprecation")
	public static String getDateString(Date date){
        String year =(date.getYear()+1900)+"";
        String mm = (date.getMonth()+1)+"";
        if(Integer.valueOf(mm).intValue()<10){
            mm="0"+mm;
        }
        String day = date.getDate()+"";
        if(Integer.valueOf(day).intValue()<10)day="0"+day;
        return year+mm+day;
    }

}