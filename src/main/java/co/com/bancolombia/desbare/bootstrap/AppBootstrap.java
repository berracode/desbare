package co.com.bancolombia.desbare.bootstrap;

import javax.sql.DataSource;

import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgDecryptionPort;
import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgEncryptionPort;
import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgGeneratorPort;
import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgKeyRepositoryPort;
import co.com.bancolombia.desbare.core.domain.usecase.DecryptUseCase;
import co.com.bancolombia.desbare.core.domain.usecase.EncryptUseCase;
import co.com.bancolombia.desbare.core.domain.usecase.GenerateKeyUseCase;
import co.com.bancolombia.desbare.core.infrastructure.gpg.BouncyCastleDecryptionAdapter;
import co.com.bancolombia.desbare.core.infrastructure.gpg.BouncyCastleEncryptionAdapter;
import co.com.bancolombia.desbare.core.infrastructure.gpg.BouncyCastleGpgGeneratorAdapter;
import co.com.bancolombia.desbare.core.infrastructure.sqlite.SqliteGpgKeyRepositoryAdapter;
import co.com.bancolombia.desbare.core.infrastructure.sqlite.config.SqliteDataSourceFactory;
import co.com.bancolombia.desbare.core.infrastructure.sqlite.config.SqliteDatabaseInitializer;
import co.com.bancolombia.desbare.ui.viewmodel.gpg.CreateKeyViewModel;
import co.com.bancolombia.desbare.ui.viewmodel.gpg.DecryptViewModel;
import co.com.bancolombia.desbare.ui.viewmodel.gpg.EncryptViewModel;

public class AppBootstrap {

    private static final AppBootstrap INSTANCE =
            new AppBootstrap();

    private final DataSource dataSource;

    private final GpgGeneratorPort generatorPort;
    private final GpgEncryptionPort encryptionPort;
    private final GpgDecryptionPort decryptionPort;

    private final GpgKeyRepositoryPort repository;

    private final GenerateKeyUseCase generateKeyUseCase;
    private final EncryptUseCase encryptUseCase;
    private final DecryptUseCase decryptUseCase;

    private final CreateKeyViewModel createKeyViewModel;
    private final EncryptViewModel encryptViewModel;
    private final DecryptViewModel decryptViewModel;

    private AppBootstrap() {

        dataSource = new SqliteDataSourceFactory()
                .create();

        new SqliteDatabaseInitializer(dataSource).initialize();

        repository = new SqliteGpgKeyRepositoryAdapter(dataSource);

        generatorPort = new BouncyCastleGpgGeneratorAdapter();
        encryptionPort = new BouncyCastleEncryptionAdapter();
        decryptionPort = new BouncyCastleDecryptionAdapter();

        generateKeyUseCase = new GenerateKeyUseCase(generatorPort, repository);
        encryptUseCase = new EncryptUseCase(encryptionPort, repository);
        decryptUseCase = new DecryptUseCase(decryptionPort, repository);

        createKeyViewModel = new CreateKeyViewModel(generateKeyUseCase);
        encryptViewModel = new EncryptViewModel(encryptUseCase);
        decryptViewModel = new DecryptViewModel(decryptUseCase);

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
}
