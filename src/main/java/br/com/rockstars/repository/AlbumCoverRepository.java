package br.com.rockstars.repository;

import br.com.rockstars.domain.entity.AlbumCover;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class AlbumCoverRepository implements PanacheRepository<AlbumCover> {

    public List<AlbumCover> findByAlbumId(Long albumId) {
        return list("album.id", albumId);
    }
}
