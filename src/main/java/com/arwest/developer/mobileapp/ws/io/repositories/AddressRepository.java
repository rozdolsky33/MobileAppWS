package com.arwest.developer.mobileapp.ws.io.repositories;

import com.arwest.developer.mobileapp.ws.io.entity.AddressEntity;
import com.arwest.developer.mobileapp.ws.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {

    List<AddressEntity> findAllByUserDetails(UserEntity entity);  // address entity has to contain field name userDetails in order for this to work
                                                                     // Get queried by field name user details which hold an object with encapsulated userDetails

}
