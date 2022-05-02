package com.ainigma100.app.ws.repository;

import com.ainigma100.app.ws.entity.UserEntity;
import com.ainigma100.app.ws.model.request.UserSearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserEntity userEntity1;
    private UserEntity userEntity2;

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
    }


    @Test
    public void givenEmail_whenFindByEmail_thenReturnUserEntity() {

        // given - precondition or setup
        userRepository.save(userEntity1);

        // when - action or behaviour that we are going to test
        UserEntity userEntityFromDB = userRepository.findByEmail(userEntity1.getEmail());

        // then - verify the output
        assertThat(userEntity1.getEmail()).isNotNull();
        assertThat(userEntityFromDB).isNotNull();

    }


    @Test
    public void givenUserSearchCriteria_whenGetUsersUsingPagination_thenReturnPageUserEntity() {

        // given - precondition or setup
        UserSearchCriteria criteria = UserSearchCriteria.builder()
                .firstName("Marco")
                .build();

        userRepository.save(userEntity1);

        // when - action or behaviour that we are going to test
        Page<UserEntity> userEntityPage = userRepository.getUsersUsingPagination(criteria, PageRequest.of(0, 10));

        // then - verify the output
        assertThat(userEntityPage).isNotNull();

    }


    @Test
    public void givenEmailVerificationToken_whenFindUserByEmailVerificationToken_thenReturnUserEntity() {

        // given - precondition or setup
        userRepository.save(userEntity1);

        // when - action or behaviour that we are going to test
        UserEntity userEntityFromDB = userRepository.findUserByEmailVerificationToken(userEntity1.getEmailVerificationToken());

        // then - verify the output
        assertThat(userEntity1.getEmailVerificationToken()).isNotNull();
        assertThat(userEntityFromDB).isNotNull();

    }



}