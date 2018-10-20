package spring.test.util;


import java.lang.reflect.Method;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 通过spring管理redis缓存配置
 * 
 * @author Administrator
 *
 */
@Configuration  
@EnableCaching 
public class RedisCacheConfig extends CachingConfigurerSupport {
    private volatile JedisConnectionFactory jedisConnectionFactory;
    private volatile RedisTemplate<String, String> redisTemplate;
    private volatile RedisCacheManager redisCacheManager;

    public RedisCacheConfig() {
        super();
    }

    /**
     * 带参数的构造方法 初始化所有的成员变量
     * 
     * @param jedisConnectionFactory
     * @param redisTemplate
     * @param redisCacheManager
     */
    public RedisCacheConfig(JedisConnectionFactory jedisConnectionFactory, RedisTemplate<String, String> redisTemplate,
            RedisCacheManager redisCacheManager) {

        this.jedisConnectionFactory = jedisConnectionFactory;
        this.redisTemplate = redisTemplate;
        this.redisCacheManager = redisCacheManager;
    	System.out.println("objhas="+this.hashCode());
    }

    public JedisConnectionFactory getJedisConnecionFactory() {
        return this.jedisConnectionFactory;
    }

    public RedisTemplate<String, String> getRedisTemplate() {
    	System.out.println("objhas="+this.hashCode());
        return this.redisTemplate;
    }

    public RedisCacheManager getRedisCacheManager() {
        return this.redisCacheManager;
    }

    @Bean
    public KeyGenerator customKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... objects) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : objects) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }
}
