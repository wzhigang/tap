package com.tapmobi.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 用于写入日志文件
 * 
 * @author gaoyun
 * 
 */
public class LogUtil{

	/** Map<accountId,Map<date,FileWriter>> **/
	private static Map<String, Map<String, FileWriter>> map = new HashMap<String, Map<String, FileWriter>>();

	/** 可重入的互斥锁 **/
	private static final Lock lock = new ReentrantLock();
	
	private static String path =  ConfigUtil.getString("log.filepath");
//	private static String path =  "E:\\";
	
	private static Log log = LogFactory.getLog(LogUtil.class);

	
	/**
	 * 
	 * @param fileName
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	private static FileWriter fileWriterManage(String fileName, String  pattern) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat (pattern);
		String date = formatter.format(new Date());
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
		File file = new File(path + fileName +  "." + date);
		FileWriter fileWriter;
		Map<String, FileWriter> fm = map.get(fileName); // 通过文件名称得到文件指针
		if (null != fm) {
			// 如果map中存放的文件指针是当前时间(小时)的则返回该文件的指针，否则，关闭当前小时之前的文件流，重新创建新的文件指针
			if (fm.containsKey(date)) {
				return (FileWriter) fm.get(date);
			} else {
				Iterator<?> iter = fm.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					fileWriter = (FileWriter) entry.getValue();
					if (null != fileWriter) {
						try {
							fileWriter.close(); // 关闭之前的filewriter
						} catch (Exception e) {
							log.error(e.getMessage());
						}
					}
				}
				map.remove(fileName); // 从map中移除
				Map<String, FileWriter> fwm = new HashMap<String, FileWriter>();
				FileWriter fw = new FileWriter(file, true); // 创建新的FileWriter
				try {
					lock.lock();
					fwm.put(date, fw); // date 是当前日期字符串： 2011-03-23.11 ,fw
					// 是新创建FileWriter
					map.put(fileName, fwm);
					return fw; // 返回新的文件指针
				} finally {
					lock.unlock();
				}
			}
		} else {
			Map<String, FileWriter> fwm = new HashMap<String, FileWriter>();
			FileWriter fw = new FileWriter(file, true);
			try {
				lock.lock();
				fwm.put(date, fw);
				map.put(fileName, fwm);
				return fw;
			} finally {
				lock.unlock();
			}
		}
	}

	/**
	 * 将log写入文件
	 * 
	 * @param fileName
	 * @param log
	 * @throws IOException
	 */
	public static void bufferedWrite(String fileName,String  pattern, String log)
			throws Exception {
		FileWriter fw = fileWriterManage(fileName, pattern);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(log+"\n");
		bw.flush();
//		bw.close();
	}

}

