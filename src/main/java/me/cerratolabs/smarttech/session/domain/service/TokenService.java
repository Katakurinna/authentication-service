package me.cerratolabs.smarttech.session.domain.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.log4j.Log4j2;
import me.cerratolabs.smarttech.session.api.model.Account;
import me.cerratolabs.smarttech.session.api.model.AccountWithId;
import me.cerratolabs.smarttech.session.conf.ApplicationConf;
import me.cerratolabs.smarttech.session.conf.CertConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Log4j2
@Service
public class TokenService {

    @Autowired
    private CertConf certConf;

    @Autowired
    private ApplicationConf applicationConf;

    public String generateJwt(AccountWithId acc) {
        log.debug("Generating new jwt for acc {}", acc.getEmail());

        Algorithm algorithm = certConf.obtainAlgorithmRs();
        String sign = JWT.create()
            .withIssuer("cerratolabs")
            .withClaim("username", acc.getUsername())
            .withClaim("email", acc.getEmail())
            .withClaim("userId", acc.getUserId())
            .withExpiresAt(expirationDate())
            .sign(algorithm);

        log.debug("Generated and signed new jwt for acc {}", acc.getEmail());
        return sign;
    }

    private Date expirationDate() {
        return new Date(System.currentTimeMillis() + applicationConf.getJwtExpirationInMs());
    }


}