package br.com.rockstars.resource.v1;

import br.com.rockstars.domain.dto.RegionalDTO;
import br.com.rockstars.domain.dto.SyncResultDTO;
import br.com.rockstars.service.RegionalSyncService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
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
import java.util.List;

@Path("/api/v1/regionais")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Regionais", description = "Gerenciamento e sincronizacao de regionais")
public class RegionalResource {

    @Inject
    RegionalSyncService regionalSyncService;

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    @Operation(summary = "Listar regionais", description = "Retorna todas as regionais ativas")
    @APIResponse(responseCode = "200", description = "Lista de regionais")
    public List<RegionalDTO> findAll() {
        return regionalSyncService.findAllActive();
    }

    @POST
    @Path("/sync")
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Sincronizar regionais", description = "Sincroniza regionais com endpoint externo")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Sincronizacao realizada",
            content = @Content(schema = @Schema(implementation = SyncResultDTO.class))),
        @APIResponse(responseCode = "500", description = "Erro ao sincronizar")
    })
    public SyncResultDTO sync() {
        return regionalSyncService.synchronize();
    }
}
