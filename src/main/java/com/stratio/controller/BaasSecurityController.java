package com.stratio.controller;

import com.stratio.service.security.BaasSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jjsanchez on 5/07/17.
 */
@RestController
@Slf4j
@RequestMapping("/login_service/v1")
public class BaasSecurityController {

    @Autowired
    BaasSecurityService service;

    @GetMapping("login")
    public ResponseEntity<String> getSecurityToken(@RequestParam String user,
                                                   @RequestParam String password) {

        String token = service.getSecurityToken(user, password);

        return ResponseEntity.ok(token);
    }

    @GetMapping("/validate/token")
    public boolean isValidToken(@RequestParam String token) {

        return service.isValidToken(token);
    }
}
