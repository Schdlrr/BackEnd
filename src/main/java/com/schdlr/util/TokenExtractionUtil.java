package com.schdlr.util;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import com.schdlr.model.TokenKey;
import com.schdlr.repo.TokenKeyRepo;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class TokenExtractionUtil {

  private final TokenKeyRepo tokenKeyRepo;

  private static List<TokenKey> keys;

  public TokenExtractionUtil(TokenKeyRepo tokenKeyRepo) {
    this.tokenKeyRepo = tokenKeyRepo;
    loadKeys();
  }

  private synchronized void loadKeys() {
    keys = tokenKeyRepo.findAll();
    System.out.println("Keys loaded: " + keys);
  }

  public synchronized void refreshKeys() {
    loadKeys();
  }

  public String extractEmail(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
    try {
      return extractClaim(token, Claims::getSubject);
    } catch (JwtException e) {
      throw new IllegalArgumentException("Invalid token");
    }
  }

  public String extractRole(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
    try{
    return extractClaim(token, claims -> claims.get("role", String.class));
    }catch (JwtException e){
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
    System.out.println("Keys loaded: " + keys);
    System.out.println("Token: " + token);

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
}
