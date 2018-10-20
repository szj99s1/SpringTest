package spring.test.kafka.producer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

import com.alibaba.fastjson.JSON;

/**
 * kafkaProducer模板 使用此模板发送消息
 * 
 * @author wangb
 * 
 */
@Component
public class KafkaProducerServer {
	public static final String SUCCESS_CODE = "00000";
	public static final String SUCCESS_MES = "成功";

	/* kakfa-code */
	public static final String KAFKA_SEND_ERROR_CODE = "30001";
	public static final String KAFKA_NO_RESULT_CODE = "30002";
	public static final String KAFKA_NO_OFFSET_CODE = "30003";

	/* kakfa-mes */
	public static final String KAFKA_SEND_ERROR_MES = "发送消息超时,联系相关技术人员";
	public static final String KAFKA_NO_RESULT_MES = "未查询到返回结果,联系相关技术人员";
	public static final String KAFKA_NO_OFFSET_MES = "未查到返回数据的offset,联系相关技术人员";

	@Autowired
	private KafkaTemplate<String, String> kafkatemplate;

	/**
	 * kafka发送消息模板
	 * 
	 * @param topic
	 *            主题
	 * @param value
	 *            messageValue
	 * @param ifPartition
	 *            是否使用分区 0是\1不是
	 * @param partitionNum
	 *            分区数 如果是否使用分区为0,分区数必须大于0
	 * @param role
	 *            角色:bbc app erp...
	 */
	public Map<String, Object> sndMesForTemplate(String topic, Object value,
			String ifPartition, Integer partitionNum, String role) {
		String key = role + "-" + value.hashCode();
		String valueString = JSON.toJSONString(value);
		valueString = "test";
		if (ifPartition.equals("0")) {
			// 表示使用分区
			int partitionIndex = getPartitionIndex(key, partitionNum);
			ListenableFuture<SendResult<String, String>> result = kafkatemplate
					.send(topic, partitionIndex, key, valueString);
			Map<String,Object> res = checkProRecord(result);
			return res;
		} else {
			ListenableFuture<SendResult<String, String>> result = kafkatemplate
					.send(topic, key, valueString);
			Map<String, Object> res = checkProRecord(result);
			return res;
		}
	}

	/**
	 * 根据key值获取分区索引
	 * 
	 * @param key
	 * @param partitionNum
	 * @return
	 */
	private int getPartitionIndex(String key, int partitionNum) {
		if (key == null) {
			Random random = new Random();
			return random.nextInt(partitionNum);
		} else {
			int result = Math.abs(key.hashCode()) % partitionNum;
			return result;
		}
	}

	/**
	 * 检查发送返回结果record
	 * 
	 * @param res
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, Object> checkProRecord(
			ListenableFuture<SendResult<String, String>> res) {
		Map<String, Object> m = new HashMap<String, Object>();
		if (res != null) {
			try {
				SendResult r = res.get();// 检查result结果集
				/* 检查recordMetadata的offset数据，不检查producerRecord */
				Long offsetIndex = r.getRecordMetadata().offset();
				if (offsetIndex != null && offsetIndex >= 0) {
					m.put("code", KafkaProducerServer.SUCCESS_CODE);
					m.put("message", KafkaProducerServer.SUCCESS_MES);
					return m;
				} else {
					m.put("code", KafkaProducerServer.KAFKA_NO_OFFSET_CODE);
					m.put("message", KafkaProducerServer.KAFKA_NO_OFFSET_MES);
					return m;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				m.put("code", KafkaProducerServer.KAFKA_SEND_ERROR_CODE);
				m.put("message", KafkaProducerServer.KAFKA_SEND_ERROR_MES);
				return m;
			} catch (ExecutionException e) {
				e.printStackTrace();
				m.put("code", KafkaProducerServer.KAFKA_SEND_ERROR_CODE);
				m.put("message", KafkaProducerServer.KAFKA_SEND_ERROR_MES);
				return m;
			}
		} else {
			m.put("code", KafkaProducerServer.KAFKA_NO_RESULT_CODE);
			m.put("message", KafkaProducerServer.KAFKA_NO_RESULT_MES);
			return m;
		}
	}

}