package com.schdlr.util;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/*
 * TokenAndCookiesUtil handles the creation and extraction of cookies
 * 
 * Responsibilities:
 * -Extract JWTs from cookies when give the name and request.
 * -Add cookies to the HttpServerletResponse response.
 * 
 * Annotations:
 * -@Component : Tells spring to make a bean of this class that will
 * be used as a dependency in others
 */
@Component
public class TokenAndCookiesUtil {

    /*
     * Method for extracting JWTs from the request cookies
     * request- The request done by the client to the server
     * cookieName- the name of the cookie that is going to be
     * extracted from the request by the server
     * returns String represantation of JWT
     */
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


    /*
     * Method for adding cookies with the appropriate tokens to the response
     * response- The request sent by the server to the client
     * name - the name of the cookie that is going to be sent
     * value - the value of the cookie
     * maxAge - how long the cookie is valid for
     * httpOnly - whether it's going to be httpOnly or not
     * secure - wheter it's going to be secured through https
     * sameSit - the same site policy
     */
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
