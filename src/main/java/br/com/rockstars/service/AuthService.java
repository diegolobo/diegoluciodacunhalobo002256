package br.com.rockstars.service;

import br.com.rockstars.domain.dto.TokenResponseDTO;
import br.com.rockstars.domain.entity.User;
import br.com.rockstars.exception.BusinessException;
import br.com.rockstars.repository.UserRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class AuthService {

    @Inject
    UserRepository userRepository;

    @Inject
    JsonWebToken jwt;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @ConfigProperty(name = "smallrye.jwt.new-token.lifespan", defaultValue = "300")
    Long tokenLifespan;

    public TokenResponseDTO authenticate(String username, String password) {
        User user = userRepository.findByUsernameAndActive(username, true)
            .orElseThrow(() -> new BusinessException("Usuario ou senha invalidos"));

        if (!verifyPassword(password, user.getPassword())) {
            throw new BusinessException("Usuario ou senha invalidos");
        }

        String token = generateToken(user);
        return TokenResponseDTO.of(token, tokenLifespan);
    }

    public TokenResponseDTO refreshToken() {
        if (jwt == null || jwt.getName() == null) {
            throw new BusinessException("Token invalido");
        }

        String username = jwt.getName();
        User user = userRepository.findByUsernameAndActive(username, true)
            .orElseThrow(() -> new BusinessException("Usuario nao encontrado ou inativo"));

        String token = generateToken(user);
        return TokenResponseDTO.of(token, tokenLifespan);
    }

    private String generateToken(User user) {
        Set<String> groups = new HashSet<>();
        groups.add(user.getRole());

        Instant now = Instant.now();
        Instant expiry = now.plus(Duration.ofSeconds(tokenLifespan));

        return Jwt.issuer(issuer)
            .subject(user.getUsername())
            .groups(groups)
            .claim("userId", user.getId())
            .issuedAt(now)
            .expiresAt(expiry)
            .sign();
    }

    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BcryptUtil.matches(plainPassword, hashedPassword);
    }

    public static class BcryptUtil {
        private static final org.mindrot.jbcrypt.BCrypt bcrypt = new org.mindrot.jbcrypt.BCrypt();

        public static String hash(String password) {
            return org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt(10));
        }

        public static boolean matches(String plainPassword, String hashedPassword) {
            return org.mindrot.jbcrypt.BCrypt.checkpw(plainPassword, hashedPassword);
        }
    }
}
