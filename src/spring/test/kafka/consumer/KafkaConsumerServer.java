package spring.test.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.springframework.kafka.listener.MessageListener;

/**
 *@Author shenzj
 *@Date 2018年10月20日上午11:47:39
 *@Description
 *
 */
public class KafkaConsumerServer implements MessageListener<String, String> {
	  private static Logger LOG = Logger.getLogger(KafkaConsumerServer.class);
	    
	  /**
	     * 监听器自动执行该方法
	     *     消费消息
	     *     自动提交offset
	     *     执行业务代码
	     *     （high level api 不提供offset管理，不能指定offset进行消费）
	     */
	    @Override
	    public void onMessage(ConsumerRecord<String, String> record) {
	        LOG.info("=============kafkaConsumer开始消费=============");
	        try{
	        	Thread.sleep(1000);
	        }catch(Exception e){
	        	
	        }
	        String topic = record.topic();
	        String key = record.key();
	        String value = record.value();
	        long offset = record.offset();
	        int partition = record.partition();
	        LOG.info("-------------topic:"+topic);
	        LOG.info("-------------value:"+value);
	        LOG.info("-------------key:"+key);
	        LOG.info("-------------offset:"+offset);
	        LOG.info("-------------partition:"+partition);
	        LOG.info("~~~~~~~~~~~~~kafkaConsumer消费结束~~~~~~~~~~~~~");
	    }
}

