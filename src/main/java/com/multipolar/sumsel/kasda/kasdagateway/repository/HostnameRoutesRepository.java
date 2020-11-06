package com.multipolar.sumsel.kasda.kasdagateway.repository;

import com.multipolar.sumsel.kasda.kasdagateway.entity.HostnameRoutes;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface HostnameRoutesRepository extends CrudRepository<HostnameRoutes, String> {

    Optional<HostnameRoutes> findByServiceIdAndEnvironment(String serviceId, String env);

    Optional<HostnameRoutes> findByServiceIdAndKodeCabangAndKodeWilayahAndEnvironment(
            String serviceId, String kodeCabang, String kodeWilayah, String env);
}
