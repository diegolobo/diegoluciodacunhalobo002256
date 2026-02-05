package br.com.rockstars.client;

import br.com.rockstars.domain.dto.ExternalRegionalDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import java.util.List;

@Path("/regionais")
@RegisterRestClient(configKey = "regional-api")
public interface RegionalClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<ExternalRegionalDTO> getAll();
}
