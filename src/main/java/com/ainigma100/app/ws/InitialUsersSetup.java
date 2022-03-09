package com.ainigma100.app.ws;

import com.ainigma100.app.ws.entity.AuthorityEntity;
import com.ainigma100.app.ws.entity.RoleEntity;
import com.ainigma100.app.ws.entity.UserEntity;
import com.ainigma100.app.ws.repository.AuthorityRepository;
import com.ainigma100.app.ws.repository.RoleRepository;
import com.ainigma100.app.ws.repository.UserRepository;
import com.ainigma100.app.ws.utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


@Component
public class InitialUsersSetup {

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("From Application ready event...");

        AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
        AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
        AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

        createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority, writeAuthority));
        RoleEntity roleAdmin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority, writeAuthority, deleteAuthority));

        if(roleAdmin == null) return;

        UserEntity adminUser = new UserEntity();
        adminUser.setFirstName("Petros");
        adminUser.setLastName("Admin");
        adminUser.setEmail("admin@test.com");
        adminUser.setEmailVerificationStatus(true);
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("12345"));
        adminUser.setRoles(Arrays.asList(roleAdmin));

        UserEntity storedUserDetails = userRepository.findByEmail("admin@test.com");
        if (storedUserDetails == null) {
            userRepository.save(adminUser);
        }
    }

    @Transactional
    public AuthorityEntity createAuthority(String name) {

        AuthorityEntity authority = authorityRepository.findByName(name);
        if (authority == null) {
            authority = new AuthorityEntity(null, name, new ArrayList<>());
            authorityRepository.save(authority);
        }
        return authority;
    }

    @Transactional
    public RoleEntity createRole(
            String name, Collection<AuthorityEntity> authorities) {

        RoleEntity role = roleRepository.findByName(name);
        if (role == null) {
            role = new RoleEntity(null, name, new ArrayList<>(), new ArrayList<>());
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }

}
