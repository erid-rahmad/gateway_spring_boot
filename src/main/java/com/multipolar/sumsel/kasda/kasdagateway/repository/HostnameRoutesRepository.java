package com.multipolar.sumsel.kasda.kasdagateway.repository;

import com.multipolar.sumsel.kasda.kasdagateway.model.HostnameRoutes;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface HostnameRoutesRepository extends CrudRepository<HostnameRoutes, String> {

    Optional<HostnameRoutes> findByServiceIdAndEnvironment(String serviceId, String env);
}
