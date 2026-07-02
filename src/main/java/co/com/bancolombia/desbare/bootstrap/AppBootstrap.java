package co.com.bancolombia.desbare.bootstrap;

import javax.sql.DataSource;

import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgGeneratorPort;
import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgKeyRepositoryPort;
import co.com.bancolombia.desbare.core.domain.usecase.GenerateKeyUseCase;
import co.com.bancolombia.desbare.core.infrastructure.gpg.BouncyCastleGpgGeneratorAdapter;
import co.com.bancolombia.desbare.core.infrastructure.sqlite.SqliteGpgKeyRepositoryAdapter;
import co.com.bancolombia.desbare.core.infrastructure.sqlite.config.SqliteDataSourceFactory;
import co.com.bancolombia.desbare.core.infrastructure.sqlite.config.SqliteDatabaseInitializer;
import co.com.bancolombia.desbare.ui.viewmodel.gpg.CreateKeyViewModel;

public class AppBootstrap {

    private static final AppBootstrap INSTANCE =
            new AppBootstrap();

    private final DataSource dataSource;

    private final GpgGeneratorPort generatorPort;
    private final GpgKeyRepositoryPort repository;

    private final GenerateKeyUseCase generateKeyUseCase;

    private final CreateKeyViewModel createKeyViewModel;

    private AppBootstrap() {

        dataSource = new SqliteDataSourceFactory()
                .create();

        new SqliteDatabaseInitializer(dataSource).initialize();

        repository = new SqliteGpgKeyRepositoryAdapter(dataSource);
        generatorPort = new BouncyCastleGpgGeneratorAdapter();

        generateKeyUseCase = new GenerateKeyUseCase(generatorPort, repository);

        createKeyViewModel = new CreateKeyViewModel(generateKeyUseCase);
    }

    public static AppBootstrap getInstance() {
        return INSTANCE;
    }

    public CreateKeyViewModel createKeyViewModel() {
        return createKeyViewModel;
    }
}
