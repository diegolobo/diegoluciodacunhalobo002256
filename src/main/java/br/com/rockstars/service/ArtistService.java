package br.com.rockstars.service;

import br.com.rockstars.domain.dto.AlbumDTO;
import br.com.rockstars.domain.dto.ArtistDTO;
import br.com.rockstars.domain.dto.ArtistRequestDTO;
import br.com.rockstars.domain.dto.PageResponseDTO;
import br.com.rockstars.domain.entity.Artist;
import br.com.rockstars.domain.entity.Regional;
import br.com.rockstars.domain.enums.ArtistType;
import br.com.rockstars.exception.NotFoundException;
import br.com.rockstars.repository.AlbumRepository;
import br.com.rockstars.repository.ArtistRepository;
import br.com.rockstars.repository.RegionalRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ArtistService {

    @Inject
    ArtistRepository artistRepository;

    @Inject
    AlbumRepository albumRepository;

    @Inject
    RegionalRepository regionalRepository;

    public List<ArtistDTO> findAll() {
        return artistRepository.listAll().stream()
            .map(ArtistDTO::fromEntity)
            .collect(Collectors.toList());
    }

    public PageResponseDTO<ArtistDTO> findAll(int page, int size, String name, ArtistType type, Boolean active, Long regionalId, String sortField, String sortDirection) {
        Sort sort = buildSort(sortField, sortDirection);

        List<ArtistDTO> content = artistRepository.findWithFilters(name, type, active, regionalId, sort)
            .page(Page.of(page, size))
            .list()
            .stream()
            .map(ArtistDTO::fromEntity)
            .collect(Collectors.toList());

        long totalElements = artistRepository.countWithFilters(name, type, active, regionalId);

        return PageResponseDTO.of(content, page, size, totalElements);
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
    public ArtistDTO create(ArtistRequestDTO dto) {
        Artist artist = dto.toEntity();
        setRegional(artist, dto.getRegionalId());
        artistRepository.persist(artist);
        return ArtistDTO.fromEntity(artist);
    }

    @Transactional
    public ArtistDTO update(Long id, ArtistRequestDTO dto) {
        Artist artist = artistRepository.findById(id);
        if (artist == null) {
            throw new NotFoundException("Artista", id);
        }
        dto.updateEntity(artist);
        setRegional(artist, dto.getRegionalId());
        artistRepository.persist(artist);
        return ArtistDTO.fromEntity(artist);
    }

    private void setRegional(Artist artist, Long regionalId) {
        if (regionalId != null) {
            Regional regional = regionalRepository.findById(regionalId);
            if (regional == null) {
                throw new NotFoundException("Regional", regionalId);
            }
            artist.setRegional(regional);
        } else {
            artist.setRegional(null);
        }
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

    public List<AlbumDTO> findAlbumsByArtistId(Long artistId) {
        Artist artist = artistRepository.findById(artistId);
        if (artist == null) {
            throw new NotFoundException("Artista", artistId);
        }
        return albumRepository.findByArtistId(artistId).stream()
            .map(AlbumDTO::fromEntityWithoutRelations)
            .collect(Collectors.toList());
    }

    private Sort buildSort(String sortField, String sortDirection) {
        if (sortField == null || sortField.isBlank()) {
            return Sort.by("name");
        }

        Sort.Direction direction = Sort.Direction.Ascending;
        if (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.Descending;
        }

        return Sort.by(sortField, direction);
    }
}
