package br.com.rockstars.repository;

import br.com.rockstars.domain.entity.Artist;
import br.com.rockstars.domain.enums.ArtistType;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ArtistRepository implements PanacheRepository<Artist> {

    public PanacheQuery<Artist> findWithFilters(String name, ArtistType type, Boolean active, Long regionalId, Sort sort) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (name != null && !name.isBlank()) {
            query.append(" AND LOWER(name) LIKE LOWER(:name)");
            params.put("name", "%" + name + "%");
        }

        if (type != null) {
            query.append(" AND type = :type");
            params.put("type", type);
        }

        if (active != null) {
            query.append(" AND active = :active");
            params.put("active", active);
        }

        if (regionalId != null) {
            query.append(" AND regional.id = :regionalId");
            params.put("regionalId", regionalId);
        }

        return find(query.toString(), sort, params);
    }

    public long countWithFilters(String name, ArtistType type, Boolean active, Long regionalId) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (name != null && !name.isBlank()) {
            query.append(" AND LOWER(name) LIKE LOWER(:name)");
            params.put("name", "%" + name + "%");
        }

        if (type != null) {
            query.append(" AND type = :type");
            params.put("type", type);
        }

        if (active != null) {
            query.append(" AND active = :active");
            params.put("active", active);
        }

        if (regionalId != null) {
            query.append(" AND regional.id = :regionalId");
            params.put("regionalId", regionalId);
        }

        return count(query.toString(), params);
    }
}
