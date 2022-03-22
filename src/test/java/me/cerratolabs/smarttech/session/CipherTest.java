package me.cerratolabs.smarttech.session;

import me.cerratolabs.smarttech.session.domain.exceptions.BadArgumentException;
import me.cerratolabs.smarttech.session.domain.service.CipherService;
import me.cerratolabs.smarttech.session.domain.utils.MessageConstants;
import me.cerratolabs.smarttech.session.infraestructure.model.AccountEntity;
import me.cerratolabs.smarttech.session.infraestructure.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CipherTest {

    @Autowired
    private CipherService cipherService;

    @Test
    public void cipherPassword_withNullSalt_thenThrowException() {
        BadArgumentException exception = assertThrows(BadArgumentException.class, () -> {
            cipherService.encrypt("juanJose", null);
        });
        assertEquals(MessageConstants.SALT_NULL_ERROR, exception.getMessage());

    }

    @Test
    public void cipherPassword_withNullPassword_thenThrowException() {
        BadArgumentException exception = assertThrows(BadArgumentException.class, () -> {
            cipherService.encrypt(null, "salwsedgjsdghds");
        });
        assertEquals(MessageConstants.PASSWORD_NULL_ERROR, exception.getMessage());
    }



    @Test
    public void cipherPassword_withGoodData_thenReturnEncryptedPassword() throws NoSuchAlgorithmException, BadArgumentException, InvalidKeySpecException {
        String salt = cipherService.generateNewSalt();
        String auth = cipherService.encrypt("auth", salt);
        String auth2 = cipherService.encrypt("auth", salt);
        // check encrypt two times dont return different result
        assertEquals(auth, auth2);
    }

}