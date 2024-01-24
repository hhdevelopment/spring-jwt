package io.softwarity.sample.jwt;

import java.io.File;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.SneakyThrows;

/**
 * Read the public key from the pem file and provide it as bean PublicKey
 */
@Configuration
public class PemProvider {

  @Bean
  PublicKey getPublicKey(@Value("${jwt.pem-path}") String pemPath, @Value("${jwt.algorithm}") String algorithm) {
    File file = new File(pemPath);
    try (PemReader pemReader = new PemReader(new FileReader(file))) {
      PemObject pemObject = pemReader.readPemObject();
      return getPublicKey(algorithm, pemObject.getContent());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

/**
   * Retourne une clé publique à partir de l'algorithme et de la clé publique au format DER.
   * @param algo
   * @param publicKey
   * @return
   */
  @SneakyThrows
  private PublicKey getPublicKey(String algo, byte[] publicKey) {
    X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey);
    KeyFactory keyFactory = getKeyFactory(algo);
    return keyFactory.generatePublic(spec);
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
