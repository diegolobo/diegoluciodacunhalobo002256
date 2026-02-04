package br.com.rockstars.service;

import br.com.rockstars.domain.dto.ArtistDTO;
import br.com.rockstars.domain.entity.Artist;
import br.com.rockstars.exception.NotFoundException;
import br.com.rockstars.repository.ArtistRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ArtistService {

    @Inject
    ArtistRepository artistRepository;

    public List<ArtistDTO> findAll() {
        return artistRepository.listAll().stream()
            .map(ArtistDTO::fromEntity)
            .collect(Collectors.toList());
    }

    public ArtistDTO findById(Long id) {
        Artist artist = artistRepository.findById(id);
        if (artist == null) {
            throw new NotFoundException("Artista", id);
        }
        return ArtistDTO.fromEntity(artist);
    }

    public Artist findEntityById(Long id) {
        Artist artist = artistRepository.findById(id);
        if (artist == null) {
            throw new NotFoundException("Artista", id);
        }
        return artist;
    }

    @Transactional
    public ArtistDTO create(ArtistDTO dto) {
        Artist artist = dto.toEntity();
        artistRepository.persist(artist);
        return ArtistDTO.fromEntity(artist);
    }

    @Transactional
    public ArtistDTO update(Long id, ArtistDTO dto) {
        Artist artist = artistRepository.findById(id);
        if (artist == null) {
            throw new NotFoundException("Artista", id);
        }
        dto.updateEntity(artist);
        artistRepository.persist(artist);
        return ArtistDTO.fromEntity(artist);
    }

    @Transactional
    public void delete(Long id) {
        Artist artist = artistRepository.findById(id);
        if (artist == null) {
            throw new NotFoundException("Artista", id);
        }
        artist.setActive(false);
        artistRepository.persist(artist);
    }
}
