package kr.co.d2net.netbro.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Utility {

	final static Logger logger = LoggerFactory.getLogger(Utility.class);

	/**
	 * 한글 여부 체크
	 * @param c
	 * @return
	 */
	public static boolean isHangul(char c) {
		return isHangulSyllables(c) || isHangulJamo(c)
				|| isHangulCompatibilityJamo(c);
	}

	// 완성된 한글인가? 참조: http://www.unicode.org/charts/PDF/UAC00.pdf
	public static boolean isHangulSyllables(char c) {
		return (c >= (char) 0xAC00 && c <= (char) 0xD7A3);
	}

	// (현대 및 고어) 한글 자모? 참조: http://www.unicode.org/charts/PDF/U1100.pdf
	public static boolean isHangulJamo(char c) {
		return (c >= (char) 0x1100 && c <= (char) 0x1159)
				|| (c >= (char) 0x1161 && c <= (char) 0x11A2)
				|| (c >= (char) 0x11A8 && c <= (char) 0x11F9);
	}

	// (현대 및 고어) 한글 자모? 참조: http://www.unicode.org/charts/PDF/U3130.pdf
	public static boolean isHangulCompatibilityJamo(char c) {
		return (c >= (char) 0x3131 && c <= (char) 0x318E);
	}

	// 현재일을 기준으로 파라미터 일수를 더한다.
	public static Date getWantDay(int _day, DStatus status) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(java.util.Calendar.DAY_OF_MONTH, _day);

		switch(status) {
		case FROM:
			calendar.set(Calendar.HOUR_OF_DAY, 00);
			calendar.set(Calendar.MINUTE, 00);
			calendar.set(Calendar.SECOND, 01);
			break;
		case TO:
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			break;
		default :
			break;
		}

		return calendar.getTime();
	}

	public static Date getWantMonth(int _month, DStatus status) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(java.util.Calendar.MONTH, _month);

		switch(status) {
		case FROM:
			calendar.set(Calendar.HOUR_OF_DAY, 00);
			calendar.set(Calendar.MINUTE, 00);
			calendar.set(Calendar.SECOND, 01);
			break;
		case TO:
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			break;
		default :
			break;
		}

		return calendar.getTime();
	}

	// 현재일을 기준으로 파라미터 일수를 더한다.
	public static String getWantDay(int _day, String _format, DStatus status) {
		SimpleDateFormat sdate = new SimpleDateFormat(_format);
		return sdate.format(getWantDay(_day, status));
	}

	public static String getOs() {
		return SystemUtils.IS_OS_LINUX ? "linux" : SystemUtils.IS_OS_MAC ? "mac" : "win";
	}

	public static String[] videoInfo(File videoFile) {
		BufferedReader error = null;
		String[] info = new String[4];
		try {
			Bundle bundle = FrameworkUtil.getBundle(Utility.class);
			URL url = null;
			if(getOs().equals("win"))
				url = FileLocator.find(bundle, new Path("ffmpeg/win/ffmpeg.exe"), null);
			else
				url = FileLocator.find(bundle, new Path("ffmpeg/"+getOs()+"/ffmpeg"), null);

			URL fileUrl = FileLocator.toFileURL(url);
			File ffFile = new File(fileUrl.getPath());

			String[] commands = null;
			if(SystemUtils.IS_OS_WINDOWS) {
				String execPath = ffFile.getAbsolutePath().replaceAll("/", "\\\\");
				commands = new String[]{ "cmd", "/c", execPath, "-i", videoFile.getAbsolutePath() };
			} else {
				commands = new String[]{ ffFile.getAbsolutePath(), "-i", videoFile.getAbsolutePath() };
			}
			Process processor = Runtime.getRuntime().exec(commands);

			String line = null;
			error = new BufferedReader(new InputStreamReader(processor.getErrorStream(), "EUC-KR"));
			while ((line = error.readLine()) != null) {
				if (StringUtils.isNotEmpty(line)) {
					String tmp = line.trim();
					
					String[] msg = null;
					if (tmp.startsWith("Stream #0:0") && tmp.indexOf("Video:") > -1) {
						msg = tmp.split(",");
						
						for(String t : msg) {
							if(t.indexOf("DAR") > -1 && t.indexOf("[") > -1 && t.indexOf("]") > -1) {
								// 해상도 720x512
								info[0] = t.trim().split(" ")[0];
								
								// 화면비 4:3
								info[1] = t.substring(t.indexOf("DAR")+4, t.indexOf("]")).trim();
							} else if(t.indexOf("kb/s") > -1) {
								// bitrate
								String[] v = t.trim().split(" ");
								info[2] = (v.length == 3) ? v[1].trim() : v[0].trim();
							} else if(t.indexOf("fps") > -1) {
								// fps
								info[3] = t.trim().split(" ")[0].trim();
							}
						}
					} else if (tmp.startsWith("Stream #0:1") && tmp.indexOf("Video:") > -1) {
						msg = tmp.split(",");
						
						for(String t : msg) {
							if(t.indexOf("DAR") > -1 && t.indexOf("[") > -1 && t.indexOf("]") > -1) {
								// 해상도 720x512
								info[0] = t.trim().split(" ")[0];
								
								// 화면비 4:3
								info[1] = t.substring(t.indexOf("DAR")+4, t.indexOf("]")).trim();
							} else if(t.indexOf("kb/s") > -1) {
								// bitrate
								String[] v = t.trim().split(" ");
								info[2] = (v.length == 3) ? v[1].trim() : v[0].trim();
							} else if(t.indexOf("fps") > -1) {
								// fps
								info[3] = t.trim().split(" ")[0].trim();
							}
						}
					} else if (tmp.indexOf("bitrate:") > -1) {
						if(StringUtils.isBlank(info[2])) {
							info[2] = tmp.substring(tmp.indexOf("bitrate:")+8, tmp.indexOf("kb/s")).trim();
						}
					} else continue;
				}
			}
			error.close(); // close를 하지 않으면 file lock 상태로 남아있게 됨
			processor.waitFor();
		} catch(Exception e) {
			logger.error("video info get error", e);
		} finally {
			if(error != null) {
				try {
					error.close();
				} catch (IOException e) {}
			}
		}
		return info;
	}

	public static long getDuration(File videoFile) {
		long duration = 0L;

		BufferedReader error = null;
		try {

			Bundle bundle = FrameworkUtil.getBundle(Utility.class);
			URL url = null;
			if(getOs().equals("win"))
				url = FileLocator.find(bundle, new Path("ffmpeg/win/ffmpeg.exe"), null);
			else
				url = FileLocator.find(bundle, new Path("ffmpeg/"+getOs()+"/ffmpeg"), null);

			URL fileUrl = FileLocator.toFileURL(url);
			File file = new File(fileUrl.getPath());
			String videoFilePath = videoFile.getAbsolutePath();
			String[] commands = null;

			String execPath = file.getAbsolutePath();

			if(SystemUtils.IS_OS_WINDOWS) {
				execPath = execPath.replaceAll("/", "\\\\");
				commands = new String[]{ "cmd", "/c", execPath, "-i", videoFilePath };
			} else {
				commands = new String[]{ execPath, "-i", videoFilePath };
			}

			Process processor = Runtime.getRuntime().exec(commands);
			String line = null;
			error = new BufferedReader(new InputStreamReader(processor.getErrorStream(), "EUC-KR"));
			while ((line = error.readLine()) != null) { // file lock
				if (StringUtils.isNotEmpty(line)) {
					String tmp = line.trim();
					if (tmp.startsWith("Duration:")) {
						String temp = tmp.substring("Duration: ".length(), tmp.indexOf(","));
						if (temp != null && temp.length() == 11) {
							long time = (Integer.parseInt(temp.substring(0, 2)) * 60 * 60 * 30) 
									+ (Integer.parseInt(temp.substring(3, 5)) * 60 * 30)  
									+ (Integer.parseInt(temp.substring(6, 8)) * 30)
									+ Math.round(Double.parseDouble(temp.substring(9, 11)) * 0.3333);
							duration = time;

							error.close(); // close를 하지 않으면 file lock 상태로 남아있게 됨
							break;
						}
					}
				}
			}

			processor.waitFor();
		} catch (Exception e) {
			logger.error("getDuration error", e);
		} finally {
			if(error != null) {
				try {
					error.close();
				} catch (IOException e) {}
			}
		}

		return duration;
	}

	/**
	 * duration = 타임코드로
	 * @param param frame
	 * @return  String
	 * @throws DataAccessException
	 */
	public static String changeDuration(long frame) {

		long hh, mm, m10, m1, ss, ff;  // 시, 분, 중간값, 중간값, 중간값, 중간값

		hh = frame / 107892; // (30000/1001)*3600
		frame = frame % 107892;
		m10 = frame / 17982; // (30000/1001)*600
		frame = frame % 17982;

		if ( frame < 1800 ) {
			m1 = 0;
			ss = frame / 30;
			ff = frame % 30;
		} else {      // except for each m10, plus 2 frame per m1

			frame = frame - 1800;  // in case of m10
			m1 = frame / 1798 + 1;
			frame = frame % 1798;

			if ( frame < 28 ) { // in case of m1
				ss = 0;
				ff = frame + 2;
			} else {    // in case of none of m10 and m1
				frame = frame - 28;
				ss = frame / 30 + 1;
				ff = frame % 30;
			}
		}
		mm = m10 * 10 + m1;

		return padLeft(String.valueOf(hh), "0", 2)+":"+padLeft(String.valueOf(mm), "0", 2)+":"+padLeft(String.valueOf(ss), "0", 2)+":"+padLeft(String.valueOf(ff), "0", 2);
	}

	public static String time2str(long time) throws Exception {
		double sec = time / 30.000;
		int dec = (int)sec;
		double fsec = sec - (int)sec;

		if (logger.isDebugEnabled()) {
			logger.debug("@@ time:" + time);
			logger.debug("@@ sec:" + sec);
			logger.debug("@@ dec:" + dec);
			logger.debug("@@ fsec:" + fsec);
		}

		String strTime = StringUtils.leftPad(String.valueOf(dec / 3600), 2, "0") + ":"
				+ StringUtils.leftPad(String.valueOf(dec % 3600 / 60), 2, "0") + ":"
				+ StringUtils.leftPad(String.valueOf(dec % 3600 % 60), 2, "0") + ":"
				+ StringUtils.leftPad(String.valueOf(Math.round(fsec * 30)), 2, "0");

		if (logger.isDebugEnabled())
			logger.debug("@@ strTime:" + strTime);

		return strTime;
	}

	/**
	 * 값 채우기
	 * 
	 * @param strTarget
	 *            대상 문자열
	 * @param strDest
	 *            채워질 문자열
	 * @param nSize
	 *            총 문자열 길이
	 * @param bLeft
	 *            채워질 문자의 방향이 좌측인지 여부
	 * @return 지정한 길이만큼 채워진 문자열
	 */
	public static String padValue(final String strTarget, final String strDest, final int nSize, final boolean bLeft) {
		if (strTarget == null) {
			return strTarget;
		}

		String retValue = null;
		StringBuffer objSB = new StringBuffer();
		int nLen = strTarget.length();
		int nDiffLen = nSize - nLen;

		for (int i = 0; i < nDiffLen; i++) {
			objSB.append(strDest);
		}

		if (bLeft) { // 채워질 문자열의 방향이 좌측일 경우
			retValue = objSB.toString() + strTarget;
		}
		else { // 채워질 문자열의 방향이 우측일 경우
			retValue = strTarget + objSB.toString();
		}

		return retValue;
	}

	/**
	 * 좌측으로 값 채우기
	 * 
	 * @param strTarget
	 *            대상 문자열
	 * @param strDest
	 *            채워질 문자열
	 * @param nSize
	 *            총 문자열 길이
	 * @return 채워진 문자열
	 */
	public static String padLeft(final String strTarget, final String strDest, final int nSize) {
		return padValue(strTarget, strDest, nSize, true);
	}

	/**
	 * 기존 파일을 신규파일명으로 변경한다.
	 * @param orgFilePath
	 * @param newFilePath
	 * @throws Exception
	 */
	public static void fileRename(String orgFilePath, String newFilePath) throws Exception {
		try{
			File f1 = new File(orgFilePath);
			if(!f1.isFile()) {
				throw new Exception("변경할 파일명이 존재하지 않습니다. - "+f1.getAbsolutePath());
			}
			boolean moved = f1.renameTo(new File(newFilePath));
			if(logger.isInfoEnabled()) {
				logger.info("file moved: "+moved+", path: "+newFilePath);
			}
		}catch(Exception e){
			if(e instanceof FileNotFoundException){
				throw e;
			}else{
				throw new Exception("File Rename Error", e);
			}
		}
	}
	
	public static String encodeMD5(String msg) {
		String tmp = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(msg.getBytes());

			byte byteData[] = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			tmp = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			logger.error("encodeMD5",e);
		}
		return tmp;
	}


}
