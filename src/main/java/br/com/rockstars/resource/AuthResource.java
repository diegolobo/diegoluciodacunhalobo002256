package br.com.rockstars.resource;

import br.com.rockstars.domain.dto.LoginRequestDTO;
import br.com.rockstars.domain.dto.TokenResponseDTO;
import br.com.rockstars.service.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    @PermitAll
    public TokenResponseDTO login(@Valid LoginRequestDTO request) {
        return authService.authenticate(request.getUsername(), request.getPassword());
    }

    @POST
    @Path("/refresh")
    @RolesAllowed({"admin", "user"})
    public TokenResponseDTO refresh() {
        return authService.refreshToken();
    }
}
