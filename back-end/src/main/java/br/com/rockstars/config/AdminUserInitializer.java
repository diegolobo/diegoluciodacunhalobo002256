package br.com.rockstars.config;

import br.com.rockstars.domain.entity.User;
import br.com.rockstars.repository.UserRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

@ApplicationScoped
public class AdminUserInitializer {

    private static final Logger LOG = Logger.getLogger(AdminUserInitializer.class);

    @Inject
    UserRepository userRepository;

    @ConfigProperty(name = "app.admin.username", defaultValue = "admin")
    String adminUsername;

    @ConfigProperty(name = "app.admin.password", defaultValue = "admin123")
    String adminPassword;

    @ConfigProperty(name = "app.admin.enabled", defaultValue = "true")
    boolean adminInitEnabled;

    @Transactional
    void onStart(@Observes StartupEvent event) {
        if (!adminInitEnabled) {
            LOG.info("Admin user initialization is disabled");
            return;
        }

        LOG.infof("Checking if admin user '%s' exists...", adminUsername);

        Optional<User> existingUser = userRepository.findByUsername(adminUsername);

        if (existingUser.isEmpty()) {
            LOG.infof("Admin user '%s' not found. Creating...", adminUsername);

            String hashedPassword = BCrypt.hashpw(adminPassword, BCrypt.gensalt(10));

            User adminUser = new User(adminUsername, hashedPassword, "ADMIN");
            userRepository.persist(adminUser);

            LOG.infof("Admin user '%s' created successfully with role ADMIN", adminUsername);
        } else {
            LOG.infof("Admin user '%s' already exists. Skipping creation.", adminUsername);
        }
    }
}
