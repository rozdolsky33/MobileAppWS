package com.arwest.developer.mobileapp.ws.io.repositories;

import com.arwest.developer.mobileapp.ws.io.entity.PasswordResetTokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface PasswordRestTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long> {

    PasswordResetTokenEntity findByToken(String token);

}
