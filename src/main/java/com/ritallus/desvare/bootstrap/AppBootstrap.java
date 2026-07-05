package com.ritallus.desvare.bootstrap;

import javax.sql.DataSource;

import com.ritallus.desvare.core.domain.ports.outbound.Base64ServicePort;
import com.ritallus.desvare.core.domain.ports.outbound.GpgDecryptionPort;
import com.ritallus.desvare.core.domain.ports.outbound.GpgEncryptionPort;
import com.ritallus.desvare.core.domain.ports.outbound.GpgGeneratorPort;
import com.ritallus.desvare.core.domain.ports.outbound.GpgKeyRepositoryPort;
import com.ritallus.desvare.core.domain.service.Base64Service;
import com.ritallus.desvare.core.domain.usecase.DecryptUseCase;
import com.ritallus.desvare.core.domain.usecase.EncryptUseCase;
import com.ritallus.desvare.core.domain.usecase.GenerateKeyUseCase;
import com.ritallus.desvare.core.domain.usecase.ListGpgKeysUseCase;
import com.ritallus.desvare.core.infrastructure.gpg.BouncyCastleDecryptionAdapter;
import com.ritallus.desvare.core.infrastructure.gpg.BouncyCastleEncryptionAdapter;
import com.ritallus.desvare.core.infrastructure.gpg.BouncyCastleGpgGeneratorAdapter;
import com.ritallus.desvare.core.infrastructure.sqlite.SqliteGpgKeyRepositoryAdapter;
import com.ritallus.desvare.core.infrastructure.sqlite.config.SqliteDataSourceFactory;
import com.ritallus.desvare.core.infrastructure.sqlite.config.SqliteDatabaseInitializer;
import com.ritallus.desvare.ui.viewmodel.base64.Base64ToolViewModel;
import com.ritallus.desvare.ui.viewmodel.gpg.CreateKeyViewModel;
import com.ritallus.desvare.ui.viewmodel.gpg.DecryptViewModel;
import com.ritallus.desvare.ui.viewmodel.gpg.EncryptViewModel;
import com.ritallus.desvare.ui.viewmodel.gpg.ListKeysViewModel;

import lombok.Getter;

public class AppBootstrap {

    private static final AppBootstrap INSTANCE =
            new AppBootstrap();

    private final DataSource dataSource;

    private final GpgGeneratorPort generatorPort;
    private final GpgEncryptionPort encryptionPort;
    private final GpgDecryptionPort decryptionPort;

    private final GpgKeyRepositoryPort repository;
    @Getter
    private final Base64ServicePort base64ServicePort;

    private final GenerateKeyUseCase generateKeyUseCase;
    private final EncryptUseCase encryptUseCase;
    private final DecryptUseCase decryptUseCase;
    private final ListGpgKeysUseCase listGpgKeysUseCase;


    private final CreateKeyViewModel createKeyViewModel;
    private final EncryptViewModel encryptViewModel;
    private final DecryptViewModel decryptViewModel;
    private final ListKeysViewModel listKeysViewModel;
    private final Base64ToolViewModel base64ToolViewModel;

    private AppBootstrap() {

        dataSource = new SqliteDataSourceFactory()
                .create();

        new SqliteDatabaseInitializer(dataSource).initialize();

        repository = new SqliteGpgKeyRepositoryAdapter(dataSource);
        base64ServicePort = new Base64Service();

        generatorPort = new BouncyCastleGpgGeneratorAdapter();
        encryptionPort = new BouncyCastleEncryptionAdapter();
        decryptionPort = new BouncyCastleDecryptionAdapter();

        generateKeyUseCase = new GenerateKeyUseCase(generatorPort, repository);
        encryptUseCase = new EncryptUseCase(encryptionPort, repository);
        decryptUseCase = new DecryptUseCase(decryptionPort, repository);
        listGpgKeysUseCase = new ListGpgKeysUseCase(repository);

        createKeyViewModel = new CreateKeyViewModel(generateKeyUseCase);
        encryptViewModel = new EncryptViewModel(encryptUseCase);
        decryptViewModel = new DecryptViewModel(decryptUseCase);
        listKeysViewModel = new ListKeysViewModel(listGpgKeysUseCase, base64ServicePort);
        base64ToolViewModel = new Base64ToolViewModel(base64ServicePort);

    }

    public static AppBootstrap getInstance() {
        return INSTANCE;
    }

    public CreateKeyViewModel createKeyViewModel() {
        return createKeyViewModel;
    }

    public EncryptViewModel encryptViewModel() {
        return encryptViewModel;
    }

    public DecryptViewModel decryptViewModel() {
        return decryptViewModel;
    }

    public ListKeysViewModel listKeysViewModel() {
        return listKeysViewModel;
    }
}
