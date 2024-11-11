package com.schdlr.controller;

import java.io.IOException;

import com.schdlr.model.SchdlrUser;
import com.schdlr.model.UserInfoObject;
import com.schdlr.service.UserManagmentService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserManagmentController {

    private UserManagmentService service;

    public UserManagmentController(UserManagmentService service){
        this.service = service;
    }

    @Hidden
    @GetMapping("/")
    public void redirect(HttpServletResponse response) throws IOException{
        response.sendRedirect("/swagger-ui/index.html");
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
