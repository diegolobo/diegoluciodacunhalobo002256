package br.com.rockstars.service;

import br.com.rockstars.domain.dto.TokenResponseDTO;
import br.com.rockstars.domain.entity.User;
import br.com.rockstars.exception.BusinessException;
import br.com.rockstars.repository.UserRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class AuthServiceTest {

    @Inject
    AuthService authService;

    @InjectMock
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword(AuthService.BcryptUtil.hash("password123"));
        user.setRole("admin");
        user.setActive(true);
    }

    @Test
    void shouldAuthenticateWithValidCredentials() {
        when(userRepository.findByUsernameAndActive("testuser", true))
            .thenReturn(Optional.of(user));

        TokenResponseDTO result = authService.authenticate("testuser", "password123");

        assertNotNull(result);
        assertNotNull(result.getAccessToken());
        assertEquals("Bearer", result.getTokenType());
    }

    @Test
    void shouldThrowExceptionWithInvalidUsername() {
        when(userRepository.findByUsernameAndActive("invaliduser", true))
            .thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
            () -> authService.authenticate("invaliduser", "password123"));
    }

    @Test
    void shouldThrowExceptionWithInvalidPassword() {
        when(userRepository.findByUsernameAndActive("testuser", true))
            .thenReturn(Optional.of(user));

        assertThrows(BusinessException.class,
            () -> authService.authenticate("testuser", "wrongpassword"));
    }

    @Test
    void shouldHashPassword() {
        String password = "mypassword";
        String hash = AuthService.BcryptUtil.hash(password);

        assertNotNull(hash);
        assertTrue(hash.startsWith("$2a$"));
    }

    @Test
    void shouldVerifyPassword() {
        String password = "mypassword";
        String hash = AuthService.BcryptUtil.hash(password);

        assertTrue(AuthService.BcryptUtil.matches(password, hash));
        assertFalse(AuthService.BcryptUtil.matches("wrongpassword", hash));
    }
}
