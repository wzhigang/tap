package com.tapmobi.common.util;


import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class MultiRedisUtil {
    
    private static String serverName = ConfigUtil.getString("tapmobi.redis.multi.name");

    //Redis服务器IP
    private static String ADDR = ConfigUtil.getString("tapmobi.redis.multi.ip");
    
    //Redis的端口号
    private static String PORT = ConfigUtil.getString("tapmobi.redis.multi.port");;
    
    //访问密码
    private static String AUTH = ConfigUtil.getString("tapmobi.redis.multi.auth");;
    
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
    
    private static Map<String, JedisPool> jedisMap = new HashMap<String, JedisPool>();
    
    /**
     * 初始化Redis连接池
     */
    static {
    	if (serverName!=null && ADDR!=null && PORT!=null) {
        	String[] serverNameArray = serverName.split(",");
        	String[] ADDRArray = ADDR.split(",");
        	String[] PORTArray = PORT.split(",");
        	String[] AUTHArray = AUTH.split(",");

        	if (serverNameArray!=null && ADDRArray!=null && PORTArray!=null && serverNameArray.length==ADDRArray.length && ADDRArray.length==PORTArray.length && PORTArray.length==AUTHArray.length) {
        		for (int i=0; i<serverNameArray.length; i++) {
            		try {
                    	JedisPoolConfig config = new JedisPoolConfig();
                    	JedisPool jedisPool = new JedisPool(config, ADDRArray[i], Integer.parseInt(PORTArray[i]), TIMEOUT, AUTHArray[i]);
                    	jedisMap.put(serverNameArray[i], jedisPool);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
        		}
        	}
    	}
    }
    
    /**
     * 获取Jedis实例
     * @return
     */
    public synchronized static Jedis getJedis(String serverName) {
        try {
        	JedisPool jedisPool = jedisMap.get(serverName);
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
    public static void returnResource(String serverName, final Jedis jedis) {
    	JedisPool jedisPool = jedisMap.get(serverName);
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }
    
    public static void main(String[] args){
    	getJedis("ali-us-73");
    	getJedis("ali-us-143");
    	getJedis("ali-cn-17");
    	System.out.println("ok!");
    }
}
