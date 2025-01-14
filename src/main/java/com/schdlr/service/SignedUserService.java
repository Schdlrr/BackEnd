package com.schdlr.service;

import com.schdlr.model.SignedUser;
import com.schdlr.repo.SignedUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

@Slf4j
@Service
public class SignedUserService{

    private SignedUserRepo repo;

    private BCryptPasswordEncoder encoder;

    public SignedUserService(SignedUserRepo repo , BCryptPasswordEncoder encoder){
        this.repo = repo;
        this.encoder = encoder;
    }

    public ResponseEntity<String> userSignUp(SignedUser user) {

         if(!isValidEmail(user.getEmail())){
            return new ResponseEntity<>("Non valid email format entered.Please change email",HttpStatus.METHOD_NOT_ALLOWED);
        }else if(usedEmail(user)){
            return new ResponseEntity<>("Email is already used by another user. Please try to sign up with another kind of contact info",HttpStatus.CONFLICT);
        }else{
            user.setPassword(encoder.encode(user.getPassword()));
            repo.save(user);
            String userName = user.getUserName();
			 log.info("User signed up : {}", userName);
            return new ResponseEntity<>(userName,HttpStatus.CREATED);
        }
        
        }

    public ResponseEntity<String> userSignIn(SignedUser user) {
        SignedUser dbUser;
        try{
        dbUser = repo.findByEmail(user.getEmail()).get();
        }catch(NoSuchElementException e){
            return new ResponseEntity<>("User not present in database", HttpStatus.BAD_REQUEST);
        }
        if (encoder.matches(user.getPassword(),dbUser.getPassword())) {
            String username = user.getUserName();
			log.info("User signed in : {}", username);
            return new ResponseEntity<>(username,HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The credentials that were used do not match to any user please try again",HttpStatus.UNAUTHORIZED);
        }
    }

    public boolean isValidEmail(String email){
        String combinedRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern =Pattern.compile(combinedRegex);
        return pattern.matcher(email).matches();
    }
    
    public boolean usedEmail(SignedUser user){
        return repo.findByEmail(user.getEmail()).isPresent();
    }

    }
