package com.schdlr.service;

import com.schdlr.model.BusinessOwner;
import com.schdlr.model.SignedUser;
import com.schdlr.repo.BusinessOwnerRepo;
import com.schdlr.repo.SignedUserRepo;
import com.schdlr.util.TokenExtractionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class TokenManagmentService {

    private TokenExtractionUtil tokenExtractionUtil;

    private SignedUserRepo repo;

    private BusinessOwnerRepo BOrepo;

    public TokenManagmentService (SignedUserRepo repo , TokenExtractionUtil tokenExtractionUtil,
								  BusinessOwnerRepo BOrepo){
        this.repo = repo;
        this.tokenExtractionUtil = tokenExtractionUtil;
        this.BOrepo = BOrepo;
    }

    public ResponseEntity<String> verifyRefreshToken( HttpServletRequest request, HttpServletResponse response,String refreshToken) throws NoSuchAlgorithmException, InvalidKeySpecException{
        if(!tokenExtractionUtil.authenticateToken(refreshToken)){
            return new ResponseEntity<>("Refresh Token is invalid", HttpStatus.UNAUTHORIZED);
        }
        String email = tokenExtractionUtil.extractEmail(refreshToken);
        String role = tokenExtractionUtil.extractRole(refreshToken);
        if(role.equals("BusinessOwner")){
            BusinessOwner BO;
            
            try{
                BO = BOrepo.findByEmail(email).get();
                }catch(NoSuchElementException e) {
                    return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                }
                log.info("User verified");
                return new ResponseEntity<>(BO.getUserName(), HttpStatus.OK);
        }else{
            SignedUser user;
            try{
                user = repo.findByEmail(email).get();
                }catch(NoSuchElementException e) {
                    return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                }
                log.info("User verified");
                return new ResponseEntity<>(user.getUserName(), HttpStatus.OK);
        }
    }
}
