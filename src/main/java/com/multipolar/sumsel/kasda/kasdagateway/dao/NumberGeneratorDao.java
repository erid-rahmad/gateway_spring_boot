package com.multipolar.sumsel.kasda.kasdagateway.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NumberGeneratorDao {

    @Autowired
    private NamedParameterJdbcTemplate template;

    public Long getNextValueSystemTraceNumber() {
        return 0l;
    }

    public Long getNextValueRefNumber() {
        return 0l;
    }
}
