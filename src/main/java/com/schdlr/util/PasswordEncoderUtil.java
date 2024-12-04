package com.schdlr.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(12);

    public static BCryptPasswordEncoder getInstance() {
        return ENCODER;
    }
}
