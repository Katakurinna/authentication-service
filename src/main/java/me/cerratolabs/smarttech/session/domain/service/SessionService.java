package me.cerratolabs.smarttech.session.domain.service;

import lombok.extern.log4j.Log4j2;
import me.cerratolabs.smarttech.session.api.model.LoginAccount;
import me.cerratolabs.smarttech.session.api.model.NewAccount;
import me.cerratolabs.smarttech.session.api.model.SessionAccount;
import me.cerratolabs.smarttech.session.domain.exceptions.BadArgumentException;
import me.cerratolabs.smarttech.session.domain.exceptions.InternalSessionException;
import me.cerratolabs.smarttech.session.domain.utils.MessageConstants;
import me.cerratolabs.smarttech.session.domain.utils.ParserFactory;
import me.cerratolabs.smarttech.session.infraestructure.exceptions.AccountAlreadyExistException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Log4j2
@Service
public class SessionService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private LoginService loginService;

    public void createNewAccount(NewAccount account) throws AccountAlreadyExistException, InternalSessionException, BadArgumentException {
        validateNewAccountData(account);
        log.debug("Creating new account {}", account.getEmail());
        try {
            accountService.createNewAccount(account);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            log.error("Cipher error while trying to create a new account.", e);
            throw new InternalSessionException(MessageConstants.GENERIC_CREATING_CIPHER_ERROR, e);
        }
        log.debug("Created new account {}", account.getEmail());
    }

    public SessionAccount login(LoginAccount account) throws AccountNotFoundException, InternalSessionException, BadArgumentException {
        validateLoginAccountData(account);
        log.debug("Login in account {}", account.getEmail());
        try {
            String token = loginService.login(account);

            log.debug("Logged and generated token successfully in account {}", account.getEmail());
            return ParserFactory.generateSessionAccount(token);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            log.error("Cipher error while trying to create a new account.", e);
            throw new InternalSessionException(MessageConstants.GENERIC_LOGIN_CIPHER_ERROR, e);
        }

    }

    private void validateLoginAccountData(LoginAccount account) throws BadArgumentException {
        if (account == null) {
            throw new BadArgumentException(MessageConstants.ACCOUNT_NULL_ERROR);
        }
        if (StringUtils.isEmpty(account.getEmail()) && StringUtils.isEmpty(account.getUsername())) {
            throw new BadArgumentException(MessageConstants.EMAIL_OR_USERNAME_NULL_ERROR);
        }
        if (StringUtils.isEmpty(account.getPassword())) {
            throw new BadArgumentException(MessageConstants.PASSWORD_NULL_ERROR);
        }
    }

    private void validateNewAccountData(NewAccount account) throws BadArgumentException {
        if (account == null) {
            throw new BadArgumentException(MessageConstants.ACCOUNT_NULL_ERROR);
        }
        if (StringUtils.isEmpty(account.getEmail())) {
            throw new BadArgumentException(MessageConstants.EMAIL_NULL_ERROR);
        }
        if (StringUtils.isEmpty(account.getUsername())) {
            throw new BadArgumentException(MessageConstants.USERNAME_NULL_ERROR);
        }
        if (StringUtils.isEmpty(account.getPassword())) {
            throw new BadArgumentException(MessageConstants.PASSWORD_NULL_ERROR);
        }
    }

}