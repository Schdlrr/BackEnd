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

@Service
public class JWTService {

  TokenKeyRepo tokenKeyRepo;

  public JWTService(TokenKeyRepo tokenKeyRepo) {
    this.tokenKeyRepo = tokenKeyRepo;
  }

  public String generateRefreshToken(String email,String role) throws NoSuchAlgorithmException, InvalidKeySpecException {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", role);

    TokenKey tokenKey = getRandomActiveKey();
    PrivateKey privateKey = getPrivateKey(tokenKey);

    return generateToken(claims,email, new Date(System.currentTimeMillis()),
        new Date(System.currentTimeMillis() + 1000l * 60l * 60l * 24l * 7l * 3l), email, privateKey);
  }

  public String generateAccessToken(String email,String role) throws NoSuchAlgorithmException, InvalidKeySpecException {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", role);

    TokenKey tokenKey = getRandomActiveKey();
    PrivateKey privateKey = getPrivateKey(tokenKey);

    return generateToken(claims, email, new Date(System.currentTimeMillis()),
        new Date(System.currentTimeMillis() + 1000l * 60l * 10l), email, privateKey);

  }

  public TokenKey getRandomActiveKey() {
    List<TokenKey> activeKeys = tokenKeyRepo.findAllActiveKeys();
    if (activeKeys.isEmpty()) {
      throw new IllegalStateException("No active keys available");
    }

    return activeKeys.get(ThreadLocalRandom.current().nextInt(activeKeys.size()));
  }

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

  private String generateToken(Map<String, Object> claims, String email,
      Date issuedAt, Date expirationDate, String keyId, PrivateKey privateKey) {

    return Jwts.builder()
        .claims()
        .add(claims)
        .subject(email)
        .issuedAt(issuedAt)
        .expiration(expirationDate)
        .and()
        .header()
        .keyId(keyId)
        .and()
        .signWith(privateKey)
        .compact();
  }

}