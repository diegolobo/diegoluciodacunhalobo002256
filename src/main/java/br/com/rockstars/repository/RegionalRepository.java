package br.com.rockstars.repository;

import br.com.rockstars.domain.entity.Regional;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RegionalRepository implements PanacheRepository<Regional> {

    public Optional<Regional> findByExternalIdAndActive(Long externalId, Boolean active) {
        return find("externalId = ?1 AND active = ?2", externalId, active).firstResultOptional();
    }

    public List<Regional> findAllActive() {
        return list("active", true);
    }

    public Optional<Regional> findActiveByExternalId(Long externalId) {
        return findByExternalIdAndActive(externalId, true);
    }
}
