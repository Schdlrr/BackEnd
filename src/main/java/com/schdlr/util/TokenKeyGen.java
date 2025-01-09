package com.schdlr.util;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class TokenKeyGen {

    public static KeyPair genKeyPair()throws NoSuchAlgorithmException{
        KeyPairGenerator KPG = KeyPairGenerator.getInstance("RSA");
        KPG.initialize(2048);
        return KPG.generateKeyPair();
    }

    public static String encodeKeyToString(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
