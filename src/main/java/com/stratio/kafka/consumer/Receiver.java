package com.stratio.kafka.consumer;

import com.stratio.dto.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {
        return latch;
    }

    @KafkaListener(topics = "${kafka.topic}")
    public void receive(String product) {
        log.info("received message='{}'", product);
        latch.countDown();
    }
}
