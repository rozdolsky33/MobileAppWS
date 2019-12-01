package com.arwest.developer.mobileapp.ws.service;

import com.arwest.developer.mobileapp.ws.shared.dto.UserDto;
import org.springframework.data.repository.CrudRepository;

public interface UserService {

    UserDto createUser(UserDto user);
}
