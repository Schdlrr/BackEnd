package com.schdlr.service;

import java.util.regex.Pattern;
import com.schdlr.model.SchdlrUser;
import com.schdlr.repo.UserManagmentRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserManagmentService {

    private UserManagmentRepo repo; 

    String combinedRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{3}$|^\\+383(44|45)\\d{6}$";

    Pattern pattern =Pattern.compile(combinedRegex);

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    
    public UserManagmentService(UserManagmentRepo repo){
        this.repo = repo;
    }

    public ResponseEntity<String> userSignUp(SchdlrUser user) {
        if(isExistingUser(user)){
            return new ResponseEntity<>("User with the same username is already registered",HttpStatus.NOT_ACCEPTABLE);
        }else if(!isValidContactInfo(user)){
            return new ResponseEntity<>("Non valid contact info enterd.Please change contact info",HttpStatus.METHOD_NOT_ALLOWED);
        }else if(usedContactInfo(user)){
            return new ResponseEntity<>("Contact info is already used by another user. Please try to sign up with another kind of contact info",HttpStatus.CONFLICT);
        }else{
            user.setPassword(encoder.encode(user.getPassword()));
            repo.save(user);
            return new ResponseEntity<>(user.getUserName(),HttpStatus.CREATED);
        }
        
        }

    public ResponseEntity<String> userSignIn(SchdlrUser user) {
        boolean isExistingUser = isExistingUser(user);
        if (isExistingUser && encoder.matches(user.getPassword(), getUserByUserName(user.getUserName()).getPassword())) {
            return new ResponseEntity<>(user.getUserName(),HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("The credentials that were used do not match to any user please try again",HttpStatus.UNAUTHORIZED);
        }
    }

    public boolean isExistingUser(SchdlrUser user){
        return repo.findByUserName(user.getUserName()).isPresent();
    }

    public boolean isValidContactInfo(SchdlrUser user){
        return pattern.matcher(user.getContactInfo()).matches();
    }

    public boolean usedContactInfo(SchdlrUser user){
        return repo.findByContactInfo(user.getContactInfo()).isPresent();
    }

    public SchdlrUser getUserByUserName(String userName){
        return repo.findByUserName(userName).get();
    }

    }
