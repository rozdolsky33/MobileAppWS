package com.arwest.developer.mobileapp.ws.service;

import com.arwest.developer.mobileapp.ws.shared.dto.AddressDTO;

import java.util.List;

public interface AddressService {

    List<AddressDTO> getAddresses(String userId);
    AddressDTO getAddress(String addressId);


}
