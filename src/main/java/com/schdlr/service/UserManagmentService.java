package com.schdlr.service;

import java.util.regex.Pattern;
import com.schdlr.model.SignedUpUser;
import com.schdlr.repo.UserManagmentRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserManagmentService {

    private UserManagmentRepo repo; 
    //.[A-Za-z]{3}$|^\\+383(44|45)\\d{6}$

    String combinedRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]";

    Pattern pattern =Pattern.compile(combinedRegex);

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    
    public UserManagmentService(UserManagmentRepo repo){
        this.repo = repo;
    }

    public ResponseEntity<String> userSignUp(SignedUpUser user) {
         if(!isValidContactInfo(user)){
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
        if (usedEmail && encoder.matches(user.getPassword(), getUserByUserName(user.getUserName()).getPassword())) {
            return new ResponseEntity<>(user.getUserName(),HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("The credentials that were used do not match to any user please try again",HttpStatus.UNAUTHORIZED);
        }
    }


    public boolean isValidContactInfo(SignedUpUser user){
        return pattern.matcher(user.getEmail()).matches();
    }

    public boolean usedEmail(SignedUpUser user){
        return repo.findByContactInfo(user.getEmail()).isPresent();
    }

    public SignedUpUser getUserByUserName(String userName){
        return repo.findByUserName(userName).get();
    }

    }
