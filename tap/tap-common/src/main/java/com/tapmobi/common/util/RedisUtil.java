package com.tapmobi.common.util;


import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisUtil {
    
    //Redis服务器IP
    private static String ADDR = ConfigUtil.getString("tapmobi.redis.ip");
    
    //Redis的端口号
    private static int PORT = ConfigUtil.getInt("tapmobi.redis.port");;
    
    //访问密码
    private static String AUTH = ConfigUtil.getString("tapmobi.redis.auth");;
    
    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 100;
    
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 20;
    
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 1000;
    
    private static int TIMEOUT = 1000;
    
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;
    
    private static JedisPool jedisPool = null;
    
    /**
     * 初始化Redis连接池
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            if (!StringUtils.isEmpty(AUTH)) {
            	jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
            } else {
            	jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取Jedis实例
     * @return
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

	public static boolean storeMessageInList(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.lpush(key, value);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (jedis != null)
				jedis.close();
		}
		return false;
	}

	public static String getMessageFromList(String key) {
		Jedis jedis = null;
		String value = "";
		try {
			jedis = RedisUtil.getJedis();
			value = jedis.rpop(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (jedis != null)
				jedis.close();
		}
		return value;
	}
    
    public static void main(String[] args){
    	getJedis();
    	System.out.println("ok!");
    }
}
