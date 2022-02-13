package com.ainigma100.app.ws.service;

import com.ainigma100.app.ws.dto.AddressDTO;

import java.util.List;

public interface AddressService {
    List<AddressDTO> getAddressesByUserId(String userId);
    AddressDTO getUserAddressById(String userId, String addressId);
}
