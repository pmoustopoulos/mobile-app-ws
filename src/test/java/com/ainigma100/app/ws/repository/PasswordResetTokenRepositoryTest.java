package com.ainigma100.app.ws.repository;

import com.ainigma100.app.ws.entity.PasswordResetTokenEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PasswordResetTokenRepositoryTest {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Test
    public void givenToken_whenFindByToken_thenReturnPasswordResetTokenEntity() {

        // given - precondition or setup
        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken("123abc");

        passwordResetTokenRepository.save(passwordResetTokenEntity);

        // when - action or behaviour that we are going to test
        PasswordResetTokenEntity recordFromDB = passwordResetTokenRepository.findByToken(passwordResetTokenEntity.getToken());

        // then - verify the output
        assertThat(recordFromDB).isNotNull();

    }

}