package co.com.bancolombia.desbare.core.infrastructure.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

import co.com.bancolombia.desbare.core.domain.model.GpgKey;
import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgKeyRepositoryPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SqliteGpgKeyRepositoryAdapter implements GpgKeyRepositoryPort {

    private final DataSource dataSource;

    @Override
    public void save(GpgKey key) {

        String sql = """
                INSERT INTO gpg_keys
                (
                  name,
                  email,
                  fingerprint,
                  public_key,
                  private_key,
                  created_at
                )
                VALUES (?,?,?,?,?,datetime('now'))
                """;

        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, key.name());
            ps.setString(2, key.email());
            ps.setString(3, key.fingerprint());
            ps.setString(4, key.publicKey());
            ps.setString(5, key.privateKey());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GpgKey> findAll() {
        return List.of();
    }

    @Override
    public Optional<GpgKey> findByFingerprint(String fingerprint) {
        return Optional.empty();
    }

    @Override
    public void delete(String fingerprint) {

    }
}
