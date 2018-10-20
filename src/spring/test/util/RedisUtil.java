package spring.test.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 *@Author shenzj
 *@Date 2018年10月17日上午11:39:06
 *@Description
 *
 */
@Component
public class RedisUtil {
	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate redisTemplate;
	
	public RedisTemplate getRedisTemplate(){
		return redisTemplate;
	}
}

