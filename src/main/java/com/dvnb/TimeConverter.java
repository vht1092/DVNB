package com.dvnb;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(TimeConverter.class);

	/**
	 * Chuyen doi chuoi yyyyMMddHH24MISSSSS sang dinh dang dd/mm/yyyy hh24:mi:ss
	 * 
	 * @param time
	 *            mot chuoi yyyyMMddHH24MISSSSS
	 * @return String
	 */
	public String convertStrToDateTime(final String time) {
		if (time != null && !"".equals(time)) {
			final String sTime = time;
			try {
				final int iYear = Integer.parseInt(sTime.substring(0, 4));
				final int iMonth = Integer.parseInt(sTime.substring(4, 6));
				final int iDate = Integer.parseInt(sTime.substring(6, 8));
				final int iHours = Integer.parseInt(sTime.substring(8, 10));
				final int iMinutes = Integer.parseInt(sTime.substring(10, 12));
				final int iSecond = Integer.parseInt(sTime.substring(12, 14));
				int iSeconds = 0;
				if(iSecond > 59){
					iSeconds = 59;
				}else{
					iSeconds = iSecond;
				}
				return String.format("%02d/%02d/%d %02d:%02d:%02d", iDate, iMonth, iYear, iHours, iMinutes, iSeconds);
			} catch (IndexOutOfBoundsException ex) {
				LOGGER.error("method convertStrToDateTime with input string:" + time + " - Message: " + ex.getMessage());
				return "";
			}
		}

		return "";

	}

	/**
	 * Chuyen doi chuoi yyyyMMddHH24MISSSSS cua bang transaction detail sang
	 * dinh dang dd/mm/yyyy hh24:mi:ss
	 * 
	 * @param time
	 *            mot chuoi yyyyMMddHH24MISSSSS
	 * @return String
	 */
	public String convertStrToDtTranx(final String time) {
		if (time != null && !"".equals(time)) {
			final String sTime = time;
			try {
				final int iYear = Integer.parseInt(sTime.substring(0, 4));
				final int iMonth = Integer.parseInt(sTime.substring(4, 6));
				final int iDate = Integer.parseInt(sTime.substring(6, 8));
				final int iHours = Integer.parseInt(sTime.substring(8, 10));
				final int iMinutes = Integer.parseInt(sTime.substring(10, 12));
				final int iSecond = Integer.parseInt(sTime.substring(12, 14));
				int iSeconds = 0;
				if(iSecond > 59){
					iSeconds = 59;
				}else{
					iSeconds = iSecond;
				}
				return String.format("%02d/%02d/%d %02d:%02d:%02d", iDate, iMonth, iYear, iHours, iMinutes, iSeconds);
			} catch (IndexOutOfBoundsException ex) {
				LOGGER.error("method convertStrToDtTranx with input string:" + time + " - Message: " + ex.getMessage());
				return "";
			}
		}
		return "";
	}

	/**
	 * Thoi gian hien tai theo dinh dang yyyyMMddHH24MISSSSS (SSSSS: midnight
	 * seconds)
	 * 
	 * @return String
	 */
	public String getCurrentTime() {
		final Date date = new Date();
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		final int iHours = cal.get(Calendar.HOUR_OF_DAY);
		final int iYear = cal.get(Calendar.YEAR);
		final int iMonth = cal.get(Calendar.MONTH) + 1;
		final int iDate = cal.get(Calendar.DATE);
		final int iMinutes = cal.get(Calendar.MINUTE);
		final int iSecond = cal.get(Calendar.SECOND);
		final LocalTime now = LocalTime.of(iHours, iMinutes, iSecond);
		final int iSecondMidNight = now.toSecondOfDay();
//		return String.format("%d%02d%02d%02d%02d%d", iYear, iMonth, iDate, iHours, iMinutes, iSecondMidNight);
		return String.format("%d%02d%02d%02d%02d%02d", iYear, iMonth, iDate, iHours, iMinutes, iSecond);
	};

	/**
	 * Convert Date => yyyyMMddHH24MISSSSS (SSSSS: midnight seconds)
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	public String convertDateTimeToStr(final Date date) {
		if (date != null) {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			final int iHours = cal.get(Calendar.HOUR_OF_DAY);
			final int iYear = cal.get(Calendar.YEAR);
			final int iMonth = cal.get(Calendar.MONTH) + 1;
			final int iDate = cal.get(Calendar.DATE);
			final int iMinutes = cal.get(Calendar.MINUTE);
			final int iSecond = cal.get(Calendar.SECOND);
			final LocalTime now = LocalTime.of(iHours, iMinutes, iSecond);
			final int iSecondMidNight = now.toSecondOfDay();
			return String.format("%02d%02d%02d%02d%02d%02d", iYear, iMonth, iDate, iHours, iMinutes, iSecondMidNight);
		}
		return "";
	}

	/**
	 * Convert Date => <code>yyyyMMddHH24MI000000000</code> hoac
	 * <code>yyyyMMddHH24MI999999999</code> cho
	 * muc dich tim kiem. <br>
	 * ismax = true se return chuoi <code>yyyyMMddHH24MI999999999</code> nguoc
	 * lai <code>yyyyMMddHH24MI000000000</code>
	 * 
	 * @param date
	 *            Date
	 * @param ismax
	 *            boolean
	 * @return String
	 */
	public String convertDatetime(final Date date, final boolean ismax) {
		if (date != null) {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			final int iYyear = cal.get(Calendar.YEAR);
			// Calendar.MONTH = 0,1,2,3,4...11
			final int iMonth = cal.get(Calendar.MONTH) + 1;
			final int iDate = cal.get(Calendar.DATE);
			if (!ismax) {
				return String.format("%02d%02d%02d", iYyear, iMonth, iDate) + "000000000";
			} else {
				return String.format("%02d%02d%02d", iYyear, iMonth, iDate) + "999999999";
			}
		}
		return "";
	}
	
	public String convertDatetime(final Date date) {
		if (date != null) {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			final int iYyear = cal.get(Calendar.YEAR);
			// Calendar.MONTH = 0,1,2,3,4...11
			final int iMonth = cal.get(Calendar.MONTH) + 1;
			final int iDate = cal.get(Calendar.DATE);
			return String.format("%02d%02d%02d", iYyear, iMonth, iDate);
		}
		return "";
	}
	
	public String convertDatetimeSeperatebychar(final Date date,final String sign) {
		if (date != null) {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			final int iYyear = cal.get(Calendar.YEAR);
			// Calendar.MONTH = 0,1,2,3,4...11
			final int iMonth = cal.get(Calendar.MONTH) + 1;
			final int iDate = cal.get(Calendar.DATE);
			return String.format("%04d"+sign+"%02d"+sign+"%02d", iYyear, iMonth, iDate);
		}
		return "";
	}
}
