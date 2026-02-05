package br.com.rockstars.service;

import br.com.rockstars.client.RegionalClient;
import br.com.rockstars.domain.dto.ExternalRegionalDTO;
import br.com.rockstars.domain.dto.RegionalDTO;
import br.com.rockstars.domain.dto.SyncResultDTO;
import br.com.rockstars.domain.entity.Regional;
import br.com.rockstars.repository.RegionalRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class RegionalSyncService {

    @Inject
    RegionalRepository regionalRepository;

    @Inject
    @RestClient
    RegionalClient regionalClient;

    public List<RegionalDTO> findAllActive() {
        return regionalRepository.findAllActive().stream()
            .map(RegionalDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional
    public SyncResultDTO synchronize() {
        List<ExternalRegionalDTO> externalRegionals = regionalClient.getAll();
        List<Regional> localRegionals = regionalRepository.findAllActive();

        // Usar Map para complexidade O(1) nas buscas
        Map<Long, Regional> localByExternalId = new HashMap<>();
        for (Regional local : localRegionals) {
            localByExternalId.put(local.getExternalId(), local);
        }

        Set<Long> externalIds = externalRegionals.stream()
            .map(ExternalRegionalDTO::getId)
            .collect(Collectors.toSet());

        int inserted = 0;
        int updated = 0;
        int deactivated = 0;

        // Processar regionais do endpoint externo
        for (ExternalRegionalDTO external : externalRegionals) {
            Regional local = localByExternalId.get(external.getId());

            if (local == null) {
                // Novo no endpoint -> inserir
                Regional newRegional = new Regional(external.getId(), external.getNome());
                regionalRepository.persist(newRegional);
                inserted++;
            } else if (!local.getName().equals(external.getNome())) {
                // Atributo alterado -> inativar antigo + criar novo
                local.setActive(false);
                regionalRepository.persist(local);

                Regional newRegional = new Regional(external.getId(), external.getNome());
                regionalRepository.persist(newRegional);
                updated++;
            }
            // Se nome igual, não faz nada
        }

        // Verificar regionais locais que não estão mais no endpoint
        for (Regional local : localRegionals) {
            if (!externalIds.contains(local.getExternalId())) {
                // Ausente no endpoint -> inativar
                local.setActive(false);
                regionalRepository.persist(local);
                deactivated++;
            }
        }

        return new SyncResultDTO(inserted, updated, deactivated);
    }
}
