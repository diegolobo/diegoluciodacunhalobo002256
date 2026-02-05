package br.com.rockstars.service;

import br.com.rockstars.domain.dto.AlbumDTO;
import br.com.rockstars.domain.dto.AlbumRequestDTO;
import br.com.rockstars.domain.entity.Album;
import br.com.rockstars.exception.NotFoundException;
import br.com.rockstars.repository.AlbumRepository;
import br.com.rockstars.websocket.AlbumNotificationSocket;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class AlbumServiceTest {

    @Inject
    AlbumService albumService;

    @InjectMock
    AlbumRepository albumRepository;

    @InjectMock
    AlbumNotificationSocket notificationSocket;

    private Album album;

    @BeforeEach
    void setUp() {
        album = new Album("Test Album", 2024);
        album.setId(1L);
        album.setActive(true);
    }

    @Test
    void shouldFindAlbumById() {
        when(albumRepository.findById(1L)).thenReturn(album);

        AlbumDTO result = albumService.findById(1L);

        assertNotNull(result);
        assertEquals("Test Album", result.getTitle());
        assertEquals(2024, result.getReleaseYear());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenAlbumNotFound() {
        when(albumRepository.findById(999L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> albumService.findById(999L));
    }

    @Test
    void shouldCreateAlbum() {
        AlbumRequestDTO request = new AlbumRequestDTO("New Album", 2025);
        doNothing().when(albumRepository).persist(any(Album.class));
        doNothing().when(notificationSocket).broadcast(any());

        AlbumDTO result = albumService.create(request);

        assertNotNull(result);
        assertEquals("New Album", result.getTitle());
        assertEquals(2025, result.getReleaseYear());
        verify(albumRepository).persist(any(Album.class));
        verify(notificationSocket).broadcast(any());
    }

    @Test
    void shouldUpdateAlbum() {
        when(albumRepository.findById(1L)).thenReturn(album);
        doNothing().when(albumRepository).persist(any(Album.class));

        AlbumRequestDTO request = new AlbumRequestDTO("Updated Album", 2026);
        AlbumDTO result = albumService.update(1L, request);

        assertNotNull(result);
        assertEquals("Updated Album", result.getTitle());
        verify(albumRepository).persist(any(Album.class));
    }

    @Test
    void shouldSoftDeleteAlbum() {
        when(albumRepository.findById(1L)).thenReturn(album);
        doNothing().when(albumRepository).persist(any(Album.class));

        albumService.delete(1L);

        assertFalse(album.getActive());
        verify(albumRepository).persist(album);
    }
}
