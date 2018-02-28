package com.stratio.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfig {

    private String bootstrapServers;

    private String groupId;

    private String securityProtocol;

    private SSL ssl;


}
