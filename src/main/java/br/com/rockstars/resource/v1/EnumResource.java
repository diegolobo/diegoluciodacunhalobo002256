package br.com.rockstars.resource.v1;

import br.com.rockstars.domain.enums.ArtistType;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;

@Path("/api/v1/enums")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Enums", description = "Valores de enumeracoes do sistema")
public class EnumResource {

    @GET
    @Path("/artist-types")
    @PermitAll
    @Operation(summary = "Listar tipos de artista", description = "Retorna os valores possiveis para tipo de artista")
    @APIResponse(responseCode = "200", description = "Lista de tipos de artista")
    public List<ArtistType> getArtistTypes() {
        return Arrays.asList(ArtistType.values());
    }
}
