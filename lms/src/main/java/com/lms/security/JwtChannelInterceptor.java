package com.lms.security;


import com.lms.dto.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {
    @Autowired
    private SecretKeyReader secretKeyReader;

    JwtChannelInterceptor(SecretKeyReader secretKeyReader) {
        this.secretKeyReader = secretKeyReader;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        SimpMessageHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, SimpMessageHeaderAccessor.class);

        if (headerAccessor != null && headerAccessor.getUser() == null) { // Check if user is already authenticated
            String authToken = headerAccessor.getFirstNativeHeader("Authorization");
            if (authToken != null && authToken.startsWith("Bearer ")) {
                authToken = authToken.substring(7);
                validateToken(authToken);
            }
        }
        return message;
    }

    private void validateToken(String token) {
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyReader.tokenSecretKey.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
            Long userId = claims.get("userId", Long.class);
            String email = claims.get("email", String.class);
            String userRole = claims.get("role", String.class);
            UserInfo userInfo = new UserInfo(userId, email, userRole);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userInfo, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private Authentication getAuthentication(String token) {
        // Extract authentication info from token
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("role"));
        return new UsernamePasswordAuthenticationToken("user", null, authorities);
    }
}