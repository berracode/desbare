package com.ritallus.desvare.core.infrastructure.gpg;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.util.Date;

import com.ritallus.desvare.core.domain.enums.KeyType;
import com.ritallus.desvare.core.domain.exception.custom.GeneratingKeyException;
import com.ritallus.desvare.core.domain.model.GenerateKeyRequest;
import com.ritallus.desvare.core.domain.model.GeneratedKeyPair;
import com.ritallus.desvare.core.domain.ports.outbound.GpgGeneratorPort;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.sig.KeyFlags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPKeyRingGenerator;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.operator.PBESecretKeyEncryptor;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;

@Slf4j
public class BouncyCastleGpgGeneratorAdapter implements GpgGeneratorPort {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public GeneratedKeyPair generate(GenerateKeyRequest request) {

        if (request.keyType() != KeyType.RSA_RSA) {
            throw new UnsupportedOperationException("Only RSA/RSA keys are currently supported");
        }

        try {
            Date creationDate = new Date();
            var identity = request.name() + " <" + request.email() + ">";

            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");

            kpg.initialize(request.keySize());


            // Primary key: para fimar y certificar
            KeyPair signingPair = kpg.generateKeyPair();

            // Encryption subkey: para  encriptar
            KeyPair encryptionPair = kpg.generateKeyPair();

            PGPKeyPair masterKey = new JcaPGPKeyPair(PGPPublicKey.RSA_SIGN, signingPair, creationDate);

            PGPKeyPair subKey = new JcaPGPKeyPair(PGPPublicKey.RSA_ENCRYPT, encryptionPair, creationDate);

            PGPDigestCalculator sha1Calc = new JcaPGPDigestCalculatorProviderBuilder()
                    .build()
                    .get(HashAlgorithmTags.SHA1);

            PBESecretKeyEncryptor secretKeyEncryptor =
                    new JcePBESecretKeyEncryptorBuilder(PGPEncryptedData.AES_256, sha1Calc).setProvider("BC")
                            .build(request.passphrase().toCharArray());

            // Primary key flags
            PGPSignatureSubpacketGenerator masterSubpackets = new PGPSignatureSubpacketGenerator();

            masterSubpackets.setKeyFlags(false, KeyFlags.CERTIFY_OTHER | KeyFlags.SIGN_DATA);

            long expiration = expirationSeconds(request);

            if (expiration > 0) {
                masterSubpackets.setKeyExpirationTime(false, expiration);
            }

            // Encryption subkey flags
            PGPSignatureSubpacketGenerator encryptionSubpackets = new PGPSignatureSubpacketGenerator();
            encryptionSubpackets.setKeyFlags(false, KeyFlags.ENCRYPT_COMMS | KeyFlags.ENCRYPT_STORAGE);

            if (expiration > 0) {
                encryptionSubpackets.setKeyExpirationTime(false, expiration);
            }

            PGPKeyRingGenerator keyRingGenerator =
                    new PGPKeyRingGenerator(
                            PGPSignature.POSITIVE_CERTIFICATION,
                            masterKey,
                            identity,
                            sha1Calc,
                            masterSubpackets.generate(),
                            null,
                            new JcaPGPContentSignerBuilder(
                                    masterKey.getPublicKey().getAlgorithm(),
                                    HashAlgorithmTags.SHA256
                            ),
                            secretKeyEncryptor
                    );

            keyRingGenerator.addSubKey(subKey, encryptionSubpackets.generate(), null);

            PGPSecretKeyRing secretKeyRing = keyRingGenerator.generateSecretKeyRing();
            PGPPublicKeyRing publicKeyRing = keyRingGenerator.generatePublicKeyRing();

            String publicKey = exportPublicKey(publicKeyRing);

            String privateKey = exportPrivateKey(secretKeyRing);

            String fingerprint = bytesToHex(masterKey.getPublicKey().getFingerprint());

            return new GeneratedKeyPair(publicKey, privateKey, fingerprint);

        } catch (Exception e) {
            log.error("Error generando KEY:", e);
            throw new GeneratingKeyException("Error generando llave OpenPGP");
        }
    }

    private String exportPublicKey(PGPPublicKeyRing keyRing) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ArmoredOutputStream armor = new ArmoredOutputStream(baos)) {
            keyRing.encode(armor);
        }

        return baos.toString(StandardCharsets.UTF_8);
    }

    private String exportPrivateKey(PGPSecretKeyRing keyRing) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ArmoredOutputStream armor = new ArmoredOutputStream(baos)) {
            keyRing.encode(armor);
        }

        return baos.toString(StandardCharsets.UTF_8);
    }

    private long expirationSeconds(GenerateKeyRequest request) {

        if (request.neverExpire()) {
            return 0;
        }
        return switch (request.expirationUnit()) {
            case WEEKS -> request.expirationAmount() * 7L * 24 * 60 * 60;
            case MONTHS -> request.expirationAmount() * 30L * 24 * 60 * 60;
            case YEARS -> request.expirationAmount() * 365L * 24 * 60 * 60;
        };
    }

    private String bytesToHex(byte[] bytes) {

        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}