package com.arwest.developer.mobileapp.ws.io.repositories;

import com.arwest.developer.mobileapp.ws.io.entity.AddressEntity;
import com.arwest.developer.mobileapp.ws.io.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    static boolean recordsCreated = false;


    @BeforeEach
    void setUp() throws Exception {

        if(!recordsCreated) createRecords();
    }

    @Test
    final void testGetVerifiedUsers(){

        Pageable pageableRequest = PageRequest.of(0,2);
        Page<UserEntity> pages = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
        assertNotNull(pages);

        List<UserEntity>userEntities = pages.getContent();
        assertNotNull(userEntities);
        assertTrue(userEntities.size() == 2);
    }

    @Test
    final void testFindUserByFirstName()
    {
        String firstName="John";
        List<UserEntity> users = userRepository.findUserByFirstName(firstName);
        assertNotNull(users);
        assertTrue(users.size() == 2);

        UserEntity user = users.get(0);
        assertTrue(user.getFirstName().equals(firstName));
    }
    @Test
    final void testFindUserByLastName()
    {
        String lastName="Whick";
        List<UserEntity> users = userRepository.findUserByLastName(lastName);
        assertNotNull(users);
        assertTrue(users.size() == 2);

        UserEntity user = users.get(0);
        assertTrue(user.getLastName().equals(lastName));
    }
    @Test
    final void testFindUsersByKeyword()
    {
        String keyword="hn";
        List<UserEntity> users = userRepository.findUsersByKeyword(keyword);
        assertNotNull(users);
        assertTrue(users.size() == 2);

        UserEntity user = users.get(0);
        assertTrue(
                user.getLastName().contains(keyword) ||
                        user.getFirstName().contains(keyword)
        );
    }
    @Test
    final void testFindUserFirstNameAndLastNameByKeyword()
    {
        String keyword="ohn";
        List<Object[]> users = userRepository.findUserFirstNameAndLastNameByKeyword(keyword);
        assertNotNull(users);
        assertTrue(users.size() == 2);

        Object[] user = users.get(0);

        assertTrue(user.length == 2);

        String userFirstName = String.valueOf(user[0]);
        String userLastName = String.valueOf(user[1]);

        assertNotNull(userFirstName);
        assertNotNull(userLastName);

        System.out.println("First name = " + userFirstName);
        System.out.println("Last name = " + userLastName);

    }

    @Test
    final void testUpdateUserEmailVerificationStatus()
    {
        boolean newEmailVerificationStatus = true;

        userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, "1a2b3c");

        UserEntity storedUserDetails = userRepository.findByUserId("1a2b3c");

        boolean storedEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();

        assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);

    }

    @Test
    final void testFindUserEntitiesByUserId(){

        String userId ="1a2b3c";

        UserEntity userEntity = userRepository.findUserEntitiesByUserId(userId);

        assertNotNull(userEntity);
        assertTrue(userEntity.getUserId().equals(userId));

    }

    @Test
    final void testGetUserEntitiesById(){

        String userId ="1a2b3c";
        List<Object[]> records = userRepository.getUserEntitiesById(userId);

        assertNotNull(records);

        Object[] userDerails = records.get(0);

        String firstName = String.valueOf(userDerails[0]);
        String lastName = String.valueOf(userDerails[1]);

        assertNotNull(firstName);
        assertNotNull(lastName);
    }

    @Test
    final void testUpdateUserEntityEmailVerificationStatus()
    {
        boolean newEmailVerificationStatus = false;

        userRepository.updateUserEntityEmailVerificationStatus(newEmailVerificationStatus, "1a2b3c");

        UserEntity storedUserDetails = userRepository.findByUserId("1a2b3c");

        boolean storedEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();

        assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);

    }

    private void createRecords()
    {
        // Prepare User Entity
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("John");
        userEntity.setLastName("Whick");
        userEntity.setUserId("1a2b3c");
//        userEntity.setPassword("123456789");
        userEntity.setEncryptedPassword("xxx");
        userEntity.setEmail("test1@test.com");
        userEntity.setEmailVerificationStatus(true);

        // Prepare User Addresses
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setType("shipping");
        addressEntity.setAddressId("ahgyt74hfy");
        addressEntity.setCity("Vancouver");
        addressEntity.setCountry("Canada");
        addressEntity.setPostalCode("ABCCDA");
        addressEntity.setStreetName("123 Street Address");

        List<AddressEntity> addresses = new ArrayList<>();
        addresses.add(addressEntity);

        userEntity.setAddresses(addresses);

        userRepository.save(userEntity);

        // Prepare User Entity
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setFirstName("John");
        userEntity2.setLastName("Whick");
        userEntity2.setUserId("1a2b3cddddd");
//        userEntity2.setPassword("123456789");
        userEntity2.setEncryptedPassword("xxx");
        userEntity2.setEmail("test2@test.com");
        userEntity2.setEmailVerificationStatus(true);

        // Prepare User Addresses
        AddressEntity addressEntity2 = new AddressEntity();
        addressEntity2.setType("shipping");
        addressEntity2.setAddressId("ahgyt74hfywwww");
        addressEntity2.setCity("Vancouver");
        addressEntity2.setCountry("Canada");
        addressEntity2.setPostalCode("ABCCDA");
        addressEntity2.setStreetName("123 Street Address");

        List<AddressEntity> addresses2 = new ArrayList<>();
        addresses2.add(addressEntity2);

        userEntity2.setAddresses(addresses2);

        userRepository.save(userEntity2);

        recordsCreated = true;

    }
}