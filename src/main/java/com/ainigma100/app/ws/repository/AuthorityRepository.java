package com.ainigma100.app.ws.repository;

import com.ainigma100.app.ws.entity.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {

    AuthorityEntity findByName(String name);

}
