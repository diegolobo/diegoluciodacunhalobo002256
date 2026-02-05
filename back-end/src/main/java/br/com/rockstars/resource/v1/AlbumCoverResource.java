package br.com.rockstars.resource.v1;

import br.com.rockstars.domain.dto.AlbumCoverDTO;
import br.com.rockstars.service.AlbumCoverService;
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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.RestForm;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("/api/v1/albums/{albumId}/covers")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Capas de Albums", description = "Upload e gerenciamento de capas")
public class AlbumCoverResource {

    @Inject
    AlbumCoverService albumCoverService;

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    @Operation(summary = "Listar capas", description = "Retorna todas as capas de um album com URLs temporarias")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Lista de capas"),
        @APIResponse(responseCode = "404", description = "Album nao encontrado")
    })
    public List<AlbumCoverDTO> listCovers(@Parameter(description = "ID do album") @PathParam("albumId") Long albumId) {
        return albumCoverService.findByAlbumId(albumId);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Upload de capa", description = "Faz upload de uma capa para o album")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Capa enviada",
            content = @Content(schema = @Schema(implementation = AlbumCoverDTO.class))),
        @APIResponse(responseCode = "400", description = "Arquivo invalido (tipo ou tamanho)"),
        @APIResponse(responseCode = "404", description = "Album nao encontrado")
    })
    public Response uploadCover(
            @Parameter(description = "ID do album") @PathParam("albumId") Long albumId,
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
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Upload multiplo de capas", description = "Faz upload de varias capas para o album")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Capas enviadas"),
        @APIResponse(responseCode = "400", description = "Arquivo invalido (tipo ou tamanho)"),
        @APIResponse(responseCode = "404", description = "Album nao encontrado")
    })
    public Response uploadCovers(
            @Parameter(description = "ID do album") @PathParam("albumId") Long albumId,
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
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Remover capa", description = "Remove uma capa do album")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Capa removida"),
        @APIResponse(responseCode = "404", description = "Album ou capa nao encontrado")
    })
    public Response deleteCover(
            @Parameter(description = "ID do album") @PathParam("albumId") Long albumId,
            @Parameter(description = "ID da capa") @PathParam("coverId") Long coverId) {

        albumCoverService.deleteCover(albumId, coverId);
        return Response.noContent().build();
    }
}
