package br.com.rockstars.resource.v1;

import br.com.rockstars.domain.dto.AlbumDTO;
import br.com.rockstars.domain.dto.ArtistDTO;
import br.com.rockstars.domain.dto.ArtistRequestDTO;
import br.com.rockstars.domain.dto.PageResponseDTO;
import br.com.rockstars.domain.enums.ArtistType;
import br.com.rockstars.service.ArtistService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;

@Path("/api/v1/artists")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Artistas", description = "Gerenciamento de artistas")
public class ArtistResource {

    @Inject
    ArtistService artistService;

    @GET
    @PermitAll
    @Operation(summary = "Listar artistas", description = "Retorna lista paginada de artistas com filtros")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Lista de artistas",
            content = @Content(schema = @Schema(implementation = PageResponseDTO.class)))
    })
    public PageResponseDTO<ArtistDTO> findAll(
            @Parameter(description = "Numero da pagina (0-indexed)") @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(description = "Tamanho da pagina") @QueryParam("size") @DefaultValue("10") int size,
            @Parameter(description = "Filtro por nome") @QueryParam("name") String name,
            @Parameter(description = "Filtro por tipo (SOLO, BAND)") @QueryParam("type") ArtistType type,
            @Parameter(description = "Filtro por status ativo") @QueryParam("active") Boolean active,
            @Parameter(description = "Campo para ordenacao") @QueryParam("sort") @DefaultValue("name") String sortField,
            @Parameter(description = "Direcao da ordenacao (asc, desc)") @QueryParam("direction") @DefaultValue("asc") String sortDirection) {
        return artistService.findAll(page, size, name, type, active, sortField, sortDirection);
    }

    @GET
    @Path("/{id}")
    @PermitAll
    @Operation(summary = "Buscar artista por ID", description = "Retorna um artista pelo seu ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Artista encontrado",
            content = @Content(schema = @Schema(implementation = ArtistDTO.class))),
        @APIResponse(responseCode = "404", description = "Artista nao encontrado")
    })
    public ArtistDTO findById(@Parameter(description = "ID do artista") @PathParam("id") Long id) {
        return artistService.findById(id);
    }

    @GET
    @Path("/{id}/albums")
    @PermitAll
    @Operation(summary = "Listar albuns do artista", description = "Retorna todos os albuns de um artista")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Lista de albuns"),
        @APIResponse(responseCode = "404", description = "Artista nao encontrado")
    })
    public List<AlbumDTO> findAlbumsByArtistId(@Parameter(description = "ID do artista") @PathParam("id") Long id) {
        return artistService.findAlbumsByArtistId(id);
    }

    @POST
    @RolesAllowed({"admin"})
    @Operation(summary = "Criar artista", description = "Cria um novo artista")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Artista criado",
            content = @Content(schema = @Schema(implementation = ArtistDTO.class))),
        @APIResponse(responseCode = "400", description = "Dados invalidos"),
        @APIResponse(responseCode = "401", description = "Nao autenticado"),
        @APIResponse(responseCode = "403", description = "Sem permissao")
    })
    public Response create(@Valid ArtistRequestDTO dto) {
        ArtistDTO created = artistService.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"admin"})
    @Operation(summary = "Atualizar artista", description = "Atualiza um artista existente")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Artista atualizado",
            content = @Content(schema = @Schema(implementation = ArtistDTO.class))),
        @APIResponse(responseCode = "400", description = "Dados invalidos"),
        @APIResponse(responseCode = "404", description = "Artista nao encontrado")
    })
    public ArtistDTO update(@Parameter(description = "ID do artista") @PathParam("id") Long id, @Valid ArtistRequestDTO dto) {
        return artistService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"admin"})
    @Operation(summary = "Inativar artista", description = "Inativa um artista (soft delete)")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Artista inativado"),
        @APIResponse(responseCode = "404", description = "Artista nao encontrado")
    })
    public Response delete(@Parameter(description = "ID do artista") @PathParam("id") Long id) {
        artistService.delete(id);
        return Response.noContent().build();
    }
}
