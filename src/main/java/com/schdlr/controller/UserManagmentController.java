package com.schdlr.controller;

import java.util.List;

import com.schdlr.model.ResponseObject;
import com.schdlr.model.SchdlrUser;
import com.schdlr.service.UserManagmentService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserManagmentController {

    private UserManagmentService service;

    public UserManagmentController(UserManagmentService service){
        this.service = service;
    }

    @GetMapping
    public List<SchdlrUser> getUsers(){
        return service.getUsers();
    }

    @PostMapping("/signup")
    public ResponseObject userSignUp(@RequestBody SchdlrUser user){
       return service.userSignUp(user);
    }

    @PostMapping("/signin")
    public ResponseObject userSignIn(@RequestBody SchdlrUser user){
        return service.userSignIn(user);
    }
}
