package br.com.rockstars.service;

import br.com.rockstars.domain.dto.ArtistDTO;
import br.com.rockstars.domain.dto.ArtistRequestDTO;
import br.com.rockstars.domain.entity.Artist;
import br.com.rockstars.domain.enums.ArtistType;
import br.com.rockstars.exception.NotFoundException;
import br.com.rockstars.repository.AlbumRepository;
import br.com.rockstars.repository.ArtistRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class ArtistServiceTest {

    @Inject
    ArtistService artistService;

    @InjectMock
    ArtistRepository artistRepository;

    @InjectMock
    AlbumRepository albumRepository;

    private Artist artist;

    @BeforeEach
    void setUp() {
        artist = new Artist("Test Artist", ArtistType.SOLO);
        artist.setId(1L);
        artist.setActive(true);
    }

    @Test
    void shouldFindArtistById() {
        when(artistRepository.findById(1L)).thenReturn(artist);

        ArtistDTO result = artistService.findById(1L);

        assertNotNull(result);
        assertEquals("Test Artist", result.getName());
        assertEquals(ArtistType.SOLO, result.getType());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenArtistNotFound() {
        when(artistRepository.findById(999L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> artistService.findById(999L));
    }

    @Test
    void shouldCreateArtist() {
        ArtistRequestDTO request = new ArtistRequestDTO("New Artist", ArtistType.BAND);
        doNothing().when(artistRepository).persist(any(Artist.class));

        ArtistDTO result = artistService.create(request);

        assertNotNull(result);
        assertEquals("New Artist", result.getName());
        assertEquals(ArtistType.BAND, result.getType());
        verify(artistRepository).persist(any(Artist.class));
    }

    @Test
    void shouldUpdateArtist() {
        when(artistRepository.findById(1L)).thenReturn(artist);
        doNothing().when(artistRepository).persist(any(Artist.class));

        ArtistRequestDTO request = new ArtistRequestDTO("Updated Artist", ArtistType.BAND);
        ArtistDTO result = artistService.update(1L, request);

        assertNotNull(result);
        assertEquals("Updated Artist", result.getName());
        verify(artistRepository).persist(any(Artist.class));
    }

    @Test
    void shouldSoftDeleteArtist() {
        when(artistRepository.findById(1L)).thenReturn(artist);
        doNothing().when(artistRepository).persist(any(Artist.class));

        artistService.delete(1L);

        assertFalse(artist.getActive());
        verify(artistRepository).persist(artist);
    }
}
