package br.com.rockstars.resource.v1;

import br.com.rockstars.domain.dto.AlbumDTO;
import br.com.rockstars.domain.dto.AlbumRequestDTO;
import br.com.rockstars.domain.dto.ArtistDTO;
import br.com.rockstars.domain.dto.PageResponseDTO;
import br.com.rockstars.domain.enums.ArtistType;
import br.com.rockstars.service.AlbumService;
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

@Path("/api/v1/albums")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Albums", description = "Gerenciamento de albums")
public class AlbumResource {

    @Inject
    AlbumService albumService;

    @GET
    @PermitAll
    @Operation(summary = "Listar albums", description = "Retorna lista paginada de albums com filtros")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Lista de albums",
            content = @Content(schema = @Schema(implementation = PageResponseDTO.class)))
    })
    public PageResponseDTO<AlbumDTO> findAll(
            @Parameter(description = "Numero da pagina (0-indexed)") @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(description = "Tamanho da pagina") @QueryParam("size") @DefaultValue("10") int size,
            @Parameter(description = "Filtro por titulo") @QueryParam("title") String title,
            @Parameter(description = "Filtro por ID do artista") @QueryParam("artistId") Long artistId,
            @Parameter(description = "Filtro por tipo de artista") @QueryParam("artistType") ArtistType artistType,
            @Parameter(description = "Filtro por status ativo") @QueryParam("active") Boolean active,
            @Parameter(description = "Campo para ordenacao") @QueryParam("sort") @DefaultValue("title") String sortField,
            @Parameter(description = "Direcao da ordenacao (asc, desc)") @QueryParam("direction") @DefaultValue("asc") String sortDirection) {
        return albumService.findAll(page, size, title, artistId, artistType, active, sortField, sortDirection);
    }

    @GET
    @Path("/{id}")
    @PermitAll
    @Operation(summary = "Buscar album por ID", description = "Retorna um album pelo seu ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Album encontrado",
            content = @Content(schema = @Schema(implementation = AlbumDTO.class))),
        @APIResponse(responseCode = "404", description = "Album nao encontrado")
    })
    public AlbumDTO findById(@Parameter(description = "ID do album") @PathParam("id") Long id) {
        return albumService.findById(id);
    }

    @GET
    @Path("/{id}/artists")
    @PermitAll
    @Operation(summary = "Listar artistas do album", description = "Retorna todos os artistas de um album")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Lista de artistas"),
        @APIResponse(responseCode = "404", description = "Album nao encontrado")
    })
    public List<ArtistDTO> findArtistsByAlbumId(@Parameter(description = "ID do album") @PathParam("id") Long id) {
        return albumService.findArtistsByAlbumId(id);
    }

    @POST
    @RolesAllowed({"admin"})
    @Operation(summary = "Criar album", description = "Cria um novo album")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Album criado",
            content = @Content(schema = @Schema(implementation = AlbumDTO.class))),
        @APIResponse(responseCode = "400", description = "Dados invalidos"),
        @APIResponse(responseCode = "401", description = "Nao autenticado"),
        @APIResponse(responseCode = "403", description = "Sem permissao")
    })
    public Response create(@Valid AlbumRequestDTO dto) {
        AlbumDTO created = albumService.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"admin"})
    @Operation(summary = "Atualizar album", description = "Atualiza um album existente")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Album atualizado",
            content = @Content(schema = @Schema(implementation = AlbumDTO.class))),
        @APIResponse(responseCode = "400", description = "Dados invalidos"),
        @APIResponse(responseCode = "404", description = "Album nao encontrado")
    })
    public AlbumDTO update(@Parameter(description = "ID do album") @PathParam("id") Long id, @Valid AlbumRequestDTO dto) {
        return albumService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"admin"})
    @Operation(summary = "Inativar album", description = "Inativa um album (soft delete)")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Album inativado"),
        @APIResponse(responseCode = "404", description = "Album nao encontrado")
    })
    public Response delete(@Parameter(description = "ID do album") @PathParam("id") Long id) {
        albumService.delete(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/artists/{artistId}")
    @RolesAllowed({"admin"})
    @Operation(summary = "Associar artista ao album", description = "Adiciona um artista ao album")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Artista associado",
            content = @Content(schema = @Schema(implementation = AlbumDTO.class))),
        @APIResponse(responseCode = "400", description = "Artista ja associado"),
        @APIResponse(responseCode = "404", description = "Album ou artista nao encontrado")
    })
    public AlbumDTO addArtist(
            @Parameter(description = "ID do album") @PathParam("id") Long albumId,
            @Parameter(description = "ID do artista") @PathParam("artistId") Long artistId) {
        return albumService.addArtistToAlbum(albumId, artistId);
    }

    @DELETE
    @Path("/{id}/artists/{artistId}")
    @RolesAllowed({"admin"})
    @Operation(summary = "Remover artista do album", description = "Remove um artista do album")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Artista removido",
            content = @Content(schema = @Schema(implementation = AlbumDTO.class))),
        @APIResponse(responseCode = "400", description = "Artista nao associado"),
        @APIResponse(responseCode = "404", description = "Album ou artista nao encontrado")
    })
    public AlbumDTO removeArtist(
            @Parameter(description = "ID do album") @PathParam("id") Long albumId,
            @Parameter(description = "ID do artista") @PathParam("artistId") Long artistId) {
        return albumService.removeArtistFromAlbum(albumId, artistId);
    }
}
