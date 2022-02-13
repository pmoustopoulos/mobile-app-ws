package com.ainigma100.app.ws.repository;

import com.ainigma100.app.ws.entity.AddressEntity;
import com.ainigma100.app.ws.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity, String> {

    List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

    @Query("SELECT address " +
            "FROM AddressEntity address " +
            "INNER JOIN UserEntity user " +
            "    ON (user.id = address.userDetails.id) " +
            "WHERE user.id = :userId " +
            "AND address.id = :addressId ")
    AddressEntity findUserAddressById(
            @Param("userId") String userId,
            @Param("addressId") String addressId);
}
