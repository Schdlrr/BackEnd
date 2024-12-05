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
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import com.schdlr.model.TokenKey;
import com.schdlr.repo.TokenKeyRepo;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

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
  public String generateRefreshToken(String email) throws NoSuchAlgorithmException, InvalidKeySpecException {
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
  public String generateAccessToken(String email) throws NoSuchAlgorithmException, InvalidKeySpecException {
    Map<String, Object> claims = new HashMap<>();

    TokenKey tokenKey = getRandomActiveKey(); // Fetch an active key for signing
    PrivateKey privateKey = getPrivateKey(tokenKey); // Convert stored key to PrivateKey

    return generateToken(claims, email, new Date(System.currentTimeMillis()),
        new Date(System.currentTimeMillis() + 1000l * 60l * 10l), email, privateKey);

  }

  /*
   * Extracts the email from a token after verifying it.
   * token-The JWT token.
   * returns The email embedded in the token.
   */
  public String extractEmail(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
    try {
      return extractClaim(token, Claims::getSubject);
    } catch (JwtException e) {
      throw new IllegalArgumentException("Invalid token");
    }
  }

  /*
   * Extracts a specific claim from the token using a resolver function.
   * token-The JWT token.
   * claimResolver-A function to extract the desired claim.
   */
  private <T> T extractClaim(String token, Function<Claims, T> claimResolver)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    final Claims claims = extractAllClaims(token);
    return claimResolver.apply(claims);
  }


  /*
   * Attempts to validate and parse a token using all keys in the database.
   * token-The JWT token.
   * returns Claims extracted from a valid token.
   */
  private Claims extractAllClaims(String token) {
    List<TokenKey> keys = tokenKeyRepo.findAll(); // Retrieve all keys

    for (TokenKey tokenKey : keys) {
      try {
        // Decode the public key from the current TokenKey
        String publicKeyString = tokenKey.getPublicKey();
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        // Attempt token parsing
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


  /*
   * Authenticates a token by checking if it can be validated by the public key.
   * token-The JWT token.
   * returns True if token email can be validated.
   */
  public boolean authenticateToken(String token){
    try{
      if(!isTokenExpired(token)){
      final String email = extractEmail(token);
    }else {
      return false;
    }

    }catch(NoSuchAlgorithmException e){
      System.out.println("No such algorithm exists");
      return false;
    }catch(InvalidKeySpecException i){
      System.out.print("Key spec is invalid");
      return false;
    }
    return true;

  }

  // Method for future implementation
  private boolean isTokenExpired(String token) throws NoSuchAlgorithmException,
      InvalidKeySpecException {
    return extractExpiration(token).before(new Date());
  }
  // Method for future implementation
  private Date extractExpiration(String token) throws NoSuchAlgorithmException,
      InvalidKeySpecException {
    return extractClaim(token, Claims::getExpiration);
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
   * Converts a stored private key string to a PrivateKey instance.
   * tokenKey-The TokenKey containing the private key string.
   * returns A PrivateKey instance.
   */
  public String extractTokenFromCookies(HttpServletRequest request, String cookieName) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(cookieName)) {
          return cookie.getValue(); // Return the token if the cookie is found
        }
      }
    }
    return null; // Return null if the cookie is not found
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