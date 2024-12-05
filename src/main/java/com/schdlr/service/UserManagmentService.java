package com.schdlr.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Pattern;
import com.schdlr.model.SignedUpUser;
import com.schdlr.repo.UserManagmentRepo;
import com.schdlr.util.PasswordEncoderUtil;
import com.schdlr.util.TokenAndCookiesUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserManagmentService {

    private UserManagmentRepo repo; 

    private TokenAndCookiesUtil tokenAndCookiesUtil;

    private final JWTService jwtService;

    // Password encoder instance
    private BCryptPasswordEncoder encoder = PasswordEncoderUtil.getInstance();
    

    /*
     * Constructor for injecting dependencies.
     * repo-Repository for user management.
     * tokenAndCookiesUtil-Utility for token and cookie operations.
     * jwtService Service-for handling JWT operations.
     */
    public UserManagmentService(UserManagmentRepo repo, TokenAndCookiesUtil tokenAndCookiesUtil
    ,JWTService jwtService){
        this.repo = repo;
        this.tokenAndCookiesUtil = tokenAndCookiesUtil;
        this.jwtService = jwtService;
    }

    /*
     * Verifies a refresh token extracted from cookies.
     * request-The HttpServletRequest containing the cookies.
     * response-The HttpServletResponse to send responses.
     * returns The username if the token is valid, or an error message otherwise.
     */
    public ResponseEntity<String> verify( HttpServletRequest request, HttpServletResponse response,String refreshToken) throws NoSuchAlgorithmException, InvalidKeySpecException{
        if(!jwtService.authenticateToken(refreshToken)){
            return new ResponseEntity<>("Refresh Token is invalid", HttpStatus.UNAUTHORIZED);
        }
        String email = jwtService.extractEmail(refreshToken);
        SignedUpUser user = repo.findByEmail(email).get();
        return new ResponseEntity<>(user.getUserName(), HttpStatus.OK);
    }

    public ResponseEntity<String> userSignUp(SignedUpUser user) {
         if(!isValidEmail(user)){
            return new ResponseEntity<>("Non valid email format entered.Please change email",HttpStatus.METHOD_NOT_ALLOWED);
        }else if(usedEmail(user)){
            return new ResponseEntity<>("Email is already used by another user. Please try to sign up with another kind of contact info",HttpStatus.CONFLICT);
        }else{
            user.setPassword(encoder.encode(user.getPassword()));
            repo.save(user);
            return new ResponseEntity<>(user.getUserName(),HttpStatus.CREATED);
        }
        
        }

    public ResponseEntity<String> userSignIn(SignedUpUser user) {
        boolean usedEmail = usedEmail(user);
        SignedUpUser dbUser = repo.findByEmail(user.getEmail()).get();
        if (usedEmail && encoder.matches(user.getPassword(),dbUser.getPassword())) {
            return new ResponseEntity<>(user.getUserName(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The credentials that were used do not match to any user please try again",HttpStatus.UNAUTHORIZED);
        }
    }


    public boolean isValidEmail(SignedUpUser user){
        // Email validation regex pattern
        String combinedRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern =Pattern.compile(combinedRegex);
        return pattern.matcher(user.getEmail()).matches();
    }

    public boolean usedEmail(SignedUpUser user){
        return repo.findByEmail(user.getEmail()).isPresent();
    }

    public SignedUpUser getUserByUserName(String userName){
        return repo.findByUserName(userName).get();
    }

    }
