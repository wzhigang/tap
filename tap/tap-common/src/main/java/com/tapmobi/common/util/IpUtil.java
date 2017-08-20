package com.tapmobi.common.util;

import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * IP������. ����httpServletRequest�����ȡhttp������Դ�ص�ip�ַ���
 */
public class IpUtil {

	private static Log log = LogFactory.getLog(IpUtil.class);

	//-- �����������ڸ���ĳЩip���ɽ���������
	/** ƥ���ָ��ʽ��ip��ַ��ģʽ�ַ��� */
	private static Pattern pattern = Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");

	/**
	 * �ж��Ƿ����ip��ʽ, �����ϸ�ʽ�򷵻�null
	 * @param ipString  ԭʼip�ַ���
	 * @return ����'1.2.3.4'��ʽ��ip�ַ���; ���ԭʼip�ַ������Ǹø�ʽ, �򷵻�null
	 */
	private static String isDottedIp(String ipString) {
		if (ipString != null) {
			Matcher matcher = pattern.matcher(ipString);
			if (matcher.matches()) {
				return matcher.group();
			}
		}
		return null;
	}


	/**
	 * ��ȡ��������Ŀͻ���(�����һ������)�� ip�ַ���, ����'1.2.3.4'��ʽ
	 * @param request HttpServletRequest����
	 * @return request�е�����'1.2.3.4'��ʽ��ip�ַ���; ���ԭʼip�ַ������Ǹø�ʽ, �򷵻�null,����������е�requestͷ��Ϣ
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = isDottedIp(request.getHeader("Proxy-Client-IP"));
			if (ip != null) {
				return ip;
			}
		} else {
			if (ip.indexOf(",") != -1) {
				String[] s = ip.split(",");
				for (int i = 0; i < s.length; i++) {
					if (s[i] != null && !"".equals(s[i])
							&& !"unknown".equals(s[i])) {
						ip = isDottedIp(s[i].trim());
						if (ip != null) {
							return ip;
						}
					}
				}
			} else if (ip.indexOf(";") != -1) {
				String[] s = ip.split(";");
				for (int i = 0; i < s.length; i++) {
					if (s[i] != null && !"".equals(s[i])
							&& !"unknown".equals(s[i])) {
						ip = isDottedIp(s[i].trim());
						if (ip != null) {
							return ip;
						}
					}
				}
			} else {
				if ("unknown".equals(ip)) {
					ip = isDottedIp(ip.trim());
					if (ip != null) {
						return ip;
					}
				}
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = isDottedIp(request.getHeader("WL-Proxy-Client-IP"));
			if (ip != null) {
				return ip;
			}
		}
		if (ip != null) {
			if (ip.indexOf(' ') != -1) {
				ip = ip.substring(0, ip.indexOf(' '));
			}
			ip = isDottedIp(ip);
			if (ip != null) {
				return ip;
			}
		}
		if (ip == null) {
			if ((request.getHeader("X-Forwarded-For") != null && !"unknown"
					.equals(request.getHeader("X-Forwarded-For")))
					|| request.getHeader("Proxy-Client-IP") != null
					|| request.getHeader("WL-Proxy-Client-IP") != null) {
				// ִ�е�����, ipStringҪôΪnull, Ҫô���ǵ�ָ�����ָ�ʽ, ��Ҫ������е�requestͷ��Ϣ������
				Enumeration headers = request.getHeaderNames();
				while (headers.hasMoreElements()) {
					String headerName = headers.nextElement().toString();
					log.warn(headerName + " --> "+ request.getHeader(headerName));
				}
				log.warn("--------request.getRemoteAddr:"+ request.getRemoteAddr() + "--------");
			}
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
