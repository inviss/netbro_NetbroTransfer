package kr.co.d2net.netbro.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DateUtils {

	final static Logger logger = LoggerFactory.getLogger(DateUtils.class);

	public static String getToday(String _format) {
		Date date = new Date();
		SimpleDateFormat sdate = new SimpleDateFormat(_format);
		return sdate.format(date);
	}

	public static String getWantDay(int _day, String _format) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdate = new SimpleDateFormat(_format);
		calendar.add(java.util.Calendar.DAY_OF_MONTH, _day);
		return sdate.format(calendar.getTime());
	}

	public static Date getWantDay(int _day) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(java.util.Calendar.DAY_OF_MONTH, _day);
		return calendar.getTime();
	}

	public static String getTodayDateTime(String _format) {
		Date date = new Date();
		SimpleDateFormat sdate = new SimpleDateFormat(_format);
		String todayDateTime = sdate.format(date);
		String minute = todayDateTime.substring(10, 12);
		if(minute.compareTo("30") > 0)
			minute = "30";
		else
			minute = "00";
		todayDateTime = (new StringBuilder(String.valueOf(todayDateTime.substring(0, 10)))).append(minute).toString();
		return todayDateTime;
	}

	public static String getFmtDateString(Date _date, String _format) {
		if(_date == null) return "";
		SimpleDateFormat sdate = new SimpleDateFormat(_format);
		return sdate.format(_date);
	}

	public static String getFmtDateString(String _date, String _format) {
		Date date = toDate(_date);
		return getFmtDateString(date, _format);
	}

	public static Long getFmtDateLong(String _date, String _format) {
		SimpleDateFormat sdf = null;
		Date d;
		try {
			if(_format == null) {
				if(_date.indexOf("-") > -1) {
					sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
				} else if(_date.indexOf("/") > -1) {
					sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
				} else if(_date.indexOf(".") > -1) {
					sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
				}
			} else {
				sdf = new SimpleDateFormat(_format, Locale.KOREA);
			}
			
			d = sdf.parse(_date);
		} catch (ParseException e) {
			return Long.valueOf(_date);
		}
		return d.getTime();
	}

	public static String getFmtDateString(long _date, String _format) {
		Date date = new Date(_date);
		return getFmtDateString(date, _format);
	}

	public static String getDelimDateString(String _date, String _delim) {
		String unFmtDate = getUnFmtDateString(_date);
		StringBuffer buf = new StringBuffer();
		buf.append(unFmtDate.substring(0, 4));
		buf.append(_delim);
		buf.append(unFmtDate.substring(4, 6));
		buf.append(_delim);
		buf.append(unFmtDate.substring(6, 8));
		return buf.toString();
	}

	public static String getUnFmtDateString(String fmtDate) {
		boolean isCharater = false;
		boolean isCorrect = true;
		String strDate = "";
		String date = "";
		String result = "";

		if(fmtDate != null)
			strDate = fmtDate.trim();

		for(int inx = 0; inx < strDate.length(); inx++) {
			if(!Character.isLetter(strDate.charAt(inx)) && strDate.charAt(inx) != ' ')
				continue;
			isCorrect = false;
			break;
		}

		if(!isCorrect)
			return "";

		if(strDate.length() != 8) {
			if(strDate.length() != 6 && strDate.length() != 10)
				return "";
			if(strDate.length() == 6) {
				if(Integer.parseInt(strDate.substring(0, 2)) > 50)
					date = "19";
				else
					date = "20";
				result = (new StringBuilder(String.valueOf(date))).append(strDate).toString();
			}
			if(strDate.length() == 10)
				result = (new StringBuilder(String.valueOf(strDate.substring(0, 4)))).append(strDate.substring(4, 8)).append(strDate.substring(8, 10)).toString();
		} else {

			try {
				Integer.parseInt(strDate);
			} catch(NumberFormatException ne) {
				isCharater = true;
			}

			if(isCharater) {
				date = (new StringBuilder(String.valueOf(strDate.substring(0, 2)))).append(strDate.substring(3, 5)).append(strDate.substring(6, 8)).toString();
				if(Integer.parseInt(strDate.substring(0, 2)) > 50)
					result = (new StringBuilder("19")).append(date).toString();
				else
					result = (new StringBuilder("20")).append(date).toString();
			} else {
				return strDate;
			}
		}
		return result;
	}

	public static Date getRelativeDate(Date _date, int _year, int _month, int _day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(_date);
		cal.add(1, _year);
		cal.add(2, _month);
		cal.add(5, _day);
		return cal.getTime();
	}

	public static String getRelativeDateString(Date _date, int _year, int _month, int _day, String _format) {
		Date relativeDate = getRelativeDate(_date, _year, _month, _day);
		return getFmtDateString(relativeDate, _format);
	}

	public static Date getRelativeDate(Date _date, int _year, int _month, int _day, int _hour, int _minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(_date);
		cal.add(1, _year);
		cal.add(2, _month);
		cal.add(5, _day);
		cal.add(11, _hour);
		cal.add(12, _minute);
		return cal.getTime();
	}

	public static String getRelativeDateString(Date _date, int _year, int _month, int _day, int _hour, int _minute, String _format) {
		Date relativeDate = getRelativeDate(_date, _year, _month, _day, _hour, _minute);
		return getFmtDateString(relativeDate, _format);
	}

	public static String getRelativeDateString(String _date, int _year, int _month, int _day, int _hour, int _minute, String _format) {
		Calendar cal = toCalendar(_date.substring(0, 8), Integer.parseInt(_date.substring(8, 10)), Integer.parseInt(_date.substring(10, 12)));
		cal.add(1, _year);
		cal.add(2, _month);
		cal.add(5, _day);
		cal.add(11, _hour);
		cal.add(12, _minute);
		SimpleDateFormat dateFormat = new SimpleDateFormat(_format);
		return dateFormat.format(cal.getTime());
	}

	public static String getNextDate(String _date, int _days, String _format) {
		if(_days < 0) {
			return _date;
		} else {
			Calendar cal = toCalendar(_date);
			cal.add(5, _days);
			SimpleDateFormat dateFormat = new SimpleDateFormat(_format);
			return dateFormat.format(cal.getTime());
		}
	}

	public static String getPrevDate(String _date, int _days, String _format) {
		if(_days < 0) {
			return _date;
		} else {
			Calendar cal = toCalendar(_date);
			cal.add(5, -_days);
			SimpleDateFormat dateFormat = new SimpleDateFormat(_format);
			return dateFormat.format(cal.getTime());
		}
	}

	public static String getNextWeekDate(String _date, int _weeks, String _format) {
		if(_weeks < 0) {
			return _date;
		} else {
			Calendar cal = toCalendar(_date);
			cal.add(5, _weeks * 7);
			SimpleDateFormat dateFormat = new SimpleDateFormat(_format);
			return dateFormat.format(cal.getTime());
		}
	}

	public static String getPrevWeekDate(String _date, int _weeks, String _format) {
		if(_weeks < 0) {
			return _date;
		} else {
			Calendar cal = toCalendar(_date);
			cal.add(5, _weeks * -7);
			SimpleDateFormat dateFormat = new SimpleDateFormat(_format);
			return dateFormat.format(cal.getTime());
		}
	}

	public static String getNextMonthDate(String _date, int _months, String _format) {
		if(_months < 0) {
			return _date;
		} else {
			Calendar cal = toCalendar(_date);
			cal.add(2, _months);
			SimpleDateFormat dateFormat = new SimpleDateFormat(_format);
			return dateFormat.format(cal.getTime());
		}
	}

	public static String getPrevMonthDate(String _date, int _months, String _format) {
		if(_months < 0) {
			return _date;
		} else {
			Calendar cal = toCalendar(_date);
			cal.add(2, -_months);
			SimpleDateFormat dateFormat = new SimpleDateFormat(_format);
			return dateFormat.format(cal.getTime());
		}
	}

	public static Calendar toCalendar(String fmtDate) {
		String date = getUnFmtDateString(fmtDate);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1, Integer.parseInt(date.substring(6, 8)));
		return calendar;
	}

	public static Calendar toCalendar(String fmtDate, int hour, int minute) {
		String date = getUnFmtDateString(fmtDate);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1, Integer.parseInt(date.substring(6, 8)), hour, minute);
		return calendar;
	}

	public static Date toDate(String fmtDate) {
		return toCalendar(fmtDate).getTime();
	}

	public static String getWeekDay(String _date, int _order) {
		String returnDay = null;
		Calendar curr = Calendar.getInstance();
		curr.set(Integer.parseInt(_date.substring(0, 4)), Integer.parseInt(_date.substring(4, 6)) - 1, Integer.parseInt(_date.substring(6, 8)));

		int weekday = curr.get(7);
		if(_order == weekday) {
			returnDay = _date;
		} else {
			curr.add(5, _order - weekday);
			SimpleDateFormat sdate = new SimpleDateFormat("yyyyMMdd");
			returnDay = sdate.format(curr.getTime());
		}
		return returnDay;
	}

	public static int getFirstDay(int _year, int _month) {
		int firstday = 0;
		Calendar curr = Calendar.getInstance();
		curr.set(_year, _month - 1, 1);
		firstday = curr.get(7);
		return firstday;
	}

	public static int getLastDate(int _year, int _month) {
		int yy = _year;
		int mm = _month;

		switch(mm) {
		case 1: // '\001'
		case 3: // '\003'
		case 5: // '\005'
		case 7: // '\007'
		case 8: // '\b'
		case 10: // '\n'
		case 12: // '\f'
			return 31;
		case 4: // '\004'
		case 6: // '\006'
		case 9: // '\t'
		case 11: // '\013'
			return 30;
		}
		return (yy % 4 != 0 || yy % 100 == 0) && yy % 400 != 0 ? 28 : 29;
	}

	public static int getWeekDayCount(String _date) {
		Calendar curr = Calendar.getInstance();
		curr.set(Integer.parseInt(_date.substring(0, 4)), Integer.parseInt(_date.substring(4, 6)) - 1, Integer.parseInt(_date.substring(6, 8)));
		int weekday = curr.get(7);
		return weekday;
	}

	public static int getWeekCountMonth(int _day) {
		int remainCount = _day - (_day / 7) * 7;
		int weekCount;
		if(remainCount > 0)
			weekCount = _day / 7 + 1;
		else
			weekCount = _day / 7;
		return weekCount;
	}

	public static int getWeekCount(int _year, int _month) {
		Calendar curr = Calendar.getInstance();
		curr.set(_year, _month - 1, getLastDate(_year, _month));
		int weeks = curr.get(4);
		return weeks;
	}

	public static int getWeekCountMonth(int _year, int _month, int _day) {
		Calendar curr = Calendar.getInstance();
		curr.set(_year, _month - 1, _day);
		int weeks = curr.get(4);
		return weeks;
	}

	public static String calcDate(String _date, int _val, String _format) {
		Calendar curr = Calendar.getInstance();
		_date = unFmtDate(_date);
		curr.set(Integer.parseInt(_date.substring(0, 4)), Integer.parseInt(_date.substring(4, 6)) - 1, Integer.parseInt(_date.substring(6, 8)));
		curr.add(5, _val);
		SimpleDateFormat sdate = new SimpleDateFormat(_format);
		return sdate.format(curr.getTime());
	}

	public static String calcDateTime(String _dateTime, int _val, String _format) {
		Calendar curr = Calendar.getInstance();
		_dateTime = unFmtDate(_dateTime);
		curr.set(Integer.parseInt(_dateTime.substring(0, 4)), Integer.parseInt(_dateTime.substring(4, 6)) - 1, Integer.parseInt(_dateTime.substring(6, 8)), Integer.parseInt(_dateTime.substring(8, 10)), Integer.parseInt(_dateTime.substring(10, 12)));
		curr.add(12, _val);
		SimpleDateFormat sdate = new SimpleDateFormat(_format);
		return sdate.format(curr.getTime());
	}

	public static String unFmtDate(String _fmtdate) {
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < _fmtdate.length(); i++)
			if(_fmtdate.charAt(i) != '-')
				buf.append(_fmtdate.charAt(i));
		return buf.toString();
	}

	public static String[] getDatesInWeek(String _firstday, String _format) {
		String weekdays[] = new String[7];
		for(int i = 0; i < 7; i++)
			weekdays[i] = calcDate(_firstday, i, _format);
		return weekdays;
	}

	public static String[] getDatesInPeriod(String _startDate, String _endDate, String _format) {
		int dateDiffCount = getTwoDatesDifference(_startDate, _endDate);
		String days[] = new String[dateDiffCount + 1];
		for(int i = 0; i < dateDiffCount + 1; i++)
			days[i] = calcDate(_startDate, i, _format);
		return days;
	}

	public static String[] getDateTimesInPeriod(String _startDateTime, String _endDateTime, String _format) {
		int dateTimeDiffCount = getTwoDateTimesDifference(_startDateTime, _endDateTime);
		String dateTimes[] = new String[dateTimeDiffCount + 1];
		for(int i = 0; i < dateTimes.length; i++)
			dateTimes[i] = calcDateTime(_startDateTime, i * 30, _format);
		return dateTimes;
	}

	public static int getTwoDatesDifference(String strDate, String strComp) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();

		int year = Integer.parseInt(strDate.substring(0, 4));
		int month = Integer.parseInt(strDate.substring(4, 6));
		int day = Integer.parseInt(strDate.substring(6, 8));

		int compYear = Integer.parseInt(strComp.substring(0, 4));
		int compMonth = Integer.parseInt(strComp.substring(4, 6));
		int compDay = Integer.parseInt(strComp.substring(6, 8));

		cal1.set(year, month - 1, day);
		cal2.set(compYear, compMonth - 1, compDay);

		long cal1sec = cal1.getTime().getTime();
		long cal2sec = cal2.getTime().getTime();

		long gap = cal2sec - cal1sec;

		int gapday = Integer.parseInt(String.valueOf(gap / 86400L / 1000L));

		return gapday;
	}

	public static int getTwoDateTimesDifference(String strDateTime, String strCompTime) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();

		int year = Integer.parseInt(strDateTime.substring(0, 4));
		int month = Integer.parseInt(strDateTime.substring(4, 6));
		int day = Integer.parseInt(strDateTime.substring(6, 8));
		int hour = Integer.parseInt(strDateTime.substring(8, 10));
		int minute = Integer.parseInt(strDateTime.substring(10, 12));

		int compYear = Integer.parseInt(strCompTime.substring(0, 4));
		int compMonth = Integer.parseInt(strCompTime.substring(4, 6));
		int compDay = Integer.parseInt(strCompTime.substring(6, 8));
		int compHour = Integer.parseInt(strCompTime.substring(8, 10));
		int compMinute = Integer.parseInt(strCompTime.substring(10, 12));

		cal1.set(year, month - 1, day, hour, minute);
		cal2.set(compYear, compMonth - 1, compDay, compHour, compMinute);

		long cal1sec = cal1.getTime().getTime();
		long cal2sec = cal2.getTime().getTime();
		long gap = cal2sec - cal1sec;

		int gapDateTime = Integer.parseInt(String.valueOf(gap / 1800L / 1000L));

		return gapDateTime;
	}

	public static String getValidDate(String yyyymmdd) {
		String strYear = "";
		String strMonth = "";
		String strDay = "";
		int validMonthDay = 0;
		String validDate = "";

		if(yyyymmdd.length() == 8) {
			strYear = yyyymmdd.substring(0, 4);
			strMonth = yyyymmdd.substring(4, 6);
			strDay = yyyymmdd.substring(6, 8);
			validMonthDay = getLastDate(Integer.parseInt(strYear), Integer.parseInt(strMonth));
			if(Integer.parseInt(strDay) <= validMonthDay)
				validDate = yyyymmdd;
			else
				validDate = (new StringBuilder(String.valueOf(strYear))).append(strMonth).append(String.valueOf(validMonthDay)).toString();
			return validDate;
		} else {
			return "";
		}
	}

}