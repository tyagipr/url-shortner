package com.example.urlshortner.configs;

import com.example.urlshortner.exception.ForbiddenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            jwt = authorizationHeader.substring(7);
//            username = extractUsername(jwt); // Implement this method based on your JWT structure
//        }

        if (authorizationHeader != null) {
            jwt = authorizationHeader;
            try {
                username = extractUsername(jwt); // Implement this method based on your JWT structure
            }catch (ForbiddenException ex) {
                request.setAttribute("invalid_token_error", ex.getMessage());
            }
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (validateToken(jwt)) { // Implement token validation logic here
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, new ArrayList<>()); // You should fetch user details and roles
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        System.out.println(request.getAttribute("invalid_token_error"));
        filterChain.doFilter(request, response);
    }

    public String extractUsername(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            Map<String, String> data = new HashMap<>();
            data.put("detail", "forbidden");
            throw new ForbiddenException(data);
        }

        String payload = parts[1];
        String decodedPayload = new String(Base64.getUrlDecoder().decode(payload));

        JSONObject payloadJson = new JSONObject(decodedPayload);
        return payloadJson.getString("sub");

    }

    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false; // Invalid token format
            }

            String payload = parts[1];
            String decodedPayload = new String(Base64.getUrlDecoder().decode(payload));

            JSONObject payloadJson = new JSONObject(decodedPayload);
            long exp = payloadJson.getLong("exp");

            // Check if the token is expired
            return System.currentTimeMillis() / 1000 <= exp;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
