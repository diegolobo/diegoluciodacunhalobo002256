package br.com.rockstars.resource;

import br.com.rockstars.domain.dto.AlbumCoverDTO;
import br.com.rockstars.service.AlbumCoverService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.RestForm;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("/api/albums/{albumId}/covers")
@Produces(MediaType.APPLICATION_JSON)
public class AlbumCoverResource {

    @Inject
    AlbumCoverService albumCoverService;

    @GET
    @PermitAll
    public List<AlbumCoverDTO> listCovers(@PathParam("albumId") Long albumId) {
        return albumCoverService.findByAlbumId(albumId);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed({"admin"})
    public Response uploadCover(
            @PathParam("albumId") Long albumId,
            @RestForm("file") FileUpload file) throws IOException {

        try (FileInputStream inputStream = new FileInputStream(file.uploadedFile().toFile())) {
            AlbumCoverDTO cover = albumCoverService.uploadCover(
                albumId,
                inputStream,
                file.fileName(),
                file.contentType(),
                file.size()
            );
            return Response.status(Response.Status.CREATED).entity(cover).build();
        }
    }

    @POST
    @Path("/batch")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed({"admin"})
    public Response uploadCovers(
            @PathParam("albumId") Long albumId,
            @RestForm("files") List<FileUpload> files) throws IOException {

        List<AlbumCoverService.FileUpload> fileUploads = new ArrayList<>();
        for (FileUpload file : files) {
            fileUploads.add(new AlbumCoverService.FileUpload(
                new FileInputStream(file.uploadedFile().toFile()),
                file.fileName(),
                file.contentType(),
                file.size()
            ));
        }

        List<AlbumCoverDTO> covers = albumCoverService.uploadCovers(albumId, fileUploads);
        return Response.status(Response.Status.CREATED).entity(covers).build();
    }

    @DELETE
    @Path("/{coverId}")
    @RolesAllowed({"admin"})
    public Response deleteCover(
            @PathParam("albumId") Long albumId,
            @PathParam("coverId") Long coverId) {

        albumCoverService.deleteCover(albumId, coverId);
        return Response.noContent().build();
    }
}
