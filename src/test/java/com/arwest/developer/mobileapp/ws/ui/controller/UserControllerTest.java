package com.arwest.developer.mobileapp.ws.ui.controller;

import com.arwest.developer.mobileapp.ws.service.impl.UserServiceImpl;
import com.arwest.developer.mobileapp.ws.shared.dto.AddressDTO;
import com.arwest.developer.mobileapp.ws.shared.dto.UserDto;
import com.arwest.developer.mobileapp.ws.ui.model.response.UserRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.Resource;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserServiceImpl userService;

    UserDto userDto;

    final String USER_ID = "dsfweiofew121";


    @BeforeEach
   final void setUp() {
        MockitoAnnotations.initMocks(this);

        userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Wick");
        userDto.setEmail("test@test.com");
        userDto.setEmailVerificationStatus(Boolean.FALSE);
        userDto.setEmailVerificationToken(null);
        userDto.setUserId(USER_ID);
        userDto.setAddresses(getAddressesDto());
        userDto.setEncryptedPassword("ieifuh08dscs303");
    }
    private List<AddressDTO> getAddressesDto() {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setType("shipping");
        addressDTO.setCity("New York");
        addressDTO.setCountry("USA");
        addressDTO.setPostalCode("103593");
        addressDTO.setStreetName("123 Street main");

        AddressDTO billingAddressDto = new AddressDTO();
        billingAddressDto.setType("billing");
        billingAddressDto.setCity("New York");
        billingAddressDto.setCountry("USA");
        billingAddressDto.setPostalCode("103593");
        billingAddressDto.setStreetName("123 Street main");

        List<AddressDTO> addresses = new ArrayList<>();
        addresses.add(addressDTO);
        addresses.add(billingAddressDto);

        return addresses;
    }

    @Test
    final void testGetUser() {
        when(userService.getUserByUserId(anyString())).thenReturn(userDto);

        Resource<UserRest> userRest = userController.getUser(USER_ID);

        assertNotNull(userRest);
        assertEquals(USER_ID, userRest.getContent().getUserId());
        assertEquals(userDto.getFirstName(), userRest.getContent().getFirstName());
        assertTrue(userDto.getAddresses().size() == userRest.getContent().getAddresses().size());
    }
}