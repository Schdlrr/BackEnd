package com.schdlr.service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.schdlr.model.TokenKey;
import com.schdlr.repo.TokenKeyRepo;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;

/*
 * JWT handles API requests related to JWT authorization and creation.
 * 
 * Responsibilities:
 * - Creating refresh and sign-in tokens.
 * - Authenticating them when needed.
 * - Correctly using keys for authentication and token creation
 * - Extracting details from the tokens
 * 
 * Annotations:
 * - @Service: Marks this class as a service
 */

@Service
public class JWTService {

  TokenKeyRepo tokenKeyRepo;

   // Constructor injection for TokenKeyRepo dependency
  public JWTService(TokenKeyRepo tokenKeyRepo) {
    this.tokenKeyRepo = tokenKeyRepo;
  }

  /*
   * Generates a refresh token valid for approximately three weeks.
   * username - The email for whom the token is created.
   * returns A signed JWT refresh token.
   */
  public String generateRefreshToken(String email,String role) throws NoSuchAlgorithmException, InvalidKeySpecException {
    Map<String, Object> claims = new HashMap<>();

    TokenKey tokenKey = getRandomActiveKey();
    PrivateKey privateKey = getPrivateKey(tokenKey);

    return generateToken(claims,email, new Date(System.currentTimeMillis()),
        new Date(System.currentTimeMillis() + 1000l * 60l * 60l * 24l * 7l * 3l), email, privateKey);
  }

   /*
   * Generates an access token valid for 10 minutes.
   * username-The email for whom the token is created.
   * returns A signed JWT access token.
   */
  public String generateAccessToken(String email,String role) throws NoSuchAlgorithmException, InvalidKeySpecException {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", role);

    TokenKey tokenKey = getRandomActiveKey(); // Fetch an active key for signing
    PrivateKey privateKey = getPrivateKey(tokenKey); // Convert stored key to PrivateKey

    return generateToken(claims, email, new Date(System.currentTimeMillis()),
        new Date(System.currentTimeMillis() + 1000l * 60l * 10l), email, privateKey);

  }


  /**
   * Fetches a random active key from the database.
   * returns A random active TokenKey.
   */
  public TokenKey getRandomActiveKey() {
    List<TokenKey> activeKeys = tokenKeyRepo.findAllActiveKeys();
    if (activeKeys.isEmpty()) {
      throw new IllegalStateException("No active keys available");
    }

    return activeKeys.get(ThreadLocalRandom.current().nextInt(activeKeys.size()));
  }

  /*
   * Extracts a token from cookies in the HTTP request.
   * request-The HTTP request containing cookies.
   * cookieName-The name of the cookie to extract.
   * returns The token value or null if not found.
   */
  private PrivateKey getPrivateKey(TokenKey tokenKey) {
    try {
      String privateKey = tokenKey.getPrivateKey();
      byte[] keyBytes = Base64.getDecoder().decode(privateKey);
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PrivateKey privateKey2 = keyFactory.generatePrivate(keySpec);
      return privateKey2;
    } catch (IllegalArgumentException | InvalidKeySpecException | NoSuchAlgorithmException e) {
      throw new RuntimeException("Invalid key format", e);
    }
  }

  /*
   * Creates and signs a JWT.
   * claims-The claims to include.
   * email-The email for whom the token is created.
   * issuedAt-Token issue time.
   * expirationDate-Token expiration time.
   * keyId-The key ID for the signing key.
   * privateKey-The private key to sign the token.
   * returns A compact JWT string.
   */
  private String generateToken(Map<String, Object> claims, String email,
      Date issuedAt, Date expirationDate, String keyId, PrivateKey privateKey) {

    return Jwts.builder()
        .claims()
        .add(claims) // Add custom claims
        .subject(email) // Add subject
        .issuedAt(issuedAt) // Issue timestamp
        .expiration(expirationDate) // Expiration timestamp
        .and()
        .header()
        .keyId(keyId) // Include key ID in header
        .and()
        .signWith(privateKey) // Sign with private key
        .compact();
  }

}