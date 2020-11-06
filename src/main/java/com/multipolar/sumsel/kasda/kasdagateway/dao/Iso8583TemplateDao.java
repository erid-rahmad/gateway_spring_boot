package com.multipolar.sumsel.kasda.kasdagateway.dao;

import com.multipolar.sumsel.kasda.kasdagateway.entity.Iso858eTemplateRequest;
import com.multipolar.sumsel.kasda.kasdagateway.repository.Iso8583TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class Iso8583TemplateDao {

    @Autowired
    private Iso8583TemplateRepository repo;

    public Optional<Iso858eTemplateRequest> findByServiceNameAndMtiAndPCode(String serviceName, String mti, String pCode) {
        return repo.findByServiceNameAndMtiAndPCode(serviceName, mti, pCode);
    }
}
