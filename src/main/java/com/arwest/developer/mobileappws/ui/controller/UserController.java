package com.arwest.developer.mobileappws.ui.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {


    @GetMapping
    public String getUser(){
        return "get uer was called";
    }
    @PostMapping
    public String createUser(){
        return "create user was called";
    }
    @PutMapping
    public String updateUser(){
        return "update User was called";
    }
    @DeleteMapping
    public String deleteUser(){
        return "delete user was called";
    }
}
