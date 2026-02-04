package br.com.rockstars.repository;

import br.com.rockstars.domain.entity.Album;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AlbumRepository implements PanacheRepository<Album> {

}
