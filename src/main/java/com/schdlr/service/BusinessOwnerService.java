package com.schdlr.service;

import com.schdlr.model.BusinessOwner;
import com.schdlr.repo.BusinessOwnerRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

@Slf4j
@Service
public class BusinessOwnerService {

    private BusinessOwnerRepo BOrepo;

    private BCryptPasswordEncoder encoder;

    public BusinessOwnerService(BusinessOwnerRepo BOrepo, BCryptPasswordEncoder encoder) {
        this.BOrepo = BOrepo;
        this.encoder = encoder;
    }


    public ResponseEntity<String> signUp(BusinessOwner BO) {
        if (!isValidEmail(BO.getEmail())) {
            return new ResponseEntity<>("Non valid email format entered.Please change email", HttpStatus.METHOD_NOT_ALLOWED);
        } else if (isUsedEmail(BO.getEmail())) {
            return new ResponseEntity<>("Email is already used by another user. Please try to sign up with another kind of contact info", HttpStatus.CONFLICT);
        } else {
            BO.setPassword(encoder.encode(BO.getPassword()));
            BOrepo.save(BO);
            String userName = BO.getUserName();
            log.info("Business signed up : {}", userName);
            return new ResponseEntity<>(userName, HttpStatus.CREATED);
        }
    }

    public ResponseEntity<String> signIn(String username, String email,String password) {
        BusinessOwner dbUser;
        try {
            dbUser = BOrepo.findByEmail(email).get();
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("User not present in database", HttpStatus.BAD_REQUEST);
        }
        if (encoder.matches(password, dbUser.getPassword())) {
            log.info("Business Owner signed in : {}", username);
            return new ResponseEntity<>(username, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The credentials that were used do not match to any user please try again", HttpStatus.UNAUTHORIZED);
        }
    }

    public boolean isValidEmail(String email) {
        String combinedRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(combinedRegex);
        return pattern.matcher(email).matches();
    }

    public boolean isUsedEmail(String email) {
        return BOrepo.findByEmail(email).isPresent();
    }

}
