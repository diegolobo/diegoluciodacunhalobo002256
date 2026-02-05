package br.com.rockstars.service;

import br.com.rockstars.domain.dto.AlbumCoverDTO;
import br.com.rockstars.domain.entity.Album;
import br.com.rockstars.domain.entity.AlbumCover;
import br.com.rockstars.exception.BusinessException;
import br.com.rockstars.exception.NotFoundException;
import br.com.rockstars.repository.AlbumCoverRepository;
import br.com.rockstars.repository.AlbumRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class AlbumCoverService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
        "image/jpeg",
        "image/jpg",
        "image/png",
        "image/webp"
    );

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Inject
    AlbumRepository albumRepository;

    @Inject
    AlbumCoverRepository albumCoverRepository;

    @Inject
    StorageService storageService;

    public List<AlbumCoverDTO> findByAlbumId(Long albumId) {
        Album album = albumRepository.findById(albumId);
        if (album == null) {
            throw new NotFoundException("Album", albumId);
        }

        return albumCoverRepository.findByAlbumId(albumId).stream()
            .map(this::toDtoWithPresignedUrl)
            .collect(Collectors.toList());
    }

    @Transactional
    public AlbumCoverDTO uploadCover(Long albumId, InputStream inputStream, String fileName, String contentType, long size) {
        validateFile(contentType, size);

        Album album = albumRepository.findById(albumId);
        if (album == null) {
            throw new NotFoundException("Album", albumId);
        }

        String minioKey = storageService.uploadFile(inputStream, fileName, contentType, size);

        AlbumCover cover = new AlbumCover(album, fileName, minioKey);
        cover.setContentType(contentType);
        cover.setFileSize(size);
        albumCoverRepository.persist(cover);

        return toDtoWithPresignedUrl(cover);
    }

    @Transactional
    public List<AlbumCoverDTO> uploadCovers(Long albumId, List<FileUpload> files) {
        Album album = albumRepository.findById(albumId);
        if (album == null) {
            throw new NotFoundException("Album", albumId);
        }

        List<AlbumCoverDTO> results = new ArrayList<>();
        for (FileUpload file : files) {
            validateFile(file.contentType(), file.size());

            String minioKey = storageService.uploadFile(file.inputStream(), file.fileName(), file.contentType(), file.size());

            AlbumCover cover = new AlbumCover(album, file.fileName(), minioKey);
            cover.setContentType(file.contentType());
            cover.setFileSize(file.size());
            albumCoverRepository.persist(cover);

            results.add(toDtoWithPresignedUrl(cover));
        }

        return results;
    }

    @Transactional
    public void deleteCover(Long albumId, Long coverId) {
        Album album = albumRepository.findById(albumId);
        if (album == null) {
            throw new NotFoundException("Album", albumId);
        }

        AlbumCover cover = albumCoverRepository.findById(coverId);
        if (cover == null || !cover.getAlbum().getId().equals(albumId)) {
            throw new NotFoundException("AlbumCover", coverId);
        }

        storageService.deleteFile(cover.getMinioKey());
        albumCoverRepository.delete(cover);
    }

    private void validateFile(String contentType, long size) {
        if (!ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new BusinessException("Tipo de arquivo nao permitido. Use: jpg, png ou webp");
        }

        if (size > MAX_FILE_SIZE) {
            throw new BusinessException("Arquivo muito grande. Tamanho maximo: 5MB");
        }
    }

    private AlbumCoverDTO toDtoWithPresignedUrl(AlbumCover cover) {
        AlbumCoverDTO dto = AlbumCoverDTO.fromEntity(cover);
        dto.setPresignedUrl(storageService.getPresignedUrl(cover.getMinioKey()));
        return dto;
    }

    public record FileUpload(InputStream inputStream, String fileName, String contentType, long size) {}
}
