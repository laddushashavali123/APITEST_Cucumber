package br.com.experian.cucumber.integration.cucumber.common.utils;

import java.security.KeyPair;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Created by Kaio Gonzaga on 21/12/2017.
 */
public class JwtUtil {

    public String createJwt(String jsonToken) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.RS512, getKeyPair().getPrivate())
                .setPayload(jsonToken)
                .compact();
    }

    private static KeyPair getKeyPair() {
        KeyPair keyPair = new KeyStoreKeyFactory(
                new ClassPathResource("id_rsa.jks"), "Ser@saDigitalTransformation##".toCharArray())
                .getKeyPair("serasasecurity");
        return keyPair;
    }
}
