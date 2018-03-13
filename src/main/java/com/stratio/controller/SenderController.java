package com.stratio.controller;

import com.google.gson.Gson;
import com.stratio.dto.Product;
import com.stratio.service.kafka.KafkaService;
import com.stratio.service.security.BaasSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.util.Map;



@RestController
public class SenderController {

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    BaasSecurityService service;

    @RequestMapping(value = "/produce", method = RequestMethod.POST)
    public String produce(@RequestBody Product product, @RequestHeader HttpHeaders headers) throws LoginException {

        /*String token = headers.getFirst("Set-Cookie");

        if (!service.isValidToken(token)) {
            throw new LoginException("Invalid Token");
        }*/

        kafkaService.produce(product);
        return "Producing to Kafka: " + product.toString();
    }

}
