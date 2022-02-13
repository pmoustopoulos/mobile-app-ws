package com.ainigma100.app.ws.repository;

import com.ainigma100.app.ws.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    private UserEntity userEntity;
    // this method will be executed before each and every test inside this class
    @BeforeEach
    public void setUp() {
        userEntity = UserEntity.builder()
                .id("id")
                .firstName("Petros")
                .lastName("Tester")
                .email("ptester@gmail.com")
                .encryptedPassword("123")
                .emailVerificationToken("abc")
                .emailVerificationStatus(false)
                .build();
    }


    // JUnit test for
    @Test
    public void givenEmail_whenFindByEmail_thenReturnUserObject() {

        // given - precondition or setup
        userRepository.save(userEntity);

        // when - action or behaviour that we are going to test
        UserEntity userFromDb = userRepository.findByEmail(userEntity.getEmail());


        // then - verify the output
        assertThat(userFromDb).isNotNull();

    }


    // JUnit test for
    @Test
    public void givenUserObject_whenSave_thenReturnSavedUser() {

        // given - precondition or setup

        // when - action or behaviour that we are going to test
        UserEntity savedUser = userRepository.save(userEntity);

        // then - verify the output
        assertThat(savedUser).isNotNull();

    }

    // JUnit test for
    @Test
    public void givenUserObject_whenFindById_thenReturnUserObject() {

        // given - precondition or setup
        userRepository.save(userEntity);

        // when - action or behaviour that we are going to test
        UserEntity userFromDb = userRepository.findById(userEntity.getId()).get();

        // then - verify the output
        assertThat(userFromDb).isNotNull();

    }

    // JUnit test for
    @Test
    public void givenUserObject_whenUpdateUser_thenReturnUpdatedUserObject() {

        // given - precondition or setup
        userRepository.save(userEntity);

        // when - action or behaviour that we are going to test
        UserEntity userFromDb = userRepository.findById(userEntity.getId()).get();
        userFromDb.setFirstName("Marco");
        userFromDb.setLastName("Polo");

        UserEntity updatedUser = userRepository.save(userFromDb);

        // then - verify the output
        assertThat(updatedUser.getFirstName()).isEqualTo("Marco");
        assertThat(updatedUser.getLastName()).isEqualTo("Polo");

    }


    // JUnit test for
    @Test
    public void givenUserObject_whenDelete_thenRemoveUserObject() {

        // given - precondition or setup
        userRepository.save(userEntity);

        // when - action or behaviour that we are going to test
        userRepository.delete(userEntity);
        Optional<UserEntity> userEntityOptional = userRepository.findById(userEntity.getId());

        // then - verify the output
        assertThat(userEntityOptional).isEmpty();
    }

}