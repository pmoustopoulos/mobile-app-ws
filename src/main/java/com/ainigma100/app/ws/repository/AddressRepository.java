package com.ainigma100.app.ws.repository;

import com.ainigma100.app.ws.entity.AddressEntity;
import com.ainigma100.app.ws.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity, String> {

    List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

}
