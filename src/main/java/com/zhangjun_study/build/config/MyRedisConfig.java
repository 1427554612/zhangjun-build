package com.zhangjun_study.build.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class MyRedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        // 1、创建RedisTemplate对象
        // 2、设置redis链接工厂
        // 3、设置redis key和 value的序列化方式
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();  // string序列化形式
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);  //  json序列化形式
        jackson2JsonRedisSerializer.setObjectMapper(new ObjectMapper());
        // 设置普通类型的key序列化
        template.setKeySerializer(redisSerializer);
        // 设置普通类型的value序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // 设置hash类型的序列化方式
        template.setHashKeySerializer(redisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }
}
