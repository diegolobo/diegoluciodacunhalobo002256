package br.com.rockstars.repository;

import br.com.rockstars.domain.entity.Album;
import br.com.rockstars.domain.enums.ArtistType;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class AlbumRepository implements PanacheRepository<Album> {

    public PanacheQuery<Album> findWithFilters(String title, Long artistId, ArtistType artistType, Boolean active, Sort sort) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (title != null && !title.isBlank()) {
            query.append(" AND LOWER(title) LIKE LOWER(:title)");
            params.put("title", "%" + title + "%");
        }

        if (artistId != null) {
            query.append(" AND :artistId MEMBER OF artists");
            params.put("artistId", artistId);
        }

        if (artistType != null) {
            query.append(" AND EXISTS (SELECT 1 FROM artists a WHERE a.type = :artistType)");
            params.put("artistType", artistType);
        }

        if (active != null) {
            query.append(" AND active = :active");
            params.put("active", active);
        }

        return find(query.toString(), sort, params);
    }

    public long countWithFilters(String title, Long artistId, ArtistType artistType, Boolean active) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (title != null && !title.isBlank()) {
            query.append(" AND LOWER(title) LIKE LOWER(:title)");
            params.put("title", "%" + title + "%");
        }

        if (artistId != null) {
            query.append(" AND :artistId MEMBER OF artists");
            params.put("artistId", artistId);
        }

        if (artistType != null) {
            query.append(" AND EXISTS (SELECT 1 FROM artists a WHERE a.type = :artistType)");
            params.put("artistType", artistType);
        }

        if (active != null) {
            query.append(" AND active = :active");
            params.put("active", active);
        }

        return count(query.toString(), params);
    }

    public List<Album> findByArtistId(Long artistId) {
        return list("SELECT a FROM Album a JOIN a.artists art WHERE art.id = :artistId",
            Map.of("artistId", artistId));
    }
}
