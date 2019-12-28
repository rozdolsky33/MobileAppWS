package com.arwest.developer.mobileapp.ws.ui.controller;

import com.arwest.developer.mobileapp.ws.service.AddressService;
import com.arwest.developer.mobileapp.ws.service.UserService;
import com.arwest.developer.mobileapp.ws.shared.dto.AddressDTO;
import com.arwest.developer.mobileapp.ws.shared.dto.UserDto;
import com.arwest.developer.mobileapp.ws.ui.model.request.UserDetailsRequestModel;
import com.arwest.developer.mobileapp.ws.ui.model.response.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {


    @Autowired
    AddressService addressService;

    @Autowired
    private UserService userService;

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping(value = "/{id}",
        produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
                "application/hal+json"
    })
    public Resource<UserRest> getUser(@PathVariable String id){

        UserDto userDto = userService.getUserByUserId(id);
        ModelMapper modelMapper = new ModelMapper();

        UserRest addressesRestModel = modelMapper.map(userDto, UserRest.class);

        return new Resource<>(addressesRestModel);
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE
            })
    public  Resource<UserRest>createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        log.info("creating user = {}" + userDetails.getEmail());
        UserDto createdUser = userService.createUser(userDto);

        UserRest returnValue = modelMapper.map(createdUser, UserRest.class);

        return new Resource<>(returnValue);
    }
    @PutMapping(value = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE
    })
    public Resource<UserRest> updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails){

        UserRest returnValue = new UserRest();

        if (userDetails.getFirstName().isEmpty()) throw new NullPointerException("The Object is null");

        UserDto userDto = new UserDto();
        log.info("updating user = {}" + userDetails.toString());
        BeanUtils.copyProperties(userDetails, userDto);  // incoming request populates dto object

        UserDto updatedUser = userService.updateUser(id,userDto);
        BeanUtils.copyProperties(updatedUser, returnValue);

        return new Resource<>(returnValue);
    }

    @DeleteMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public OperationStatusModel deleteUser(@PathVariable String id){

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE
    })
    public  Resources<UserRest>getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "limit", defaultValue = "2") int limit){

        List<UserRest>addressesListRestModel = new ArrayList<>();
        List<UserDto>usersDto = userService.getUsers(page,limit);

        if (usersDto != null && !usersDto.isEmpty()){
            Type listType = new TypeToken<List<UserRest>>() {
            }.getType();
            addressesListRestModel = new ModelMapper().map(usersDto, listType);
        }

        return new  Resources(addressesListRestModel);
    }
    @GetMapping(value = "/{id}/addresses",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,
                    "application/hal+json"})
    public Resources<AddressesRest> getUserAddresses(@PathVariable String id){

        List<AddressesRest> addressesListRestModel = new ArrayList<>();
        List<AddressDTO> addressesDTO = addressService.getAddresses(id);

        if (addressesDTO != null && !addressesDTO.isEmpty()) {
            Type listType = new TypeToken<List<AddressesRest>>() {
            }.getType();
            addressesListRestModel = new ModelMapper().map(addressesDTO, listType);

            for (AddressesRest addressRest : addressesListRestModel) {
                Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(id, addressRest.getAddressId()))
                        .withSelfRel();
                addressRest.add(addressLink);

                Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
                addressRest.add(userLink);
            }
        }
        return new Resources<>(addressesListRestModel);
    }
    @GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE, "application/hal+json"})
    public Resource<AddressesRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {

        AddressDTO addressesDto = addressService.getAddress(addressId);

        ModelMapper modelMapper = new ModelMapper();

        Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
        Link userLink = linkTo(UserController.class).slash(userId).withRel("user");
        Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");

        AddressesRest addressesRestModel = modelMapper.map(addressesDto, AddressesRest.class);

        addressesRestModel.add(addressLink);
        addressesRestModel.add(userLink);
        addressesRestModel.add(addressesLink);

        return new Resource<>(addressesRestModel);
    }

    /*
    http://localhost:8080/mobile-app-ws/users/emil-verification?token=kjhjkjhgh
     */

    @GetMapping(value = "/email-verification", produces = {MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token){
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

        boolean isVerified = userService.verifyEmailToken(token);

        if(isVerified){
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }else{
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }
        return returnValue;
    }

}
