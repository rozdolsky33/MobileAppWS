package com.arwest.developer.mobileapp.ws.security;

import com.arwest.developer.mobileapp.ws.io.entity.AuthorityEntity;
import com.arwest.developer.mobileapp.ws.io.entity.RoleEntity;
import com.arwest.developer.mobileapp.ws.io.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {


    private static final long serialVersionUID = 9173359976625202331L;

    UserEntity userEntity;

    public UserPrincipal(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority>authorities = new ArrayList<>();

        //Get user Roles
        Collection<RoleEntity> roles = userEntity.getRoles();
        List<AuthorityEntity> authorityEntities = new ArrayList<>();

        if(roles == null) return authorities;

        roles.forEach((role) -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
                authorityEntities.addAll(role.getAuthorities()); // Get role from each entity and add to authorities
        });

        authorityEntities.forEach(authorityEntity -> {
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
        return userEntity.getEmail();
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
