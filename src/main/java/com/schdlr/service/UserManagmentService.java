package com.schdlr.service;

import java.util.List;
import java.util.Optional;

import com.schdlr.model.ResponseObject;
import com.schdlr.model.SchdlrUser;
import com.schdlr.repo.UserManagmentRepo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserManagmentService {

    private UserManagmentRepo repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    
    public UserManagmentService(UserManagmentRepo repo){
        this.repo = repo;
    }

    public ResponseObject userSignUp(SchdlrUser user) {
        if (repo.findByUserName(user.getUserName()).isPresent()) {
            return ResponseObject.Unsuccessful;
        }
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        return ResponseObject.Successful;
        }

    public List<SchdlrUser> getUsers() {
        return repo.findAll();
    }

    public ResponseObject userSignIn(SchdlrUser user) {
    Optional<SchdlrUser> existingUser = repo.findByUserName(user.getUserName());
        if (existingUser.isPresent() && encoder.matches(user.getPassword(), existingUser.get().getPassword())) {
            return ResponseObject.Successful;
        } else {
            return ResponseObject.Unsuccessful;
        }
    }
    }
