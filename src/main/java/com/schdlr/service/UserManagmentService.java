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

    String combinedRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{3}$|^\\+383(44|45)\\d{6}$";

    Pattern pattern =Pattern.compile(combinedRegex);

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    
    public UserManagmentService(UserManagmentRepo repo){
        this.repo = repo;
    }

    public UserInfoObject userSignUp(SchdlrUser user) {
        if(isExistingUser(user)){
            return new UserInfoObject(ResponseObject.Unsuccessful, "User with the same username already exists.Pick a different username");
        }else if(!isValidContactInfo(user)){
            return new UserInfoObject(ResponseObject.Unsuccessful, "Non valid contact info enterd.Please change contact info");
        }else if(usedContactInfo(user)){
            return new UserInfoObject(ResponseObject.Unsuccessful, "User with the same contact info already exists.Change contact info or login and choose to forget password");
        }else{
            user.setPassword(encoder.encode(user.getPassword()));
            repo.save(user);
            return new UserInfoObject(ResponseObject.Successful,user.getUserName());
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

    public boolean isExistingUser(SchdlrUser user){
        return repo.findByUserName(user.getUserName()).isPresent();
    }

    public boolean isValidContactInfo(SchdlrUser user){
        return pattern.matcher(user.getContactInfo()).matches();
    }

    public boolean usedContactInfo(SchdlrUser user){
        return repo.findByContactInfo(user.getContactInfo()).isPresent();
    }

    }
