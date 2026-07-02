package co.com.bancolombia.desbare.core.infrastructure.gpg;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgDecryptionPort;

import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;

public class BouncyCastleDecryptionAdapter
        implements GpgDecryptionPort {

    @Override
    public String decrypt(
            String encryptedText,
            String armoredPrivateKey,
            String passphrase
    ) {

        try {

            PGPSecretKeyRing secretKeyRing =
                    readSecretKeyRing(
                            armoredPrivateKey
                    );

            InputStream decoderStream =
                    PGPUtil.getDecoderStream(
                            new ByteArrayInputStream(
                                    encryptedText.getBytes(
                                            StandardCharsets.UTF_8
                                    )
                            )
                    );

            PGPObjectFactory factory =
                    new PGPObjectFactory(
                            decoderStream,
                            new JcaKeyFingerprintCalculator()
                    );

            Object object = factory.nextObject();

            PGPEncryptedDataList encryptedDataList;

            if (object instanceof PGPEncryptedDataList list) {
                encryptedDataList = list;
            } else {
                encryptedDataList =
                        (PGPEncryptedDataList)
                                factory.nextObject();
            }

            PGPPrivateKey privateKey = null;
            PGPPublicKeyEncryptedData encryptedData = null;

            Iterator<?> iterator =
                    encryptedDataList.getEncryptedDataObjects();

            while (iterator.hasNext()) {

                PGPPublicKeyEncryptedData candidate =
                        (PGPPublicKeyEncryptedData)
                                iterator.next();

                PGPSecretKey secretKey =
                        secretKeyRing.getSecretKey(
                                candidate.getKeyID()
                        );

                if (secretKey != null) {

                    privateKey =
                            extractPrivateKey(
                                    secretKey,
                                    passphrase
                            );

                    encryptedData = candidate;

                    break;
                }
            }

            if (privateKey == null) {
                throw new IllegalArgumentException(
                        "No se encontró la llave privada."
                );
            }

            InputStream clearData =
                    encryptedData.getDataStream(
                            new JcePublicKeyDataDecryptorFactoryBuilder()
                                    .build(privateKey)
                    );

            PGPObjectFactory plainFactory =
                    new PGPObjectFactory(
                            clearData,
                            new JcaKeyFingerprintCalculator()
                    );

            Object message =
                    plainFactory.nextObject();

            if (message instanceof PGPLiteralData literalData) {

                InputStream data =
                        literalData.getInputStream();

                return new String(
                        data.readAllBytes(),
                        StandardCharsets.UTF_8
                );
            }

            throw new IllegalStateException(
                    "Contenido PGP no soportado."
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error descifrando mensaje",
                    e
            );
        }
    }

    private PGPSecretKeyRing readSecretKeyRing(
            String armoredPrivateKey
    ) throws Exception {

        InputStream in =
                PGPUtil.getDecoderStream(
                        new ByteArrayInputStream(
                                armoredPrivateKey.getBytes(
                                        StandardCharsets.UTF_8
                                )
                        )
                );

        PGPSecretKeyRingCollection collection =
                new PGPSecretKeyRingCollection(
                        in,
                        new JcaKeyFingerprintCalculator()
                );

        return collection.getKeyRings().next();
    }

    private PGPPrivateKey extractPrivateKey(
            PGPSecretKey secretKey,
            String passphrase
    ) throws Exception {

        return secretKey.extractPrivateKey(
                new JcePBESecretKeyDecryptorBuilder()
                        .build(
                                passphrase.toCharArray()
                        )
        );
    }
}

