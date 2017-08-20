package com.tapmobi.common.util;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 仓库类Storage实现缓冲区
 * 
 */
public class Storage<T> {
	private static final Logger logger = LoggerFactory
			.getLogger(Storage.class);	// 仓库最大存储量
	private final int MAX_SIZE = 1024 * 100;

	// 仓库存储的载体
	private LinkedBlockingQueue<T> list;
	
	private volatile boolean isFull = false;
	private Lock lock = new ReentrantLock();
	
	public Storage(int size){
		if (size<1)
			size = 1024*10;
		list  = new LinkedBlockingQueue<T>(size);
	}

	// 生产
	public boolean produce(T t) {
		boolean flag = false;
		// 如果仓库剩余容量为0
		if (list.size() == MAX_SIZE) {
			logger.warn("The capacity:{}" , MAX_SIZE
					,"/t, you can't produce in the storage right now!");
		}
		try {
			// 放入对象，非阻塞模式
			flag = list.offer(t);
			try{
				lock.lock();
				this.setFull(!flag);
			}finally{
				lock.unlock();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error while insert into the queue,{}",e.getMessage());
		}
		logger.info("Producer: the capacity=" + list.size());
		return flag;
	}

	// 消费
	public T consume() {
		T t = null;
		// 如果仓库存储量不足
		if (list.size() == 0) {
			logger.info("The capacity::0/t, you can not consume in the storage!");
		}
		try {
			// 消费产品，阻塞模式
			t = list.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("Consumor: the capacity= {}" , list.size());
		return t;
	}

	// set/get方法
	public LinkedBlockingQueue<T> getList() {
		return list;
	}
	
	public boolean isEmpty(){
		return list.isEmpty();
	}
	
	/**
	 * @param isFull the isFull to set
	 */
	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}

	public boolean isFull(){
		return isFull;
	}

	public void setList(LinkedBlockingQueue<T> list) {
		this.list = list;
	}

	public int getMAX_SIZE() {
		return MAX_SIZE;
	}
	
	public static void main(String[] args)  
    {  
        // 仓库对象  
        Storage<String> storage = new Storage<String>(1024*10);  

        // 生产者对象  
        for (int i = 0; i<10 ;i++){
        	Producer p = new Producer(storage);  
            // 线程开始执行  
            p.start();
        }
        // 消费者对象  
        for (int i = 0; i<7 ;i++){
            Consumer c =  new Consumer(storage); 
            // 线程开始执行  
            c.start();  
        }
    }  
	
	/** 
	 * 生产者Producer和消费者Consumer类继承线程类Thread 
	 *  
	 */  
private static class Producer extends Thread  
	{  
	    // 所在放置的仓库  
	    private Storage<String> storage;  
	  
	    // 构造函数，设置仓库  
	    public Producer(Storage<String> storage)  
	    {  
	        this.storage = storage;  
	    }  
	  
	    // 线程run函数  
	    public void run()  
	    {  
	    	while (true)
	    		produce();  
	    }  
	  
	    // 调用仓库Storage的生产函数  
	    public void produce()  
	    {  
	    	String str = new String();
	        storage.produce(str);  
	    }  
	  
	    public Storage<String> getStorage()  
	    {  
	        return storage;  
	    }  
	  
	    public void setStorage(Storage<String> storage)  
	    {  
	        this.storage = storage;  
	    }  
	}  
	
private static class Consumer extends Thread  
	{  
	    // 所在放置的仓库  
	    private Storage<String> storage;  
	  
	    // 构造函数，设置仓库  
	    public Consumer(Storage<String> storage)  
	    {  
	        this.storage = storage;  
	    }  
	  
	    // 线程run函数  
	    public void run()  
	    {  
	    	while (true)
	    		consume();  
	    }  
	  
	    public void consume()  
	    {  
	        String object = storage.consume();
	        logger.info("object="+object.toString());
	        object = null;
	    }  
	  
	    public Storage<String> getStorage()  
	    {  
	        return storage;  
	    }  
	  
	    public void setStorage(Storage<String> storage)  
	    {  
	        this.storage = storage;  
	    }  
	}
	
}