package me.cerratolabs.smarttech.session;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.cerratolabs.smarttech.session.api.model.LoginAccount;
import me.cerratolabs.smarttech.session.api.model.NewAccount;
import me.cerratolabs.smarttech.session.api.model.SessionAccount;
import me.cerratolabs.smarttech.session.conf.ApplicationConf;
import me.cerratolabs.smarttech.session.conf.CertConf;
import me.cerratolabs.smarttech.session.domain.exceptions.BadArgumentException;
import me.cerratolabs.smarttech.session.domain.exceptions.InternalSessionException;
import me.cerratolabs.smarttech.session.domain.service.SessionService;
import me.cerratolabs.smarttech.session.domain.utils.MessageConstants;
import me.cerratolabs.smarttech.session.infraestructure.exceptions.AccountAlreadyExistException;
import me.cerratolabs.smarttech.session.infraestructure.repository.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LoginTests {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private CertConf certConf;

    @Autowired
    private AccountRepository repository;

    @Test
    public void loginAccount_withNullAccount_thenThrowException() {
        LoginAccount mockedAccount = null;
        BadArgumentException exception = assertThrows(BadArgumentException.class, () -> {
            sessionService.login(mockedAccount);
        });
        assertEquals(MessageConstants.ACCOUNT_NULL_ERROR, exception.getMessage());
    }

    @Test
    public void loginAccount_withNullEmailAndUsername_thenThrowException() {
        LoginAccount mockedAccount = new LoginAccount();
        BadArgumentException exception = assertThrows(BadArgumentException.class, () -> {
            sessionService.login(mockedAccount);
        });
        assertEquals(MessageConstants.EMAIL_OR_USERNAME_NULL_ERROR, exception.getMessage());
    }

    @Test
    public void loginAccount_withNullUsername_thenThrowException() {
        LoginAccount mockedAccount = new LoginAccount();
        mockedAccount.setEmail("example@example.com");
        mockedAccount.setPassword("auth2");
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            sessionService.login(mockedAccount);
        });
        assertEquals(MessageConstants.ACCOUNT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void loginAccount_withNullEmail_thenThrowException() {
        LoginAccount mockedAccount = new LoginAccount();
        mockedAccount.setUsername("example");
        mockedAccount.setPassword("auth2");
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            sessionService.login(mockedAccount);
        });
        assertEquals(MessageConstants.ACCOUNT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void loginAccount_withNullPassword_thenThrowException() {
        LoginAccount mockedAccount = new LoginAccount();
        mockedAccount.setEmail("example@example.com");
        mockedAccount.setUsername("example");
        BadArgumentException exception = assertThrows(BadArgumentException.class, () -> {
            sessionService.login(mockedAccount);
        });
        assertEquals(MessageConstants.PASSWORD_NULL_ERROR, exception.getMessage());
    }

    @Test
    public void loginAccount_withNotExistingAccount_thenThrowException() {
        LoginAccount mockedAccount = new LoginAccount();
        mockedAccount.setEmail("example@example.com");
        mockedAccount.setUsername("example2");
        mockedAccount.setPassword("auth2");
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            sessionService.login(mockedAccount);
        });
        assertEquals(MessageConstants.ACCOUNT_NOT_FOUND, exception.getMessage());

    }


    @Test
    public void loginAccount_withAllParamsGood_thenReturnValidToken() throws BadArgumentException, InternalSessionException, AccountAlreadyExistException, AccountNotFoundException {
        LoginAccount mockedAccount = new LoginAccount();
        mockedAccount.setEmail("newAcc@example.com");
        mockedAccount.setUsername("newAcc");
        mockedAccount.setPassword("newAcc");
        SessionAccount login = sessionService.login(mockedAccount);
        assertNotNull(login);
        assertNotNull(login.getToken());
        JWTVerifier verifier = JWT.require(certConf.obtainAlgorithmRs())
            .acceptLeeway(1)
            .build();
        DecodedJWT verify = verifier.verify(login.getToken());
        assertNotNull(verify);
    }

    @BeforeEach
    public void addAccount() throws BadArgumentException, AccountAlreadyExistException, InternalSessionException {
        NewAccount mockedNewAccount = new NewAccount();
        mockedNewAccount.setEmail("newAcc@example.com");
        mockedNewAccount.setUsername("newAcc");
        mockedNewAccount.setPassword("newAcc");
        sessionService.createNewAccount(mockedNewAccount);

    }

    @AfterEach
    public void removeAccounts() {
        repository.deleteAll();
    }
}