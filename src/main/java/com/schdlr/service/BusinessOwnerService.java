package com.schdlr.service;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import com.schdlr.model.BusinessOwner;
import com.schdlr.repo.BusinessOwnerRepo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BusinessOwnerService {

    private BusinessOwnerRepo BOrepo;

    private BCryptPasswordEncoder encoder;

    public BusinessOwnerService (BusinessOwnerRepo BOrepo, BCryptPasswordEncoder encoder){
        this.BOrepo = BOrepo;
        this.encoder = encoder;
    }


    public ResponseEntity<String> userSignUp(BusinessOwner BO) {
         if(!isValidEmail(BO)){
            return new ResponseEntity<>("Non valid email format entered.Please change email",HttpStatus.METHOD_NOT_ALLOWED);
        }else if(usedEmail(BO)){
            return new ResponseEntity<>("Email is already used by another user. Please try to sign up with another kind of contact info",HttpStatus.CONFLICT);
        }else{
            BO.setPassword(encoder.encode(BO.getPassword()));
            BOrepo.save(BO);
            String userName = BO.getUserName();
            log.info("Business signed up : " + userName);
            return new ResponseEntity<>(userName,HttpStatus.CREATED);
        }
    }

    public ResponseEntity<String> userSignIn(BusinessOwner BO) {
        boolean usedEmail = usedEmail(BO);
        BusinessOwner dbUser;
        try{
        dbUser = BOrepo.findByEmail(BO.getEmail()).get();
        }catch(NoSuchElementException e){
            return new ResponseEntity<>("User not present in database", HttpStatus.BAD_REQUEST);
        }
        if (usedEmail && encoder.matches(BO.getPassword(),dbUser.getPassword())) {
            String username = BO.getUserName();
            log.info("Business Owner signed in : " + username);
            return new ResponseEntity<>(username,HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The credentials that were used do not match to any user please try again",HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     * Checks if the email of a user is a valid email
     * SignedUpUser - parameter that contains the email of the user being checked
     * returns true if it's valid false if not
     */
    public boolean isValidEmail(BusinessOwner BO){
        // Email validation regex pattern
        String combinedRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern =Pattern.compile(combinedRegex);
        return pattern.matcher(BO.getEmail()).matches();
    }

    /*
     * Checks if the email of a user has ever been used
     * SignedUpUser - parameter that contains the email of the user being checked
     * returns true if it's used false if not
     */
    public boolean usedEmail(BusinessOwner BO){
        return BOrepo.findByEmail(BO.getEmail()).isPresent();
    }

}
