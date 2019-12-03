package com.arwest.developer.mobileapp.ws.ui.controller;


import com.arwest.developer.mobileapp.ws.service.UserService;
import com.arwest.developer.mobileapp.ws.shared.dto.UserDto;
import com.arwest.developer.mobileapp.ws.ui.model.request.UserDetailsRequestModel;
import com.arwest.developer.mobileapp.ws.ui.model.response.UserRest;
import org.springframework.beans.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping(value = "/{id}",
        produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
        )
    public UserRest getUser(@PathVariable String id){

        UserRest returnValue = new UserRest();

        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
            )
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

        UserRest returnValue = new UserRest();

        if (userDetails.getFirstName().isEmpty()) throw new NullPointerException("The Object is null");

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);  // incoming request populates dto object

        UserDto createUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createUser, returnValue);

        return returnValue ;
    }
    @PutMapping(value = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails){

        UserRest returnValue = new UserRest();

        if (userDetails.getFirstName().isEmpty()) throw new NullPointerException("The Object is null");

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);  // incoming request populates dto object

        UserDto updatedUser = userService.updateUser(id,userDto);
        BeanUtils.copyProperties(updatedUser, returnValue);

        return returnValue;
    }

    @DeleteMapping(value = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public String deleteUser(){
        return "delete user was called";
    }
}
