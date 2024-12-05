package com.schdlr.service;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class TokenKeyGen {

    /*
     * Generates a new RSA key pair (public and private keys).
     * returns A KeyPair object containing the generated RSA keys.
     * throws NoSuchAlgorithmException If the RSA algorithm is not available.
     */
    public static KeyPair genKeyPair()throws NoSuchAlgorithmException{
        KeyPairGenerator KPG = KeyPairGenerator.getInstance("RSA"); // Initialize RSA KeyPairGenerator
        KPG.initialize(2048); // Set the key size to 2048 bits
        return KPG.generateKeyPair(); // Generate and return the KeyPair
    }

    /*
     * Encodes a Key object to a Base64 string representation.
     * key-The Key object to encode.
     * returns A Base64-encoded string of the key's bytes.
     */
    public static String encodeKeyToString(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded()); // Convert the key bytes to Base64 string
    }
}
