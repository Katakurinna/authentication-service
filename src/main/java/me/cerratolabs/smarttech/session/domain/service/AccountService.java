package me.cerratolabs.smarttech.session.domain.service;

import lombok.extern.log4j.Log4j2;
import me.cerratolabs.smarttech.session.api.model.Account;
import me.cerratolabs.smarttech.session.api.model.NewAccount;
import me.cerratolabs.smarttech.session.domain.exceptions.BadArgumentException;
import me.cerratolabs.smarttech.session.domain.utils.MessageConstants;
import me.cerratolabs.smarttech.session.infraestructure.exceptions.AccountAlreadyExistException;
import me.cerratolabs.smarttech.session.infraestructure.model.AccountEntity;
import me.cerratolabs.smarttech.session.infraestructure.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Log4j2
@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private CipherService cipherService;

    public boolean existAccount(String email, String username) {
        return repository.findByEmailOrUsername(email, username).isPresent();
    }

    public boolean existAccount(Account acc) {
        return existAccount(acc.getEmail(), acc.getUsername());
    }

    public void createNewAccount(NewAccount account) throws AccountAlreadyExistException, InvalidKeySpecException, NoSuchAlgorithmException, BadArgumentException {
        if (existAccount(account.getEmail(), account.getUsername())) {
            log.debug("Account not found with email or username {}:{}", account.getEmail(), account.getUsername());
            throw new AccountAlreadyExistException(MessageConstants.ACCOUNT_ALREADY_EXIST_ERROR);
        }

        AccountEntity newEntity = createNewEntity(account);
        repository.save(newEntity);
    }

    private AccountEntity createNewEntity(NewAccount account) throws InvalidKeySpecException, NoSuchAlgorithmException, BadArgumentException {
        log.debug("Generating new account entity, {}", account.getEmail());
        AccountEntity entity = new AccountEntity();

        entity.setEmail(account.getEmail());
        entity.setUsername(account.getUsername());
        String salt = cipherService.generateNewSalt();
        String auth = cipherService.encrypt(account.getPassword(), salt);
        entity.setSalt(salt);
        entity.setAuth(auth);

        log.debug("Generated new account entity, {}", account.getEmail());
        return entity;
    }
}