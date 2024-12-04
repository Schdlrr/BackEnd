package com.schdlr.util;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenAndCookiesUtil {

    public String extractTokenFromCookies(HttpServletRequest request, String cookieName) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();  // Return the token if the cookie is found
            }
        }
    }
    return null;
}


    public void addCookie(HttpServletResponse response, String name, String value, int maxAge, boolean httpOnly, boolean secure, String sameSite) {
        StringBuilder cookie = new StringBuilder();
        cookie.append(name).append("=").append(value).append("; Path=/; Max-Age=").append(maxAge);
        
        if (httpOnly) {
            cookie.append("; HttpOnly");
        }
        if (secure) {
            cookie.append("; Secure");
        }
        if (sameSite != null) {
            cookie.append("; SameSite=").append(sameSite);
        }
    
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
