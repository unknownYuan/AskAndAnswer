package com.haodong.util;

import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class RedisssionCluster implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        Config config = new Config();
        config.setTransportMode(TransportMode.EPOLL);
        String[] ports = {"7001", "7002", "7002", "7003", "7004", "7005", "7006"};

        for (int i = 0; i < ports.length; i++) {
            config.useClusterServers()
                    .addNodeAddress("redis://127.0.0.1:" + ports[i]);
        }
    }

}
