package com.ainigma100.app.ws.repository;

import com.ainigma100.app.ws.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findUserByEmail(String email);

}
