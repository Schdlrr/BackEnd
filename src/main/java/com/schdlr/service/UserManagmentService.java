package com.schdlr.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.schdlr.model.ResponseObject;
import com.schdlr.model.SchdlrUser;
import com.schdlr.model.UserInfoObject;
import com.schdlr.repo.UserManagmentRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserManagmentService {

    private UserManagmentRepo repo; 

    String combinedRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}" + "|" + "\\+383 (045|044)\\d{6}";

    Pattern pattern =Pattern.compile(combinedRegex);

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    
    public UserManagmentService(UserManagmentRepo repo){
        this.repo = repo;
    }

    public UserInfoObject userSignUp(SchdlrUser user) {
        if(repo.findByUserName(user.getUserName()) != null || 
        !pattern.matcher(user.getContactInfo()).matches()){
            return new UserInfoObject(ResponseObject.Unsuccessful, "User already found in database");
        }else{
            return new UserInfoObject(ResponseObject.Successful, user.getUserName());
        }
        }

    public List<SchdlrUser> getUsers() {
        return repo.findAll();
    }

    public UserInfoObject userSignIn(SchdlrUser user) {
    Optional<SchdlrUser> existingUser = repo.findByUserName(user.getUserName());
        if (existingUser.isPresent() && encoder.matches(user.getPassword(), existingUser.get().getPassword())) {
            return new UserInfoObject(ResponseObject.Successful, user.getUserName());
        } else {
            return new UserInfoObject(ResponseObject.Unsuccessful, "Login credentials do not match any user"
            + ",Try again");
        }
    }
    }
