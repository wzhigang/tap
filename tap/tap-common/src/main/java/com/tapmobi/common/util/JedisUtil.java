/**
 * 
 */
package com.tapmobi.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanResult;

/**
 * @author wzg
 * 封装jedis的一些接口
 */
public class JedisUtil {
	private static Logger log = LoggerFactory.getLogger(JedisUtil.class);

	public static Set<String> getRedisSet(String key) {
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			Set<String> result = jedis.smembers(key);
			return result;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	public static String getRedisSetRandom(String key) {
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			String result = jedis.srandmember(key);
			return result;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}
	
	public static Long addRedisSet(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			Long result = jedis.sadd(key, value);
			return result;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	public static Long delRedisSet(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			Long result = jedis.srem(key, value);
			return result;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	/*
	 * 在list头部添加一个
	 */
	public static Long lpushRedisList(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			Long result = jedis.lpush(key, value);
			return result;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	/*
	 * 在list头部添加多个
	 */
	public static Long lpushRedisList(String key, String[] value) {
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			Long result = jedis.lpush(key, value);
			return result;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	/*
	 * 从list尾部消费一个
	 * 返回字符串和队列大小
	 */
	public static Map<String, String> rpopRedisListWhithSize(String key) {
		Jedis jedis = null;
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			jedis = RedisUtil.getJedis();
			String value = jedis.rpop(key);
			Long curSize = jedis.llen(key);

			resultMap.put("value", value);
			resultMap.put("size", String.valueOf(curSize));
			return resultMap;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}
	
//	public static String rpopRedisList(String key) {
//		Jedis jedis = null;
//		try {
//			jedis = RedisUtil.getJedis();
//			Long curSize = jedis.llen(key);
//			if (curSize!=null && curSize.longValue()>0 && curSize.longValue()%10==0) {
//				log.info(key + " list size:"+curSize.longValue());
//			}
//			String value = jedis.rpop(key);// 从list尾部消费一个
//			return value;
//		} finally {
//			if (jedis != null)
//				jedis.close();
//		}
//	}
	
	/*
	 * 从list尾部消费一个
	 */
	public static String rpopRedisList(String key) {
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			String value = jedis.rpop(key);
			return value;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	/*
	 * 返回队列大小
	 */
	public static Long getRedisListSize(String key) {
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			Long curSize = jedis.llen(key);
			return curSize;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	/*
	 * 返回set大小
	 */
	public static Long getRedisSetSize(String key) {
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			Long curSize = jedis.scard(key);
			return curSize;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	/*
	 * 遍历set
	 */
	public static ScanResult<String> ssan(String key, String cursor) {
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			 ScanResult<String>  result = jedis.sscan(key, cursor);
			return result;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	/*
	 * 计数器加1
	 */
	public static Long incrRedis(String key) {
		return incrRedis(key, -1);
	}

	public static Long incrRedis(String key, int expireSeconds) {
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			Long result = jedis.incr(key);
			// 第一次操作时才设置过期时间
			if (expireSeconds>0 && result!=null && result.longValue()==1) {
				jedis.expire(key, expireSeconds);
			}
			return result;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	public static String getRedis(String key) {
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			String value = jedis.get(key);
			return value;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	public static Map<String, String> hgetAll(String key) {
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			Map<String, String> result = jedis.hgetAll(key);
			return result;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	public static Set<String> keys(String pattern) {
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			Set<String> result = jedis.keys(pattern);
			return result;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}
	
	public static List<String> mget(String[] keys) {
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			 List<String> result = jedis.mget(keys);
			return result;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}
	
	public static boolean setExpireKey(String key,int time){
		Jedis jedis = null;
		try{
			jedis = RedisUtil.getJedis();
			jedis.setex(key, time, "0");
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}finally{
			if (jedis!=null)
				jedis.close();
		}
		return true;
	}
	
	public static boolean existsKey(String key){
		Jedis jedis = null;
		try{
			jedis = RedisUtil.getJedis();
			return jedis.exists(key);
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}finally{
			if (jedis!=null)
				jedis.close();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		 System.out.println(lpushRedisList("UNSEND-20170621-1002-30669","B3EF96B5-C2F4-4350-AD94-4CCAE06CA37E:1002@@ios@@10.3@@apple@@cn@@1498032382000"));
//		 System.out.println(lpushRedisList("UNSEND-20170621-1002-30669","B43C2E9D-D7A7-459B-BCF8-39E8A9AB54DA:1002@@ios@@10.3@@apple@@cn@@1498032382000"));
//		 System.out.println(lpushRedisList("UNSEND-20170621-1002-30669","58AB52BA-8CFB-44D4-B10F-AB5191C7D0CC:1002@@ios@@10.3@@apple@@cn@@1498032382000"));
//		 System.out.println(lpushRedisList("UNSEND-20170621-1002-30669","75604C8D-FA6E-4053-BA76-73126EA9F268:1002@@ios@@10.3@@apple@@cn@@1498032382000"));
//		 System.out.println(lpushRedisList("UNSEND-20170621-1002-30669","49EE3B0A-147F-4BF4-9A0E-2DD1DA3AFBBA:1002@@ios@@10.3@@apple@@cn@@1498032382000"));
		 System.out.println(getRedisSetSize("proxy_vaild_CN"));
	}

}
