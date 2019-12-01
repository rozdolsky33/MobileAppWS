package com.arwest.developer.mobileapp.ws.ui.controller;


import com.arwest.developer.mobileapp.ws.service.UserService;
import com.arwest.developer.mobileapp.ws.shared.dto.UserDto;
import com.arwest.developer.mobileapp.ws.ui.model.request.UserDetailsRequestModel;
import com.arwest.developer.mobileapp.ws.ui.model.response.UserRest;
import org.springframework.beans.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping
    public String getUser(){
        return "get user was called";
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails){

        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);  // incoming request populates dto object

        UserDto createUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createUser, returnValue);

        return returnValue ;
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
