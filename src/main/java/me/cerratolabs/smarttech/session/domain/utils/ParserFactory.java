package me.cerratolabs.smarttech.session.domain.utils;

import me.cerratolabs.smarttech.session.api.model.Account;
import me.cerratolabs.smarttech.session.api.model.AccountWithId;
import me.cerratolabs.smarttech.session.api.model.SessionAccount;
import me.cerratolabs.smarttech.session.infraestructure.model.AccountEntity;

public class ParserFactory {
    public static Account parseToGenericAccount(AccountEntity accountEntity) {
        Account account = new Account();
        account.setEmail(accountEntity.getEmail());
        account.setUsername(accountEntity.getUsername());
        return account;
    }

    public static AccountWithId parseToAccountWithId(AccountEntity accountEntity) {
        AccountWithId account = new AccountWithId();
        account.setEmail(accountEntity.getEmail());
        account.setUsername(accountEntity.getUsername());
        account.setUserId(accountEntity.getId());
        return account;
    }

    public static SessionAccount generateSessionAccount(String token) {
        return new SessionAccount(token);
    }

}