package com.schdlr.controller;

import com.schdlr.model.SchdlrUser;
import com.schdlr.model.UserInfoObject;
import com.schdlr.service.UserManagmentService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserManagmentController {

    private UserManagmentService service;

    public UserManagmentController(UserManagmentService service){
        this.service = service;
    }

    @PostMapping("/signup")
    public UserInfoObject userSignUp(@RequestBody SchdlrUser user){
       UserInfoObject UIO = service.userSignUp(user);
       return UIO;
    }

    @PostMapping("/signin")
    public  UserInfoObject userSignIn(@RequestBody SchdlrUser user){
       UserInfoObject UIO = service.userSignIn(user);
       return UIO;
    }
}
