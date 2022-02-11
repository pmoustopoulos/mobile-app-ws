package com.ainigma100.app.ws.repository;

import com.ainigma100.app.ws.entity.UserEntity;
import com.ainigma100.app.ws.model.request.UserSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity getByUserId(String id);

    @Query(value = "SELECT u FROM UserEntity u " +
            "WHERE ( :#{#criteria.firstName} IS NULL OR u.firstName LIKE :#{#criteria.firstName}% ) " +
            "AND ( :#{#criteria.lastName} IS NULL OR u.lastName LIKE :#{#criteria.lastName}% ) " +
            "AND ( :#{#criteria.email} IS NULL OR u.email LIKE :#{#criteria.email}% ) ")
    Page<UserEntity> getUsersUsingPagination(
            @Param("criteria") UserSearchCriteria criteria,
            Pageable pageable);
}
