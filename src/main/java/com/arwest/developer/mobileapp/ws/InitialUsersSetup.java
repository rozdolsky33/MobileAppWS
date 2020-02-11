package com.arwest.developer.mobileapp.ws;

import com.arwest.developer.mobileapp.ws.io.entity.AuthorityEntity;
import com.arwest.developer.mobileapp.ws.io.entity.RoleEntity;
import com.arwest.developer.mobileapp.ws.io.entity.UserEntity;
import com.arwest.developer.mobileapp.ws.io.repositories.AuthorityRepository;
import com.arwest.developer.mobileapp.ws.io.repositories.RoleRepository;
import com.arwest.developer.mobileapp.ws.io.repositories.UserRepository;
import com.arwest.developer.mobileapp.ws.shared.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    Utils utils;

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event){

        AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
        AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
        AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

       RoleEntity roleUser = creatRole("ROLE_USER", Arrays.asList(readAuthority, writeAuthority));
       RoleEntity roleAdmin = creatRole("ADMIN_USER", Arrays.asList(readAuthority, writeAuthority, deleteAuthority));

       if (roleAdmin == null)return;

        UserEntity adminUser = new UserEntity();
        adminUser.setFirstName("Volodymyr");
        adminUser.setLastName("Rozdolsky");
        adminUser.setEmail("rrozdolsky86@@gmail.com");
        adminUser.setEmailVerificationStatus(true);
        adminUser.setUserId(utils.generateUserId(30));
        adminUser.setPassword("123456789");
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("12345678"));
        adminUser.setRoles(Arrays.asList(roleAdmin));

        userRepository.save(adminUser);

    }

    @Transactional
    private AuthorityEntity createAuthority(String name){

        AuthorityEntity authority = authorityRepository.findByName(name);
        if (authority == null){
            authority = new AuthorityEntity(name);
            authorityRepository.save(authority);
        }
        return authority;
    }

    @Transactional
    private RoleEntity creatRole(String name, Collection<AuthorityEntity> authorities){

        RoleEntity role = roleRepository.findByName(name);
        if (role == null){
            role = new RoleEntity(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }
}
