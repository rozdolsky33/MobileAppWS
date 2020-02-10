package com.arwest.developer.mobileapp.ws.io.repositories;

import com.arwest.developer.mobileapp.ws.io.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    RoleEntity findByName(String name);

}
