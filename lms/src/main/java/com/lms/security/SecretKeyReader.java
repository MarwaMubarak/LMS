package com.lms.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecretKeyReader {
    @Value("${auth.secret}")
    public String tokenSecretKey;
}
