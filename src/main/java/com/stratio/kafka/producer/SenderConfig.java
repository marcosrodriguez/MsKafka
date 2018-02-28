package com.stratio.kafka.producer;

import com.stratio.dto.Product;
import com.stratio.kafka.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SenderConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private KafkaConfig config;

    @Bean(name = "producerConfig")
    @Profile("dev")
    public Map<String, Object> producerConfig() {
        Map<String, Object> props = new HashMap<>();
        // list of host:port pairs used for establishing the initial connections to the Kakfa cluster
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }


    @Bean(name = "producerConfig")
    @Profile("prod")
    public Map<String, Object> producerConfigTLS() {
        Map<String, Object> props = new HashMap<>();
        // list of host:port pairs used for establishing the initial connections to the Kakfa cluster
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put("security.protocol", config.getSecurityProtocol());
        props.put("ssl.truststore.location", config.getSsl().getTruststore().getLocation());
        props.put("ssl.truststore.password", config.getSsl().getTruststore().getPassword());
        props.put("ssl.keystore.location", config.getSsl().getKeystore().getLocation());
        props.put("ssl.keystore.password", config.getSsl().getKeystore().getPassword());
        props.put("ssl.key.password", config.getSsl().getKeyPassword());


        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>((Map<String, Object>) applicationContext.getBean("producerConfig"),
                new StringSerializer(), new StringSerializer() );
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public Sender sender() {
        return new Sender();
    }
}
