package co.com.bancolombia.desbare.core.infrastructure.gpg;

import java.io.ByteArrayOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.util.Date;

import co.com.bancolombia.desbare.core.domain.model.GeneratedKeyPair;
import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgGeneratorPort;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.operator.PBESecretKeyEncryptor;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;

@Slf4j
public class BouncyCastleGpgGeneratorAdapter
        implements GpgGeneratorPort {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public GeneratedKeyPair generate(
            String name,
            String email,
            String passphrase
    ) {

        log.info("Generando key {} {} {}", name, email, passphrase);

        try {

            String identity =
                    name + " <" + email + ">";

            KeyPairGenerator kpg =
                    KeyPairGenerator.getInstance(
                            "RSA",
                            "BC"
                    );

            kpg.initialize(2048);

            KeyPair rsaPair = kpg.generateKeyPair();

            PGPKeyPair pgpKeyPair =
                    new JcaPGPKeyPair(
                            PGPPublicKey.RSA_GENERAL,
                            rsaPair,
                            new Date()
                    );

            PGPDigestCalculator sha1Calc =
                    new JcaPGPDigestCalculatorProviderBuilder()
                            .build()
                            .get(HashAlgorithmTags.SHA1);

            PBESecretKeyEncryptor secretKeyEncryptor =
                    new JcePBESecretKeyEncryptorBuilder(
                            PGPEncryptedData.AES_256,
                            sha1Calc
                    )
                            .setProvider("BC")
                            .build(
                                    passphrase.toCharArray()
                            );

            PGPSecretKey secretKey =
                    new PGPSecretKey(
                            PGPSignature.DEFAULT_CERTIFICATION,
                            pgpKeyPair,
                            identity,
                            sha1Calc,
                            null,
                            null,
                            new JcaPGPContentSignerBuilder(
                                    pgpKeyPair.getPublicKey()
                                            .getAlgorithm(),
                                    HashAlgorithmTags.SHA256
                            ),
                            secretKeyEncryptor
                    );

            String publicKey =
                    exportPublicKey(secretKey);

            String privateKey =
                    exportPrivateKey(secretKey);

            String fingerprint =
                    bytesToHex(
                            secretKey.getPublicKey()
                                    .getFingerprint()
                    );

            return new GeneratedKeyPair(
                    publicKey,
                    privateKey,
                    fingerprint
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error generando llave OpenPGP",
                    e
            );
        }
    }

    private String exportPublicKey(
            PGPSecretKey secretKey
    ) throws Exception {

        ByteArrayOutputStream baos =
                new ByteArrayOutputStream();

        try (
                ArmoredOutputStream armor =
                        new ArmoredOutputStream(baos)
        ) {

            secretKey
                    .getPublicKey()
                    .encode(armor);
        }

        return baos.toString();
    }

    private String exportPrivateKey(
            PGPSecretKey secretKey
    ) throws Exception {

        ByteArrayOutputStream baos =
                new ByteArrayOutputStream();

        try (
                ArmoredOutputStream armor =
                        new ArmoredOutputStream(baos)
        ) {

            secretKey.encode(armor);
        }

        return baos.toString();
    }

    private String bytesToHex(byte[] bytes) {

        StringBuilder sb =
                new StringBuilder();

        for (byte b : bytes) {
            sb.append(
                    String.format("%02X", b)
            );
        }

        return sb.toString();
    }
}
