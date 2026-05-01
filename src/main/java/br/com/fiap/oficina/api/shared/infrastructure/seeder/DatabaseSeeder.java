package br.com.fiap.oficina.api.shared.infrastructure.seeder;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class DatabaseSeeder {

    @Inject
    Instance<Seeder> seeders;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        String profile = ConfigUtils.getProfiles().iterator().next();
        
        if (!"prod".equals(profile)) {
            log.info("Iniciando semeadura do banco de dados (Profile: {})...", profile);
            
            seeders.stream().forEach(seeder -> {
                log.info("Executando seeder: {}", seeder.getClass().getSimpleName());
                seeder.execute();
            });
            
            log.info("Semeadura concluída com sucesso.");
        } else {
            log.info("Semeadura ignorada: Ambiente de produção detectado.");
        }
    }
}
