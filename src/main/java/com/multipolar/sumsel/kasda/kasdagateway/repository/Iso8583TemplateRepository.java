package com.multipolar.sumsel.kasda.kasdagateway.repository;

import com.multipolar.sumsel.kasda.kasdagateway.entity.Iso858eTemplateRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface Iso8583TemplateRepository extends CrudRepository<Iso858eTemplateRequest, String> {

    @Query("from Iso858eTemplateRequest where serviceName = :serviceName and mti = :mti and pCode = :pCode")
    Optional<Iso858eTemplateRequest> findByServiceNameAndMtiAndPCode(String serviceName, String mti, String pCode);
}
