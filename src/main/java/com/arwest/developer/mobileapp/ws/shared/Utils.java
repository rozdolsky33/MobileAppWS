
package com.arwest.developer.mobileapp.ws.shared;

import com.arwest.developer.mobileapp.ws.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Service
public class Utils {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateUserId(int length){
        return generatedRandomString(length);
    }
    public String generateAddressId(int length){
        return generatedRandomString(length);
    }

    private String generatedRandomString(int length){
        StringBuilder returnValue = new StringBuilder(length);

        for (int i = 0; i < length ; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));

        }
        return new String(returnValue);
    }

    public static boolean hasTokenExpired (String token){

        boolean returnValue = false;


        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(SecurityConstants.getTokenSecret())
                    .parseClaimsJws(token).getBody();

            Date tokenExpirationDate = claims.getExpiration();
            Date todayDate = new Date();

            returnValue = tokenExpirationDate.before(todayDate);
        }catch (ExpiredJwtException ex){
            return true;
        }
        return returnValue;
    }
    public String generateEmailVerificationToken(String userId){
        String token = Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret()).compact();
        return token;
    }
    /*
   TODO: Create a separate function that generate uniq token and takes two @params that takes userId and expirationDate
     */
    public String generatePasswordResetToken (String userId){
        String token = Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret()).compact();
        return token;
    }



}

