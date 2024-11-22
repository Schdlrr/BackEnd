package com.schdlr.controller;

import java.io.IOException;

import com.schdlr.model.SignedUpUser;
import com.schdlr.service.UserManagmentService;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> userSignUp(@RequestBody SignedUpUser user){
        return service.userSignUp(user);
    }

    @PostMapping("/signin")
    public  ResponseEntity<String> userSignIn(@RequestBody SignedUpUser user){
       return service.userSignIn(user);
    }
}
