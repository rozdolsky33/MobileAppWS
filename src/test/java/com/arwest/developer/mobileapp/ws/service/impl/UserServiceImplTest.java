package com.arwest.developer.mobileapp.ws.service.impl;

import com.arwest.developer.mobileapp.ws.exceptions.UserServiceException;
import com.arwest.developer.mobileapp.ws.io.entity.AddressEntity;
import com.arwest.developer.mobileapp.ws.io.entity.UserEntity;
import com.arwest.developer.mobileapp.ws.io.repositories.UserRepository;
import com.arwest.developer.mobileapp.ws.shared.AmazonSES;
import com.arwest.developer.mobileapp.ws.shared.Utils;
import com.arwest.developer.mobileapp.ws.shared.dto.AddressDTO;
import com.arwest.developer.mobileapp.ws.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    AmazonSES amazonSES;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    String userId = "87ghjk989ddf";
    String encryptedPassword = "ghefjgr34895895t9hegdDDS54g";

    UserEntity userEntity;

    @BeforeEach
   final void setUp() {
        MockitoAnnotations.initMocks(this);
        //Stub Object
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("John");
        userEntity.setLastName("Wick");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setEmail("tets@test.com");
        userEntity.setEmailVerificationToken("utrif004rtgfjd44345434re");
        userEntity.setAddresses(getAddressesEntity());
    }

    @Test
    final void testGetUser() {

        when(userRepository.findUserByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUser("test@test.com");

        assertNotNull(userDto);
        assertEquals("John", userDto.getFirstName());
    }

    @Test
    final void testGetUser_UsernameNotFoundException(){

        when(userRepository.findUserByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                ()->{
                    userService.getUser("test@test.com");

        });
    }

    @Test
    final void testCreateUser_CreateUserServiceException(){

        when(userRepository.findUserByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = new UserDto();
        userDto.setAddresses(getAddressesDto());
        userDto.setFirstName("John");
        userDto.setLastName("Wick");
        userDto.setPassword("123456789");
        userDto.setEmail("test@test.com");

        assertThrows(UserServiceException.class,

                ()->{
                    userService.createUser(userDto);
                });
    }

    @Test
    final void testCreateUser(){

        when(userRepository.findUserByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("fghjkjhg9893");
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDto.class));

        UserDto userDto = new UserDto();
        userDto.setAddresses(getAddressesDto());
        userDto.setFirstName("John");
        userDto.setLastName("Wick");
        userDto.setPassword("123456789");
        userDto.setEmail("test@test.com");

        UserDto storedUserDetails = userService.createUser(userDto);
        assertNotNull(storedUserDetails);
        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
        assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
        assertNotNull(storedUserDetails.getUserId());
        assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
        verify(utils, times(storedUserDetails.getAddresses().size())).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("123456789");
        verify(userRepository, times(1)).save(any(UserEntity.class));

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

    private List<AddressEntity>getAddressesEntity(){

        List<AddressDTO> addresses = getAddressesDto();

        Type listType = new TypeToken<List<AddressEntity>>(){}.getType();

        return new ModelMapper().map(addresses, listType);

    }

}