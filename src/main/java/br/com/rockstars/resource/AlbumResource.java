package br.com.rockstars.resource;

import br.com.rockstars.domain.dto.AlbumDTO;
import br.com.rockstars.domain.dto.ArtistDTO;
import br.com.rockstars.domain.dto.PageResponseDTO;
import br.com.rockstars.domain.enums.ArtistType;
import br.com.rockstars.service.AlbumService;
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

@Path("/api/albums")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlbumResource {

    @Inject
    AlbumService albumService;

    @GET
    public PageResponseDTO<AlbumDTO> findAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("title") String title,
            @QueryParam("artistId") Long artistId,
            @QueryParam("artistType") ArtistType artistType,
            @QueryParam("active") Boolean active,
            @QueryParam("sort") @DefaultValue("title") String sortField,
            @QueryParam("direction") @DefaultValue("asc") String sortDirection) {
        return albumService.findAll(page, size, title, artistId, artistType, active, sortField, sortDirection);
    }

    @GET
    @Path("/{id}")
    public AlbumDTO findById(@PathParam("id") Long id) {
        return albumService.findById(id);
    }

    @GET
    @Path("/{id}/artists")
    public List<ArtistDTO> findArtistsByAlbumId(@PathParam("id") Long id) {
        return albumService.findArtistsByAlbumId(id);
    }

    @POST
    public Response create(@Valid AlbumDTO dto) {
        AlbumDTO created = albumService.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public AlbumDTO update(@PathParam("id") Long id, @Valid AlbumDTO dto) {
        return albumService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        albumService.delete(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/artists/{artistId}")
    public AlbumDTO addArtist(@PathParam("id") Long albumId, @PathParam("artistId") Long artistId) {
        return albumService.addArtistToAlbum(albumId, artistId);
    }

    @DELETE
    @Path("/{id}/artists/{artistId}")
    public AlbumDTO removeArtist(@PathParam("id") Long albumId, @PathParam("artistId") Long artistId) {
        return albumService.removeArtistFromAlbum(albumId, artistId);
    }
}
