package dev.bomboclat.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class JwtUtil {

    // Simple secret key for JWT signing - in production, this should be more secure
    private static final String SECRET_KEY = "bomboclat-secret-key-for-jwt-signing";

    /**
     * Generate a JWT token for the given email
     * 
     * @param email The email to include in the token
     * @return The generated JWT token
     */
    public String generateToken(String email) {
        try {
            // Create JWT header
            Map<String, String> header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");
            String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            String encodedHeader = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));

            // Create JWT payload
            Instant now = Instant.now();
            Instant expiry = now.plus(1, ChronoUnit.HOURS);

            String payloadJson = String.format(
                    "{\"sub\":\"%s\",\"iat\":%d,\"exp\":%d}",
                    email, now.getEpochSecond(), expiry.getEpochSecond()
            );
            String encodedPayload = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

            // Create signature
            String signatureBase = encodedHeader + "." + encodedPayload;
            String signature = createSignature(signatureBase);

            // Combine all parts to form the JWT token
            return encodedHeader + "." + encodedPayload + "." + signature;
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    /**
     * Validate a JWT token
     * 
     * @param token The token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            System.out.println("[DEBUG_LOG] Validating token: " + token);

            // Split the token into its parts
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                System.out.println("[DEBUG_LOG] Token validation failed: Invalid token format");
                return false;
            }

            String encodedHeader = parts[0];
            String encodedPayload = parts[1];
            String providedSignature = parts[2];

            // Verify the signature
            String signatureBase = encodedHeader + "." + encodedPayload;
            String expectedSignature = createSignature(signatureBase);
            System.out.println("[DEBUG_LOG] Expected signature: " + expectedSignature);
            System.out.println("[DEBUG_LOG] Provided signature: " + providedSignature);

            if (!expectedSignature.equals(providedSignature)) {
                System.out.println("[DEBUG_LOG] Token validation failed: Invalid signature");
                return false;
            }

            // Decode the payload
            String payloadJson = new String(Base64.getUrlDecoder().decode(encodedPayload), StandardCharsets.UTF_8);
            System.out.println("[DEBUG_LOG] Decoded payload: " + payloadJson);

            JsonReader jsonReader = Json.createReader(new StringReader(payloadJson));
            JsonObject payload = jsonReader.readObject();

            // Check if the token has expired
            long expTime = payload.getJsonNumber("exp").longValue();
            long currentTime = Instant.now().getEpochSecond();
            System.out.println("[DEBUG_LOG] Token expiry time: " + expTime);
            System.out.println("[DEBUG_LOG] Current time: " + currentTime);

            if (currentTime > expTime) {
                System.out.println("[DEBUG_LOG] Token validation failed: Token expired");
                return false;
            }

            System.out.println("[DEBUG_LOG] Token validation successful");
            return true;
        } catch (Exception e) {
            System.out.println("[DEBUG_LOG] Token validation failed with exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Extract the email from a JWT token
     * 
     * @param token The token to extract the email from
     * @return The email extracted from the token, or null if the token is invalid
     */
    public String extractEmail(String token) {
        try {
            if (!validateToken(token)) {
                return null;
            }

            // Split the token and decode the payload
            String[] parts = token.split("\\.");
            String encodedPayload = parts[1];
            String payloadJson = new String(Base64.getUrlDecoder().decode(encodedPayload), StandardCharsets.UTF_8);

            // Parse the JSON and extract the subject (email)
            JsonReader jsonReader = Json.createReader(new StringReader(payloadJson));
            JsonObject payload = jsonReader.readObject();
            return payload.getString("sub");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a signature for the JWT token
     * 
     * @param data The data to sign
     * @return The signature
     */
    private String createSignature(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest((data + SECRET_KEY).getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }
}
