package com.schdlr.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
 * PasswordEncodeUtil handles the creation of a BCryptPasswordEncoder instance
 * that is used to encrypt the passwords
 * 
 * Responsibilities:
 * - Create the password encoder.
 */
public class PasswordEncoderUtil {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(12);

    /*
     * Method for returning the instance of BCryptPasswordEncoder
     * returns BCryptPasswordEncoder
     */
    public static BCryptPasswordEncoder getInstance() {
        return ENCODER;
    }
}
