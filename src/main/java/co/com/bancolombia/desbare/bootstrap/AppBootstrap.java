package co.com.bancolombia.desbare.bootstrap;

import javax.sql.DataSource;

import co.com.bancolombia.desbare.core.domain.ports.outbound.Base64ServicePort;
import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgDecryptionPort;
import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgEncryptionPort;
import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgGeneratorPort;
import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgKeyRepositoryPort;
import co.com.bancolombia.desbare.core.domain.service.Base64Service;
import co.com.bancolombia.desbare.core.domain.usecase.DecryptUseCase;
import co.com.bancolombia.desbare.core.domain.usecase.EncryptUseCase;
import co.com.bancolombia.desbare.core.domain.usecase.GenerateKeyUseCase;
import co.com.bancolombia.desbare.core.domain.usecase.ListGpgKeysUseCase;
import co.com.bancolombia.desbare.core.infrastructure.gpg.BouncyCastleDecryptionAdapter;
import co.com.bancolombia.desbare.core.infrastructure.gpg.BouncyCastleEncryptionAdapter;
import co.com.bancolombia.desbare.core.infrastructure.gpg.BouncyCastleGpgGeneratorAdapter;
import co.com.bancolombia.desbare.core.infrastructure.sqlite.SqliteGpgKeyRepositoryAdapter;
import co.com.bancolombia.desbare.core.infrastructure.sqlite.config.SqliteDataSourceFactory;
import co.com.bancolombia.desbare.core.infrastructure.sqlite.config.SqliteDatabaseInitializer;
import co.com.bancolombia.desbare.ui.viewmodel.base64.Base64ToolViewModel;
import co.com.bancolombia.desbare.ui.viewmodel.gpg.CreateKeyViewModel;
import co.com.bancolombia.desbare.ui.viewmodel.gpg.DecryptViewModel;
import co.com.bancolombia.desbare.ui.viewmodel.gpg.EncryptViewModel;
import co.com.bancolombia.desbare.ui.viewmodel.gpg.ListKeysViewModel;

public class AppBootstrap {

    private static final AppBootstrap INSTANCE =
            new AppBootstrap();

    private final DataSource dataSource;

    private final GpgGeneratorPort generatorPort;
    private final GpgEncryptionPort encryptionPort;
    private final GpgDecryptionPort decryptionPort;

    private final GpgKeyRepositoryPort repository;
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

    public Base64ToolViewModel base64ToolViewModel() {
        return base64ToolViewModel;
    }

    public ListKeysViewModel listKeysViewModel() {
        return listKeysViewModel;
    }
}
