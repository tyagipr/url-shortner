package com.example.urlshortner.utils;

import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class JwtUtil {

    static String SECRET_KEY = "tcofMrbLFVAeChUN7oiZvZQskigfxlIBWfCcvanuhww=";
    public static String generateToken(String username) {
        long currentTimeMillis = System.currentTimeMillis();
        long expirationTime = currentTimeMillis + 1000 * 60 * 60; // 1 hour expiration

        String header = Base64.getUrlEncoder().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
        String payload = Base64.getUrlEncoder().encodeToString(("{\"sub\":\"" + username + "\",\"iat\":" + currentTimeMillis + ",\"exp\":" + expirationTime + "}").getBytes());

        String signature = generateSignature(header, payload, SECRET_KEY);
        return header + "." + payload + "." + signature;
    }

    private static String generateSignature(String header, String payload, String secret) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            hmac.init(secretKey);

            String data = header + "." + payload;
            byte[] signatureBytes = hmac.doFinal(data.getBytes());
            return Base64.getUrlEncoder().encodeToString(signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC SHA256 signature", e);
        }
    }
}
