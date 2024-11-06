package com.schdlr.service;

import com.schdlr.model.ResponseObject;
import com.schdlr.model.User;
import com.schdlr.repo.UserManagmentRepo;

import org.springframework.stereotype.Service;

@Service
public class UserManagmentService {

    private UserManagmentRepo repo;
    
    public UserManagmentService(UserManagmentRepo repo){
        this.repo = repo;
    }

    public ResponseObject userSignUp(User user) {
        User u = repo.findById(user.getId()).orElse(new User(-1));
        if(u.getId() == -1){
            return ResponseObject.Unsuccesful;
        }else{
            return ResponseObject.Successful;
        }
    }
}
