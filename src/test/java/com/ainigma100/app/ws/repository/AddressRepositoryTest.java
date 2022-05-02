package com.ainigma100.app.ws.repository;

import com.ainigma100.app.ws.entity.AddressEntity;
import com.ainigma100.app.ws.entity.UserEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity userEntity1;
    private UserEntity userEntity2;
    private AddressEntity addressEntity1;
    private AddressEntity addressEntity2;


    @BeforeEach
    public void setUp() {
        userEntity1 = UserEntity.builder()
                .firstName("Marco")
                .lastName("Polo")
                .email("mpolo@gmail.com")
                .encryptedPassword("abc123")
                .emailVerificationToken("vertoken123")
                .emailVerificationStatus(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userEntity2 = UserEntity.builder()
                .firstName("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .encryptedPassword("zzz123")
                .emailVerificationToken("vertoken777")
                .emailVerificationStatus(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        addressEntity1 = AddressEntity.builder()
                .city("Thessaloniki")
                .country("Greece")
                .streetName("Nikis 91")
                .postalCode("56431")
                .type("billing")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userDetails(userEntity1)
                .build();

        addressEntity2 = AddressEntity.builder()
                .city("Feres")
                .country("Greece")
                .streetName("Antigonis 10")
                .postalCode("68500")
                .type("billing")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userDetails(userEntity2)
                .build();

    }



    @Test
    public void givenUserEntity_whenFindAllByUserDetails_thenReturnListOfAddressEntity() {

        // given - precondition or setup
        userRepository.save(userEntity1);
        addressRepository.save(addressEntity1);

        // when - action or behaviour that we are going to test
        List<AddressEntity> addressEntityListFromDB = addressRepository.findAllByUserDetails(addressEntity1.getUserDetails());

        // then - verify the output
        assertThat(addressEntityListFromDB).isNotNull();

    }


    @Test
    public void givenUserIdAndAddressId_whenFindUserAddressById_thenReturnAddressEntity() {

        // given - precondition or setup
        userRepository.save(userEntity1);
        addressRepository.save(addressEntity1);

        // when - action or behaviour that we are going to test
        AddressEntity addressEntityFromDB = addressRepository.findUserAddressById(userEntity1.getId(), addressEntity1.getId());

        // then - verify the output
        assertThat(addressEntityFromDB).isNotNull();

    }


}