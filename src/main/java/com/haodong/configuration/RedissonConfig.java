package com.haodong.configuration;

//import com.zns.properties.RedisConfigProperties;
import org.redisson.Redisson;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class RedissonConfig {
//    @Autowired
//    private RedisConfigProperties redisConfigProperties;


    @Autowired
    private Environment environment;



    @Bean
    public RedisConnectionFactory myLettuceConnectionFactory() {
//        String[] nodes = new String[6];
        String s = "";
        for (int i = 1; i <= 6; i++) {
            s += ("127.0.0.1:700" + i + ",");
        }
        s = s.substring(0, s.length() - 1);

        Map<String, Object> source = new HashMap<String, Object>();
        source.put("spring.redis.cluster.nodes", s);
        source.put("spring.redis.cluster.timeout", environment.getProperty("spring.redis.cluster.timeout"));
        source.put("spring.redis.cluster.max-redirects", environment.getProperty("spring.redis.cluster.max-redirects"));
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));

//        redisClusterConfiguration.setClusterNodes();
        return new LettuceConnectionFactory(redisClusterConfiguration);
    }


    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(@Autowired  RedisConnectionFactory myLettuceConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(myLettuceConnectionFactory);
        return template;
    }


    //添加redisson的bean
    @Bean
    public Redisson redisson() {
        //redisson版本是3.5，集群的ip前面要加上“redis://”，不然会报错，3.2版本可不加
        List<String> clusterNodes = new ArrayList<>();
        String[] ports= {"7001","7002","7003","7004","7005","7006"};
        for (int i = 0; i < ports.length; i++) {
            clusterNodes.add("redis://127.0.0.1:" + ports[i]);
        }
        Config config = new Config();
        ClusterServersConfig clusterServersConfig = config.useClusterServers()
                .addNodeAddress(clusterNodes.toArray(new String[clusterNodes.size()]));
        return (Redisson) Redisson.create(config);
    }
}