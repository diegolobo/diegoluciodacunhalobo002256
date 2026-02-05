package br.com.rockstars.service;

import br.com.rockstars.domain.dto.AlbumDTO;
import br.com.rockstars.domain.dto.AlbumRequestDTO;
import br.com.rockstars.domain.dto.ArtistDTO;
import br.com.rockstars.domain.dto.PageResponseDTO;
import br.com.rockstars.domain.entity.Album;
import br.com.rockstars.domain.entity.Artist;
import br.com.rockstars.domain.enums.ArtistType;
import br.com.rockstars.exception.BusinessException;
import br.com.rockstars.exception.NotFoundException;
import br.com.rockstars.repository.AlbumRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AlbumService {

    @Inject
    AlbumRepository albumRepository;

    @Inject
    ArtistService artistService;

    public List<AlbumDTO> findAll() {
        return albumRepository.listAll().stream()
            .map(AlbumDTO::fromEntity)
            .collect(Collectors.toList());
    }

    public PageResponseDTO<AlbumDTO> findAll(int page, int size, String title, Long artistId, ArtistType artistType, Boolean active, String sortField, String sortDirection) {
        Sort sort = buildSort(sortField, sortDirection);

        List<AlbumDTO> content = albumRepository.findWithFilters(title, artistId, artistType, active, sort)
            .page(Page.of(page, size))
            .list()
            .stream()
            .map(AlbumDTO::fromEntity)
            .collect(Collectors.toList());

        long totalElements = albumRepository.countWithFilters(title, artistId, artistType, active);

        return PageResponseDTO.of(content, page, size, totalElements);
    }

    public AlbumDTO findById(Long id) {
        Album album = albumRepository.findById(id);
        if (album == null) {
            throw new NotFoundException("Album", id);
        }
        return AlbumDTO.fromEntity(album);
    }

    public Album findEntityById(Long id) {
        Album album = albumRepository.findById(id);
        if (album == null) {
            throw new NotFoundException("Album", id);
        }
        return album;
    }

    @Transactional
    public AlbumDTO create(AlbumRequestDTO dto) {
        Album album = dto.toEntity();
        albumRepository.persist(album);
        return AlbumDTO.fromEntity(album);
    }

    @Transactional
    public AlbumDTO update(Long id, AlbumRequestDTO dto) {
        Album album = albumRepository.findById(id);
        if (album == null) {
            throw new NotFoundException("Album", id);
        }
        dto.updateEntity(album);
        albumRepository.persist(album);
        return AlbumDTO.fromEntity(album);
    }

    @Transactional
    public void delete(Long id) {
        Album album = albumRepository.findById(id);
        if (album == null) {
            throw new NotFoundException("Album", id);
        }
        album.setActive(false);
        albumRepository.persist(album);
    }

    @Transactional
    public AlbumDTO addArtistToAlbum(Long albumId, Long artistId) {
        Album album = findEntityById(albumId);
        Artist artist = artistService.findEntityById(artistId);

        if (album.getArtists().contains(artist)) {
            throw new BusinessException("Artista ja esta associado a este album");
        }

        album.addArtist(artist);
        albumRepository.persist(album);
        return AlbumDTO.fromEntity(album);
    }

    @Transactional
    public AlbumDTO removeArtistFromAlbum(Long albumId, Long artistId) {
        Album album = findEntityById(albumId);
        Artist artist = artistService.findEntityById(artistId);

        if (!album.getArtists().contains(artist)) {
            throw new BusinessException("Artista nao esta associado a este album");
        }

        album.removeArtist(artist);
        albumRepository.persist(album);
        return AlbumDTO.fromEntity(album);
    }

    public List<ArtistDTO> findArtistsByAlbumId(Long albumId) {
        Album album = albumRepository.findById(albumId);
        if (album == null) {
            throw new NotFoundException("Album", albumId);
        }
        return album.getArtists().stream()
            .map(ArtistDTO::fromEntity)
            .collect(Collectors.toList());
    }

    private Sort buildSort(String sortField, String sortDirection) {
        if (sortField == null || sortField.isBlank()) {
            return Sort.by("title");
        }

        Sort.Direction direction = Sort.Direction.Ascending;
        if (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.Descending;
        }

        return Sort.by(sortField, direction);
    }
}
