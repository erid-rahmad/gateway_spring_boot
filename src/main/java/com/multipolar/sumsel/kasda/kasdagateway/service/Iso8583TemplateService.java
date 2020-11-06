package com.multipolar.sumsel.kasda.kasdagateway.service;

import com.multipolar.sumsel.kasda.kasdagateway.dao.Iso8583TemplateDao;
import com.multipolar.sumsel.kasda.kasdagateway.entity.Iso858eTemplateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class Iso8583TemplateService {

    @Autowired
    private Iso8583TemplateDao dao;

    public Optional<Iso858eTemplateRequest> findByServiceNameAndHttpMethodAndMtiAndPCode(String serviceName, String mti, String pCode) {
        return dao.findByServiceNameAndMtiAndPCode(serviceName, mti, pCode);
    }
}
