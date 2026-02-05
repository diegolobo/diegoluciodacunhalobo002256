package br.com.rockstars.repository;

import br.com.rockstars.domain.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public Optional<User> findByUsername(String username) {
        return find("username", username).firstResultOptional();
    }

    public Optional<User> findByUsernameAndActive(String username, Boolean active) {
        return find("username = ?1 AND active = ?2", username, active).firstResultOptional();
    }
}
