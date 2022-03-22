package me.cerratolabs.smarttech.session;

import me.cerratolabs.smarttech.session.api.model.NewAccount;
import me.cerratolabs.smarttech.session.domain.exceptions.BadArgumentException;
import me.cerratolabs.smarttech.session.domain.exceptions.InternalSessionException;
import me.cerratolabs.smarttech.session.domain.service.SessionService;
import me.cerratolabs.smarttech.session.domain.utils.MessageConstants;
import me.cerratolabs.smarttech.session.infraestructure.exceptions.AccountAlreadyExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CreateAccountTests {

    @Autowired
    private SessionService sessionService;

    @Test
    public void createAccount_withNullAccount_thenThrowException() {
        NewAccount mockedAccount = null;
        BadArgumentException exception = assertThrows(BadArgumentException.class, () -> {
            sessionService.createNewAccount(mockedAccount);
        });
        assertEquals(MessageConstants.ACCOUNT_NULL_ERROR, exception.getMessage());
    }

    @Test
    public void createAccount_withNullEmail_thenThrowException() {
        NewAccount mockedAccount = new NewAccount();
        BadArgumentException exception = assertThrows(BadArgumentException.class, () -> {
            sessionService.createNewAccount(mockedAccount);
        });
        assertEquals(MessageConstants.EMAIL_NULL_ERROR, exception.getMessage());
    }

    @Test
    public void createAccount_withNullUsername_thenThrowException() {
        NewAccount mockedAccount = new NewAccount();
        mockedAccount.setEmail("example@example.com");
        BadArgumentException exception = assertThrows(BadArgumentException.class, () -> {
            sessionService.createNewAccount(mockedAccount);
        });
        assertEquals(MessageConstants.USERNAME_NULL_ERROR, exception.getMessage());
    }

    @Test
    public void createAccount_withNullPassword_thenThrowException() {
        NewAccount mockedAccount = new NewAccount();
        mockedAccount.setEmail("example@example.com");
        mockedAccount.setUsername("example");
        BadArgumentException exception = assertThrows(BadArgumentException.class, () -> {
            sessionService.createNewAccount(mockedAccount);
        });
        assertEquals(MessageConstants.PASSWORD_NULL_ERROR, exception.getMessage());
    }

    @Test
    public void createAccount_withExistingEmail_thenThrowException() {
        NewAccount mockedAccount = new NewAccount();
        mockedAccount.setEmail("example@example.com");
        mockedAccount.setUsername("example2");
        mockedAccount.setPassword("auth2");
        AccountAlreadyExistException exception = assertThrows(AccountAlreadyExistException.class, () -> {
            sessionService.createNewAccount(mockedAccount);
            sessionService.createNewAccount(mockedAccount);
        });
        assertEquals(MessageConstants.ACCOUNT_ALREADY_EXIST_ERROR, exception.getMessage());

    }

    @Test
    public void createAccount_withExistingUsername_thenThrowException()  {
        NewAccount mockedAccount = new NewAccount();
        mockedAccount.setEmail("example2@example.com");
        mockedAccount.setUsername("example");
        mockedAccount.setPassword("auth2");
        AccountAlreadyExistException exception = assertThrows(AccountAlreadyExistException.class, () -> {
            sessionService.createNewAccount(mockedAccount);
            sessionService.createNewAccount(mockedAccount);
        });
        assertEquals(MessageConstants.ACCOUNT_ALREADY_EXIST_ERROR, exception.getMessage());

    }

    @Test
    public void createAccount_withAllParamsGood_thenReturnOk()  {
        NewAccount mockedAccount = new NewAccount();
        mockedAccount.setEmail("new@example.com");
        mockedAccount.setUsername("new");
        mockedAccount.setPassword("new");
        AccountAlreadyExistException exception = assertThrows(AccountAlreadyExistException.class, () -> {
            sessionService.createNewAccount(mockedAccount);
            sessionService.createNewAccount(mockedAccount);
        });
        assertEquals(MessageConstants.ACCOUNT_ALREADY_EXIST_ERROR, exception.getMessage());

    }

}