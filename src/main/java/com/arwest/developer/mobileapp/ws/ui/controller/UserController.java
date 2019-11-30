package com.arwest.developer.mobileapp.ws.ui.controller;


import com.arwest.developer.mobileapp.ws.ui.model.request.UserDetailsRequestModel;
import com.arwest.developer.mobileapp.ws.ui.model.response.UserRest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {


    @GetMapping
    public String getUser(){
        return "get uer was called";
    }
    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails){
        return null ;
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
