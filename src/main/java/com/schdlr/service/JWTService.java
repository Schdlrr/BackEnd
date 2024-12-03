  package com.schdlr.service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import com.schdlr.model.TokenKey;
import com.schdlr.repo.TokenKeyRepo;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JWTService {

  TokenKeyRepo tokenKeyRepo;

  public JWTService(TokenKeyRepo tokenKeyRepo){
    this.tokenKeyRepo = tokenKeyRepo;
  }

  public String generateRefreshToken (String username) throws NoSuchAlgorithmException,InvalidKeySpecException{
    Map<String, Object> claims = new HashMap<>();
    
    TokenKey tokenKey = getRandomActiveKey();
    PrivateKey privateKey = getPrivateKey(tokenKey);
    

    return Jwts.builder()
    .claims()
    .add(claims)
    .subject(username)
    .issuedAt(new Date(System.currentTimeMillis()))
    .expiration(new Date(System.currentTimeMillis() + 1000l * 60l 
    * 60l * 24l * 7l * 3l))
    .and()
    .header()
    .keyId(tokenKey.getKid().toString())
    .and()
    .signWith(privateKey)
    .compact();

  }

  

  public String generateAccessToken (String username) throws NoSuchAlgorithmException,InvalidKeySpecException{
    Map<String, Object> claims = new HashMap<>();
    
    TokenKey tokenKey = getRandomActiveKey();
    PrivateKey privateKey = getPrivateKey(tokenKey);

    return Jwts.builder()
    .claims()
    .add(claims)
    .subject(username)
    .issuedAt(new Date(System.currentTimeMillis()))
    .expiration(new Date(System.currentTimeMillis() + 1000l * 60l 
    * 30l))
    .and()
    .header()
    .keyId(tokenKey.getKid().toString())
    .and()
    .signWith(privateKey)
    .compact();

  }


  public String extractUsername(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
    try {
      return extractClaim(token, Claims::getSubject);
  } catch (JwtException e) {
      throw new IllegalArgumentException("Invalid token");
  }
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimResolver) throws NoSuchAlgorithmException, InvalidKeySpecException {
    final Claims claims = extractAllClaims(token);
    return claimResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    List<TokenKey> keys = tokenKeyRepo.findAll();
    
    for (TokenKey tokenKey : keys) {
        try {
            // Decode the public key from the current TokenKey
            String publicKeyString = tokenKey.getPublicKey();
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            return Jwts.parser()
                       .verifyWith(publicKey)
                       .build()
                       .parseSignedClaims(token)
                       .getPayload();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println("Invalid key format for key: " + tokenKey.getKid());
        } catch (JwtException e) {
            System.err.println("Invalid JWT for key: " + tokenKey.getKid());
        }
    }

    throw new IllegalArgumentException("Unable to verify token with any of the provided keys");
}

  public boolean authenticateToken(String token, UserDetails userDetails) throws NoSuchAlgorithmException, InvalidKeySpecException {
    final String name = extractUsername(token);
    return (name.equals(userDetails.getUsername()));
  }

 /*  private boolean isTokenExpired(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
    return extractClaim(token, Claims::getExpiration);
  } */

  public List<TokenKey> getTokenKeys(){
    return tokenKeyRepo.findAll();
  }


  public TokenKey getRandomActiveKey() {
    List<TokenKey> activeKeys = tokenKeyRepo.findAllActiveKeys();
    if (activeKeys.isEmpty()) {
      throw new IllegalStateException("No active keys available");
    }

    return activeKeys.get(ThreadLocalRandom.current().nextInt(activeKeys.size()));
  }
  public String extractTokenFromCookies(HttpServletRequest request, String cookieName) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();  // Return the token if the cookie is found
            }
        }
    }
    return null; // Return null if the cookie is not found
}

private PrivateKey getPrivateKey(TokenKey tokenKey){
  try {
    String privateKey = tokenKey.getPrivateKey();
    byte[]  keyBytes = Base64.getDecoder().decode(privateKey);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PrivateKey privateKey2 = keyFactory.generatePrivate(keySpec);
    return privateKey2;
  } catch (IllegalArgumentException | InvalidKeySpecException | NoSuchAlgorithmException e) {
    throw new RuntimeException("Invalid key format", e);
}
  }
  
}