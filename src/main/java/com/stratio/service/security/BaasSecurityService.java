package com.stratio.service.security;

public interface BaasSecurityService {

    String getSecurityToken(String user, String pass);

    Boolean isValidToken(String token);

}
