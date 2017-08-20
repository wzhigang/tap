package com.tapmobi.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.requests.FetchRequest.PartitionData;

import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
//import kafka.cluster.BrokerEndPoint;
import kafka.common.ErrorMapping;
import kafka.common.TopicAndPartition;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.OffsetRequest;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.TopicMetadataResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaHandler<T> {
	private List<String> borkerList = new ArrayList<String>();
	private static final Logger logger = LoggerFactory
			.getLogger(KafkaHandler.class);
	private KafkaProducer<T, T> kafkaProducer = null;
	private KafkaConsumer<T, T> kafkaConsumer = null;
	private Map<String,String> currentTopics = new ConcurrentHashMap<String,String>();
	private String groupId = DateTimeUtil.getUserDate("yyyyMMddhhmmss");
	private AtomicInteger counter = new AtomicInteger();
	private KafkaConsumer<T,T> buildConsumer(){
		if (kafkaConsumer==null){
			synchronized(this){
			if (kafkaConsumer==null){
				Properties properties = new Properties();
				properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
						ConfigUtil.getString("kafka.servers.addr"));
				properties.put("group.id", groupId);
				properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,
						ConfigUtil.getInt("kafka.session.timeout"));
				properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
						ConfigUtil.getString("kafka.auto.commit"));
				properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,
						ConfigUtil.getString("kafka.auto.commit.interval"));
				properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
						"org.apache.kafka.common.serialization.StringDeserializer");
				properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
						"org.apache.kafka.common.serialization.StringDeserializer");
				kafkaConsumer = new KafkaConsumer<T, T>(
						properties);
			}
		}
		}
		return kafkaConsumer;
	}
	private KafkaProducer<T,T> buildProducer(){
		if (kafkaProducer==null){
			synchronized(this){
			if (kafkaProducer==null){
				Properties properties = new Properties();
				properties.put("bootstrap.servers",
						ConfigUtil.getString("kafka.servers.addr"));
				// properties.put("producer.type", "sync");
				properties.put("key.serializer",
						"org.apache.kafka.common.serialization.StringSerializer");
				// properties.put("value.serializer",
				// "org.apache.kafka.common.serialization.ByteArraySerializer");
				properties.put("value.serializer",
						"org.apache.kafka.common.serialization.StringSerializer");
				kafkaProducer = new KafkaProducer<T, T>(
						properties);
			}
			}
		}
		return kafkaProducer;
	}
	private void destroyProducer(){
		if (kafkaProducer!=null){
			kafkaProducer.close(1000,TimeUnit.MILLISECONDS);
			kafkaProducer = null;
		}
	}
	private void destroyConsumer(){
		if (kafkaConsumer!=null){
			kafkaConsumer.close(1000,TimeUnit.MILLISECONDS);
			kafkaConsumer = null;
		}
	}

	/**
	 * @return the kafkaProducer
	 */
	public KafkaProducer<T, T> getKafkaProducer() {
		return buildProducer();
	}
	/**
	 * @return the kafkaConsumer
	 */
	public KafkaConsumer<T, T> getKafkaConsumer() {
		return buildConsumer();
	}
	public boolean produce(String topic, T key, T value) {
		boolean ret = false;
		ProducerRecord<T, T> kafkaRecord = new ProducerRecord<T, T>(
				topic, key, value);
		kafkaProducer = buildProducer();
		Future<RecordMetadata> future = kafkaProducer.send(kafkaRecord, new Callback() {
			public void onCompletion(RecordMetadata metadata, Exception e) {
				if (null != e) {
					e.printStackTrace();
					logger.error("error while produce {}", e.getMessage());
					// Reconnect to Kafka cluster when it's failed.
					if (kafkaProducer!=null){
						destroyProducer();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					if (counter.incrementAndGet()%20==0){
						logger.info("Exception while writting to kafak,trying to reconnect to kafka cluster again.");
						// mail alert here if it's failed.
					}
				}
				if (logger.isDebugEnabled())
					logger.debug("complete, key {} offset {}",key, metadata.offset());
			}
		});
		return ret;
	}

	public ConsumerRecords<T, T> consume(List<String> topics, int pollMillions) {
		kafkaConsumer = buildConsumer();		
		changeTopics(topics);
		ConsumerRecords<T, T> consumerRecords = null;
		// kafkaConsumer.subscribe("*");
		try {
			consumerRecords = kafkaConsumer.poll(pollMillions);
		} catch (Exception ex) {
			logger.error("error while consume {}", ex.getMessage());
			this.destroyConsumer();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		/**
		if (null != consumerRecords) {
			Map<T, T> records = new HashMap<T, T>(
					consumerRecords.count());
			for (ConsumerRecord<T, T> consumerRecord : consumerRecords) {
				T key = consumerRecord.key();
				T value = consumerRecord.value();
				int partition = consumerRecord.partition();
				long offset = -1L;
				try {
					offset = consumerRecord.offset();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				logger.info(
						"comsuming: topic {} key {} value{} partition {} offset {}",
						topic, key, value, partition, offset);
				records.put(key, value);
			}
		}
		*/
		return consumerRecords;
	}
	
	public void changeTopics(List<String> topics){
		boolean isChanged = false;
		if (topics == null || topics.isEmpty())
			return;
		for (String topic:topics){
			if (!currentTopics.containsKey(topic)){
				isChanged = true;
				break;
			}
		}
		if (isChanged){
			try{
				kafkaConsumer.unsubscribe();
				kafkaConsumer.subscribe(topics);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			currentTopics = new ConcurrentHashMap<String,String>();
			for  (String topic:topics){
				currentTopics.put(topic, topic);
				logger.info("Subscribe the topic {}", topic);
			}
		}
	}
	

	/**
	 * Not Well Test Yet
	 * 
	 * @param seedBrokers
	 * @param port
	 * @param topic
	 * @param partition
	 * @return
	 */
//	private PartitionMetadata findLeader(List<String> seedBrokers, int port,
//			String topic, int partition) {
//		PartitionMetadata partitionMetadata = null;
//		loop: for (String seedBroker : seedBrokers) {
//			SimpleConsumer consumer = null;
//			try {
//				consumer = new SimpleConsumer(seedBroker, port, 100000,
//						64 * 1024, "leaderLookup");
//				List<String> topics = Collections.singletonList(topic);
//				TopicMetadataRequest topicMetadataRequest = new TopicMetadataRequest(
//						topics);
//				TopicMetadataResponse topicMetadataResponse = consumer
//						.send(topicMetadataRequest);
//				List<TopicMetadata> topicMetadatas = topicMetadataResponse
//						.topicsMetadata();
//				for (TopicMetadata topicMetadata : topicMetadatas) {
//					topicMetadata.partitionsMetadata();
//					for (PartitionMetadata pMetadata : topicMetadata
//							.partitionsMetadata()) {
//						if (pMetadata.partitionId() == partition) {
//							partitionMetadata = pMetadata;
//							break loop;
//						}
//					}
//				}
//			} catch (Exception e) {
//				logger.info("error communicating with broker [" + seedBroker
//						+ "] to find leader for [" + topic + ", " + partition
//						+ "] reason: " + e);
//			} finally {
//				if (consumer != null)
//					consumer.close();
//			}
//		}
//		if (partitionMetadata != null) {
//			borkerList.clear();
//			for (BrokerEndPoint replica : partitionMetadata.replicas()) {
//				borkerList.add(replica.host());
//				logger.info("host {}", replica.host());
//			}
//		}
//		return partitionMetadata;
//	}

//	public static long getLastOffset(SimpleConsumer consumer, String topic,
//			int partition, long whichTime, String clientName) {
//		TopicAndPartition topicAndPartition = new TopicAndPartition(topic,
//				partition);
//		Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
//		requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(
//				whichTime, 1));
//		OffsetRequest request = new OffsetRequest(requestInfo,
//				kafka.api.OffsetRequest.CurrentVersion(), clientName);
//		OffsetResponse response = consumer.getOffsetsBefore(request);
//		if (response.hasError()) {
//			logger.info("error fetching data offset data the broker. reason: "
//					+ response.errorCode(topic, partition));
//			return 0;
//		}
//		long[] offsets = response.offsets(topic, partition);
//		return offsets[0];
//	}
//
//	private String findNewLeader(String oldLeader, String topic, int partition,
//			int port) throws Exception {
//		for (int i = 0; i < 3; i++) {
//			boolean goToSleep = false;
//			PartitionMetadata metadata = findLeader(borkerList, port, topic,
//					partition);
//			if (metadata == null) {
//				goToSleep = true;
//			} else if (metadata.leader() == null) {
//				goToSleep = true;
//			} else if (oldLeader.equalsIgnoreCase(metadata.leader().host())
//					&& i == 0) {
//				goToSleep = true;
//			} else {
//				return metadata.leader().host();
//			}
//			if (goToSleep) {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException ie) {
//				}
//			}
//		}
//		logger.info("unable to find new leader after broker failure. exit");
//		throw new Exception(
//				"unable to find new leader after broker failure. exit");
//	}
//
//	public void test() {
//		// 最大读取消息数量
//		long maxReadNum = Long.parseLong("3");
//		// 订阅的topic
//		String topic = "test";
//		// 查找的分区
//		int partition = Integer.parseInt("0");
//		// broker节点
//		List<String> seeds = new ArrayList<String>();
//		seeds.add("localhost");
//		// 端口
//		int port = Integer.parseInt("9092");
//		try {
//			SimpleConsumer consumer = new SimpleConsumer("localhost", port,
//					100000, 64 * 1024, "leaderLookup");
//		} catch (Exception e) {
//			logger.info("Oops:" + e);
//			e.printStackTrace();
//		}
//		// 获取指定topic partition的元数据
//		PartitionMetadata metadata = findLeader(seeds, port, topic, partition);
//		if (metadata == null) {
//			logger.info("can't find metadata for topic and partition. exit");
//			return;
//		}
//		if (metadata.leader() == null) {
//			logger.info("can't find leader for topic and partition. exit");
//			return;
//		}
//		String leadBroker = metadata.leader().host();
//		String clientName = "client_" + topic + "_" + partition;
//		int minBytes = 16;
//		int maxWait = 16;
//		int maxBytes = 128;
//		SimpleConsumer consumer = new SimpleConsumer(leadBroker, port, 100000,
//				64 * 1024, clientName);
//		long readOffset = getLastOffset(consumer, topic, partition,
//				kafka.api.OffsetRequest.EarliestTime(), clientName);
//		int numErrors = 0;
//		while (maxReadNum > 0) {
//			if (consumer == null) {
//				consumer = new SimpleConsumer(leadBroker, port, 100000,
//						64 * 1024, clientName);
//			}
//			LinkedHashMap<TopicPartition, PartitionData> fetchData = new LinkedHashMap<TopicPartition, PartitionData>();
//			fetchData.put(new TopicPartition(topic, partition),
//					new PartitionData(readOffset, maxWait));
//
//			kafka.api.FetchRequest req = new FetchRequestBuilder()
//					.clientId(clientName)
//					.addFetch(topic, partition, readOffset, 100000).build();
//			// FetchRequest req=new FetchRequest().Builder( maxWait, minBytes,
//			// fetchData).build();
//			FetchResponse fetchResponse = consumer.fetch(req);
//
//			if (fetchResponse.hasError()) {
//				numErrors++;
//				short code = fetchResponse.errorCode(topic, partition);
//				logger.info("error fetching data from the broker:" + leadBroker
//						+ " reason: " + code);
//				if (numErrors > 5)
//					break;
//				if (code == ErrorMapping.OffsetOutOfRangeCode()) {
//					readOffset = getLastOffset(consumer, topic, partition,
//							kafka.api.OffsetRequest.LatestTime(), clientName);
//					continue;
//				}
//				consumer.close();
//				consumer = null;
//				try {
//					leadBroker = findNewLeader(leadBroker, topic, partition,
//							port);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				continue;
//			}
//			numErrors = 0;
//			long numRead = 0;
//			Iterator<MessageAndOffset> iterator = fetchResponse.messageSet(
//					topic, partition).iterator();
//			while (iterator.hasNext()) {
//				MessageAndOffset messageAndOffset = iterator.next();
//				long currentOffset = messageAndOffset.offset();
//				if (currentOffset < readOffset) {
//					logger.info("found an old offset: " + currentOffset
//							+ " expecting: " + readOffset);
//					continue;
//				}
//				readOffset = messageAndOffset.nextOffset();
//				ByteBuffer payload = messageAndOffset.message().payload();
//
//				byte[] bytes = new byte[payload.limit()];
//				payload.get(bytes);
//				try {
//					logger.info(String.valueOf(messageAndOffset.offset())
//							+ ": " + new String(bytes, "UTF-8"));
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
//				numRead++;
//				maxReadNum--;
//			}
//
//			if (numRead == 0) {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException ie) {
//				}
//			}
//		}
//		if (consumer != null)
//			consumer.close();
//	}

	public static void main(String[] args) {
		KafkaHandler<String> kafkaUtil = new KafkaHandler<String>();
		List<String> topics = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			kafkaUtil.produce("test"+i, String.valueOf(i), "test" + i);
			logger.info("1");
			topics.add("test"+i);
			kafkaUtil.consume(topics,  100);
		}

//		List<String> seedBrokers = new ArrayList<String>();
//		seedBrokers.add("localhost");
//		kafkaUtil.findLeader(seedBrokers, 9092, "test", 1);
	}
}
