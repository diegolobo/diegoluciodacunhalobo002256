package br.com.rockstars.resource;

import br.com.rockstars.domain.dto.ArtistDTO;
import br.com.rockstars.service.ArtistService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
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
    public List<ArtistDTO> findAll() {
        return artistService.findAll();
    }

    @GET
    @Path("/{id}")
    public ArtistDTO findById(@PathParam("id") Long id) {
        return artistService.findById(id);
    }

    @POST
    public Response create(@Valid ArtistDTO dto) {
        ArtistDTO created = artistService.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public ArtistDTO update(@PathParam("id") Long id, @Valid ArtistDTO dto) {
        return artistService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        artistService.delete(id);
        return Response.noContent().build();
    }
}
