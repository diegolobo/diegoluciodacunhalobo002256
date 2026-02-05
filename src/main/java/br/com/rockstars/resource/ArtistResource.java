package br.com.rockstars.resource;

import br.com.rockstars.domain.dto.AlbumDTO;
import br.com.rockstars.domain.dto.ArtistDTO;
import br.com.rockstars.domain.dto.ArtistRequestDTO;
import br.com.rockstars.domain.dto.PageResponseDTO;
import br.com.rockstars.domain.enums.ArtistType;
import br.com.rockstars.service.ArtistService;
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
import java.util.List;

@Path("/api/artists")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArtistResource {

    @Inject
    ArtistService artistService;

    @GET
    public PageResponseDTO<ArtistDTO> findAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("name") String name,
            @QueryParam("type") ArtistType type,
            @QueryParam("active") Boolean active,
            @QueryParam("sort") @DefaultValue("name") String sortField,
            @QueryParam("direction") @DefaultValue("asc") String sortDirection) {
        return artistService.findAll(page, size, name, type, active, sortField, sortDirection);
    }

    @GET
    @Path("/{id}")
    public ArtistDTO findById(@PathParam("id") Long id) {
        return artistService.findById(id);
    }

    @GET
    @Path("/{id}/albums")
    public List<AlbumDTO> findAlbumsByArtistId(@PathParam("id") Long id) {
        return artistService.findAlbumsByArtistId(id);
    }

    @POST
    public Response create(@Valid ArtistRequestDTO dto) {
        ArtistDTO created = artistService.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public ArtistDTO update(@PathParam("id") Long id, @Valid ArtistRequestDTO dto) {
        return artistService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        artistService.delete(id);
        return Response.noContent().build();
    }
}
