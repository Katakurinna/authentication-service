package me.cerratolabs.smarttech.session.domain.service;

import lombok.extern.log4j.Log4j2;
import me.cerratolabs.smarttech.session.api.model.Account;
import me.cerratolabs.smarttech.session.api.model.AccountWithId;
import me.cerratolabs.smarttech.session.api.model.LoginAccount;
import me.cerratolabs.smarttech.session.domain.exceptions.BadArgumentException;
import me.cerratolabs.smarttech.session.domain.utils.MessageConstants;
import me.cerratolabs.smarttech.session.domain.utils.ParserFactory;
import me.cerratolabs.smarttech.session.infraestructure.model.AccountEntity;
import me.cerratolabs.smarttech.session.infraestructure.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

@Log4j2
@Service
public class LoginService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private CipherService cipherService;

    @Autowired
    private TokenService tokenService;

    public String login(LoginAccount account) throws AccountNotFoundException, InvalidKeySpecException, NoSuchAlgorithmException, BadArgumentException {
        AccountWithId acc = validateAuth(account);
        return tokenService.generateJwt(acc);
    }

    private AccountWithId validateAuth(LoginAccount account) throws AccountNotFoundException, InvalidKeySpecException, NoSuchAlgorithmException, BadArgumentException {
        log.debug("Validating account {}", account.getEmail());
        Optional<AccountEntity> accountOpt = repository.findByEmailOrUsername(account.getEmail(), account.getUsername());

        if (accountOpt.isEmpty()) {
            log.debug("Account not found while login {}", account.getEmail());
            throw new AccountNotFoundException(MessageConstants.ACCOUNT_NOT_FOUND);
        }

        AccountEntity accountEntity = accountOpt.get();
        String auth = cipherService.encrypt(account.getPassword(), accountEntity.getSalt());
        if (!auth.equals(accountEntity.getAuth())) {
            log.debug("Incorrect password during login {}", account.getEmail());
            throw new AccountNotFoundException(MessageConstants.ACCOUNT_NOT_FOUND);
        }
        log.debug("Account validated for login {}", account.getEmail());
        return ParserFactory.parseToAccountWithId(accountEntity);
    }

}