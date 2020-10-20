package com.multipolar.sumsel.kasda.kasdagateway.service;

import com.multipolar.sumsel.kasda.kasdagateway.converter.TraceNumberGenerator;
import com.multipolar.sumsel.kasda.kasdagateway.dao.NumberGeneratorDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AtomicNumberGenerator implements TraceNumberGenerator {

    @Autowired
    private NumberGeneratorDao dao;

    @Override
    public String getTraceNumber() {
        String value = dao.getNextValueSystemTraceNumber().toString();
        if (StringUtils.length(value) > 6)
            return StringUtils.substring(value, 0, 6);
        else
            return StringUtils.leftPad(value, 6, '0');
    }

    @Override
    public String getReferenceNumber() {
        String value = dao.getNextValueRefNumber().toString();
        if (StringUtils.length(value) > 12)
            return StringUtils.substring(value, 0, 6);
        else
            return StringUtils.leftPad(value, 12, '0');
    }
}
