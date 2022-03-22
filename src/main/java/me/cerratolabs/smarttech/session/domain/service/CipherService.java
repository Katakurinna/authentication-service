package me.cerratolabs.smarttech.session.domain.service;

import com.sun.istack.NotNull;
import lombok.extern.log4j.Log4j2;
import me.cerratolabs.smarttech.session.domain.exceptions.BadArgumentException;
import me.cerratolabs.smarttech.session.domain.utils.MessageConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Log4j2
@Service
public class CipherService {

    public String encrypt(String auth, String userSalt) throws NoSuchAlgorithmException, InvalidKeySpecException, BadArgumentException {
        log.debug("Encrypting password");
        if(StringUtils.isEmpty(auth)) {
            throw new BadArgumentException(MessageConstants.PASSWORD_NULL_ERROR);
        }
        if(StringUtils.isEmpty(userSalt)) {
            throw new BadArgumentException(MessageConstants.SALT_NULL_ERROR);
        }
        KeySpec spec = new PBEKeySpec(auth.toCharArray(), userSalt.getBytes(StandardCharsets.UTF_8), 65536, 512);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        String encodedPassword = Base64.getEncoder().encodeToString(hash);

        log.debug("Password encrypted successfully");
        return encodedPassword;
    }

    public String generateNewSalt() {
        log.debug("Generating new salt");

        SecureRandom random = new SecureRandom();
        byte[] saltByteArray = new byte[32];
        random.nextBytes(saltByteArray);
        String salt = Base64.getEncoder().encodeToString(saltByteArray);

        log.debug("New salt generated");
        return salt;
    }

}