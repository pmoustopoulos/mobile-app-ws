package com.ainigma100.app.ws.security;

import com.ainigma100.app.ws.entity.AuthorityEntity;
import com.ainigma100.app.ws.entity.RoleEntity;
import com.ainigma100.app.ws.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;


public class UserPrincipal implements UserDetails {

    private UserEntity userEntity;

    // se want setter and getter only for this property
    @Getter @Setter
    private String id;

    public UserPrincipal(UserEntity userEntity) {
        this.userEntity = userEntity;
        this.id = userEntity.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new HashSet<>();
        Collection<AuthorityEntity> authorityEntities = new HashSet<>();

        // Get user Roles
        Collection<RoleEntity> roles = userEntity.getRoles();

        if(roles == null) return authorities;

        // we want to support roles and authorities
        roles.forEach((role) -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            authorityEntities.addAll(role.getAuthorities());
        });

        // now we will have both roles and authority names
        authorityEntities.forEach((authorityEntity) -> {
            authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()));
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.userEntity.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return this.userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.userEntity.getEmailVerificationStatus();
    }

}
