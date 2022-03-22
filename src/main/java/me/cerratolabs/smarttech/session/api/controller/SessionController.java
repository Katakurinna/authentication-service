package me.cerratolabs.smarttech.session.api.controller;

import me.cerratolabs.smarttech.session.api.model.LoginAccount;
import me.cerratolabs.smarttech.session.api.model.NewAccount;
import me.cerratolabs.smarttech.session.api.model.SessionAccount;
import me.cerratolabs.smarttech.session.domain.exceptions.BadArgumentException;
import me.cerratolabs.smarttech.session.domain.exceptions.InternalSessionException;
import me.cerratolabs.smarttech.session.domain.service.SessionService;
import me.cerratolabs.smarttech.session.infraestructure.exceptions.AccountAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/public/v1/account")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    /**
     * Create new account
     * @param request http request
     * @param data body
     * @return 200 OK
     * @throws InternalSessionException if some error occurred during account creating
     * @throws AccountAlreadyExistException if account already exist
     */
    @PostMapping("/new")
    public ResponseEntity<?> createNewAccount(HttpServletRequest request, @RequestBody NewAccount data) throws InternalSessionException, AccountAlreadyExistException, BadArgumentException {
        sessionService.createNewAccount(data);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * Obtain new jwt token of account
     * @param request http request
     * @param data body data with email/username and password
     * @return jwt token
     * @throws InternalSessionException if some internal error occurred
     * @throws AccountNotFoundException if the account or password is incorrect
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody LoginAccount data) throws InternalSessionException, AccountNotFoundException, BadArgumentException {
        SessionAccount login = sessionService.login(data);
        return new ResponseEntity<>(login, HttpStatus.CREATED);
    }

    /**
     * Nothing happens because sessions use jwt token
     * @param request http request
     * @param data body with jwt
     * @return 200 OK
     */
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, @RequestBody SessionAccount data) {
        // not necessary with jwt tokens
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}