package com.stratio.kafka.config;

import lombok.Data;

@Data
public class SSL {

    private Credentials keystore;

    private Credentials truststore;

    private String keyPassword;

}
