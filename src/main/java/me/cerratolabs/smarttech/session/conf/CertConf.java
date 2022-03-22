package me.cerratolabs.smarttech.session.conf;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Controller
public class CertConf {

    private final ApplicationConf applicationConf;
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    @Autowired
    public CertConf(ApplicationConf applicationConf) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, URISyntaxException {
        this.applicationConf = applicationConf;
        loadPublicRsa();
        loadPrivateRsa();
    }

    private void loadPublicRsa() throws IOException, URISyntaxException, NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKey = readFile(applicationConf.getPublicKeyPath());
        publicKey = publicKey.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");

        KeyFactory kf = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        this.publicKey = (RSAPublicKey) kf.generatePublic(keySpecX509);

    }

    private void loadPrivateRsa() throws IOException, URISyntaxException, NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKey = readFile(applicationConf.getPrivateKeyPath());
        privateKey = privateKey.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");

        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        this.privateKey = (RSAPrivateKey) kf.generatePrivate(pkcs8);

    }

    private String readFile(String path) throws URISyntaxException, IOException {
        return new String(Files.readAllBytes(Paths.get(this.getClass().getResource(path).toURI())));
    }

    public Algorithm obtainAlgorithmRs() {
        return Algorithm.RSA256(publicKey, privateKey);
    }

}