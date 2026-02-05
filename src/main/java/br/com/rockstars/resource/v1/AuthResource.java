package br.com.rockstars.resource.v1;

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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/v1/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Autenticacao", description = "Endpoints de autenticacao")
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    @PermitAll
    @Operation(summary = "Realizar login", description = "Autentica usuario e retorna token JWT")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Login realizado com sucesso",
            content = @Content(schema = @Schema(implementation = TokenResponseDTO.class))),
        @APIResponse(responseCode = "400", description = "Usuario ou senha invalidos")
    })
    public TokenResponseDTO login(@Valid LoginRequestDTO request) {
        return authService.authenticate(request.getUsername(), request.getPassword());
    }

    @POST
    @Path("/refresh")
    @RolesAllowed({"admin", "user"})
    @Operation(summary = "Renovar token", description = "Renova o token JWT do usuario autenticado")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Token renovado",
            content = @Content(schema = @Schema(implementation = TokenResponseDTO.class))),
        @APIResponse(responseCode = "401", description = "Token invalido ou expirado")
    })
    public TokenResponseDTO refresh() {
        return authService.refreshToken();
    }
}
