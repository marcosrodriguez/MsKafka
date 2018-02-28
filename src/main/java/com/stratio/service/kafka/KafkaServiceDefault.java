package com.stratio.service.kafka;

import com.stratio.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.stratio.kafka.producer.Sender;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceDefault implements KafkaService{


    @Value("${kafka.topic}")
    private String topic;

    @Autowired
    private Sender sender;

    @Override
    public void produce(Product product){
        sender.send(product.getTopic(), product.getMessage());
    }

}
