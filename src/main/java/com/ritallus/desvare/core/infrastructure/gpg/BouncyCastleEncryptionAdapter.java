package com.ritallus.desvare.core.infrastructure.gpg;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Iterator;

import com.ritallus.desvare.core.domain.ports.outbound.GpgEncryptionPort;

import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;

public class BouncyCastleEncryptionAdapter
        implements GpgEncryptionPort {

    @Override
    public String encrypt(String plainText, String armoredPublicKey) {

        try {

            PGPPublicKey publicKey = readEncryptionKey(armoredPublicKey);
            ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream();

            try (ArmoredOutputStream armoredOutput = new ArmoredOutputStream(encryptedOut)) {

                PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(
                        new JcePGPDataEncryptorBuilder(
                                PGPEncryptedData.AES_256).setSecureRandom(
                                        new SecureRandom())
                                .setWithIntegrityPacket(true)
                );

                encryptedDataGenerator.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(publicKey));

                ByteArrayOutputStream literalData = new ByteArrayOutputStream();

                PGPLiteralDataGenerator literalDataGenerator = new PGPLiteralDataGenerator();

                try (OutputStream literalOut = literalDataGenerator.open(
                        literalData,
                        PGPLiteralData.BINARY,
                        PGPLiteralData.CONSOLE,
                        plainText.getBytes().length,
                        Date.valueOf(LocalDate.now())
                )) {
                    literalOut.write(plainText.getBytes(StandardCharsets.UTF_8));
                }

                byte[] bytes = literalData.toByteArray();

                try (OutputStream encryptedStream = encryptedDataGenerator.open(armoredOutput, bytes.length)) {
                    encryptedStream.write(bytes);
                }
            }

            return encryptedOut.toString(StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Error cifrando contenido", e);
        }
    }

    private PGPPublicKey readEncryptionKey(String armoredPublicKey) throws Exception {

        try (InputStream in = new ArmoredInputStream(new ByteArrayInputStream(armoredPublicKey.getBytes(StandardCharsets.UTF_8)))) {

            PGPPublicKeyRingCollection rings = new PGPPublicKeyRingCollection(
                    PGPUtil.getDecoderStream(in),
                    new JcaKeyFingerprintCalculator()
            );

            Iterator<PGPPublicKeyRing> ringIterator = rings.getKeyRings();

            while (ringIterator.hasNext()) {

                PGPPublicKeyRing ring = ringIterator.next();

                Iterator<PGPPublicKey> keys = ring.getPublicKeys();

                while (keys.hasNext()) {
                    PGPPublicKey key = keys.next();
                    if (key.isEncryptionKey()) {
                        return key;
                    }
                }
            }
            throw new IllegalArgumentException("No se encontró una llave de cifrado.");
        }
    }
}

