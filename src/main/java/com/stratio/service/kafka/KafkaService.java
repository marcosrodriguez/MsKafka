package com.stratio.service.kafka;

import com.stratio.dto.Product;

public interface KafkaService {

    void produce(Product product);
}
