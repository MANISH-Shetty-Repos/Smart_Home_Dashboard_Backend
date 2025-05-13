package com.smarthome.smart_home_dashboard.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String extractUsername(String token) {
        try {
            return extractClaim(token, claims -> (String) claims.get(Claims.SUBJECT));
        } catch (Exception e) {
            logger.error("Failed to extract username from token: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public <T> T extractClaim(String token, Function<Map<String, Object>, T> claimsResolver) {
        final Map<String, Object> claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration) {
        try {
            return Jwts.builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            logger.error("Failed to generate JWT token: {}", e.getMessage());
            throw new IllegalStateException("Could not generate JWT token", e);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
            if (!isValid) {
                logger.warn("Token validation failed for user: {}. Expired: {}", username, isTokenExpired(token));
            }
            return isValid;
        } catch (Exception e) {
            logger.error("Invalid JWT token during validation: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            Long expiration = extractClaim(token, claims -> (Long) claims.get(Claims.EXPIRATION));
            if (expiration == null) {
                return true;
            }
            Date expirationDate = new Date(expiration * 1000);
            boolean isExpired = expirationDate.before(new Date());
            if (isExpired) {
                logger.debug("Token expired at: {}", expirationDate);
            }
            return isExpired;
        } catch (Exception e) {
            logger.error("Failed to check token expiration: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    private Date extractExpiration(String token) {
        Long expiration = extractClaim(token, claims -> (Long) claims.get(Claims.EXPIRATION));
        return expiration != null ? new Date(expiration * 1000) : null;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractAllClaims(String token) {
        try {
            // Split the JWT token into its three parts: Header, Payload, Signature
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT token format");
            }

            // Decode the payload (second part) which contains the claims
            String payload = new String(Decoders.BASE64URL.decode(parts[1]), StandardCharsets.UTF_8);
            Map<String, Object> claims = objectMapper.readValue(payload, Map.class);

            // Verify the signature manually using HMAC-SHA256
            String headerAndPayload = parts[0] + "." + parts[1];
            byte[] signature = Decoders.BASE64URL.decode(parts[2]);

            // Compute the expected signature
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
            byte[] computedSignature = mac.doFinal(headerAndPayload.getBytes(StandardCharsets.UTF_8));

            // Compare the signatures
            if (!java.util.Arrays.equals(signature, computedSignature)) {
                throw new IllegalArgumentException("Invalid JWT signature");
            }

            return claims;
        } catch (Exception e) {
            logger.error("Failed to parse JWT token: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secretKey);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid Base64-encoded secret key: {}", e.getMessage());
            throw new IllegalArgumentException("JWT secret key must be Base64-encoded", e);
        }
        if (keyBytes.length < 32) {
            logger.error("JWT secret key is too short for HS256 (must be at least 32 bytes)");
            throw new IllegalArgumentException("JWT secret key must be at least 32 bytes for HS256");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}