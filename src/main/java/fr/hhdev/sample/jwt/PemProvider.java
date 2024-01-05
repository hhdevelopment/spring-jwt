package fr.hhdev.sample.jwt;

import java.io.File;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Read the public key from the pem file and provide it as bean PublicKey
 */
@Configuration
public class PemProvider {


  @Autowired
  JwtProperties jwtProperties;

  @Bean
  PublicKey getPublicKey() {
    Security.addProvider(new BouncyCastleProvider());
    File file = new File(jwtProperties.getPemPath());
    try (PemReader pemReader = new PemReader(new FileReader(file))) {
      PemObject pemObject = pemReader.readPemObject();
      byte[] content = pemObject.getContent();
      X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(content);
      return getKeyFactory(jwtProperties.getAlgorithm()).generatePublic(keySpecX509);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Returns a Keyfactory body for the algorithm given "RS256" or "ES256", "PS256".
   * @param jwtAlgorithm
   * @return
   * @throws NoSuchAlgorithmException
   */
  private KeyFactory getKeyFactory(String jwtAlgorithm) throws NoSuchAlgorithmException { // "RS256" ou "ES256", "PS256"
    String keyAlgorithm;
    switch (jwtAlgorithm) {
      case "ES256":
        keyAlgorithm = "EC";
        break;
      case "RS256":
      case "PS256":
      default:
        keyAlgorithm = "RSA";
    }
    return KeyFactory.getInstance(keyAlgorithm);
  }

}
