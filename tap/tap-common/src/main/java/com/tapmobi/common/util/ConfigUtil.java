package com.tapmobi.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigUtil {
	private static Log log = LogFactory.getLog(ConfigUtil.class);
	//private static ResourceBundle bundle;
	private static PropertyResourceBundle bundle;
	/** 属性文件绝对路径 */
	private static String propFilePath = "config.properties";
	/** 属性文件的最近更新时间 */
	private static long lastModified = System.currentTimeMillis();
	/** 属性文件变动的监视线程 */
	private static PropertyMonitor monitor = null;
	
	protected static ClassLoader loader = Thread.currentThread().getContextClassLoader();

	// 初始化时, 定位config.properties文件, 启动属性文件监视线程
	static {
		FileInputStream fis = null;
		try {
			log.info("config loading...");
	
			File configFile = new File(loader.getResource(propFilePath).getFile());

			// 加载属性文件
			fis = new FileInputStream(configFile);
			bundle = new PropertyResourceBundle(fis);
			// 启动属性文件监视线程
			//monitor = new ConfigUtil().new PropertyMonitor();
			//monitor.start();
			log.info("config file " + configFile.getAbsolutePath() +  " loading complete");
		} catch (Exception e) {
			log.error("load PropertyResourceBundle failed:", e);
		}finally{
			if(null != fis){
				try {
					fis.close();
				} catch (IOException e) {
					log.error("fis close failed:", e);
				}
			}
		}
	}
	
	//-----------------------------------------------

	/**
	 * 查看属性文件是否更新的监视线程, 每隔10秒检查一次该文件的变化, 若有变化则重新加载
	 */
	public class PropertyMonitor extends Thread {
		private long checkInterval = 10000L; // 检查时间间隔: 10秒

		public void run() {
			log.info("Property Monitor started");
			while (true) {
				try {
					sleep(checkInterval);
				} catch (InterruptedException e) {
					log.error("InterruptedException encountered, exit.");
					return;
				}
				try {
					File file = new File(loader.getResource(propFilePath).getFile());
					if (file.lastModified() > lastModified) {
						lastModified = file.lastModified(); // 记录最新修改时间 
						reloadConfig();
					}
				} catch (Exception e) {
					log.error("failed to reload the properties file."
							+ e.getMessage() + ", continue to monitor.");
				}
			}

		}
	}

	//-----------------------------------------------

	/**
	 * 重新加载配置文件config.properties
	 * @throws Exception 加载配置文件时抛出的异常
	 */
	public static void reloadConfig() throws Exception {
		FileInputStream fis = null;
		try {
			File configFile = new File(loader.getResource(propFilePath).getFile());
			fis = new FileInputStream(configFile);
			bundle = new PropertyResourceBundle(fis);
			log.warn("the property file " + configFile.getAbsolutePath() +" changed.");
		} catch (Exception e) {
			throw e;
		}finally{
			if(null != fis){
				fis.close();
			}
		}
	}

	public static String getString(String key) {
		try {
			if(null == bundle){
				 reloadConfig();
			}
			String result = bundle.getString(key);
			return result;
		} catch (Exception mre) {
			log.error("getString => key="+key+",bundle="+bundle, mre);
			return "";
		}
	}

	public static String getString(String key, String defaultValue) {
		String rtn = getString(key);
		if (rtn == null || rtn.trim().length() == 0) {
			return defaultValue;
		}
		
		return rtn;
	}
	
	/**
	 * 获取key对应的整型值
	 * @param key
	 * @return
	 */
	public static Integer getInt(String key) {
		//读取
		String valueStr = getString(key);
		if (valueStr == null || valueStr.trim().length() == 0) {
			return null;
		}
		
		Integer rtn = null;
		
		try {
			rtn = Integer.parseInt(valueStr.trim());
		} catch (Throwable ex) {
			log.error("getInt => key="+key+",bundle="+bundle, ex);
			rtn = null;
		}
	
		return rtn;
	}
	
	/**
	 * 获取key对应的整型值,无则返回默认值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Integer getInt(String key, Integer defaultValue) {
		Integer rtn = getInt(key);
	
		return rtn != null ? rtn : defaultValue;
	}
	
	/**
	 * 获取key对应的浮点值
	 * @param key
	 * @return value or null
	 */
	public static Double getDouble(String key) {
		String valueStr = getString(key);
		if (valueStr == null || valueStr.trim().length() == 0) {
			return null;
		}
		
		Double rtn = null;
		
		try {
			rtn = Double.parseDouble(valueStr.trim());
		} catch (Throwable ex) {
			log.error("getDouble => key="+key+",bundle="+bundle, ex);
			rtn = null;
		}
	
		return rtn;
	}
	
	/**
	 * 获取key对应的浮点值,无则返回默认值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Double getDouble(String key, Double defaultValue) {
		Double rtn = getDouble(key);
	
		return rtn != null ? rtn : defaultValue;
	}
	
	public static void main(String[] args) {
		String s = null;
		System.out.println(s+"abc");
		
	}
}
