package com.ainigma100.app.ws.service;

import com.ainigma100.app.ws.dto.AddressDTO;
import com.ainigma100.app.ws.entity.AddressEntity;
import com.ainigma100.app.ws.entity.UserEntity;
import com.ainigma100.app.ws.repository.AddressRepository;
import com.ainigma100.app.ws.repository.UserRepository;
import com.ainigma100.app.ws.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final Utils utils;

    @Override
    public List<AddressDTO> getAddressesByUserId(String userId) {

        // first find the user or else return null
        UserEntity userEntity = userRepository.findById(userId).orElse(null);

        // get the list of addresses associated to the specific user
        List<AddressEntity> addressEntityList = addressRepository.findAllByUserDetails(userEntity);

        // map and return the results
        return utils.mapList(addressEntityList, AddressDTO.class);
    }

    @Override
    public AddressDTO getUserAddressById(String userId, String addressId) {

        AddressDTO returnValue = new AddressDTO();

        AddressEntity addressFromDb = addressRepository.findUserAddressById(userId, addressId);

        if (addressFromDb != null) {
            returnValue = utils.map(addressFromDb, AddressDTO.class);
        }

        return returnValue;
    }


}
