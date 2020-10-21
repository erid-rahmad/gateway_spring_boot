package com.multipolar.sumsel.kasda.kasdagateway.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NumberGeneratorDao {

    @Autowired
    private NamedParameterJdbcTemplate template;

    public Long getNextValueSystemTraceNumber() {
        //language=PostgreSQL
        String query = "select nextval('stan_number_seq') as row_number";
        return this.template.queryForObject(query,
                new MapSqlParameterSource(),
                (resultSet, i) -> resultSet.getLong(1));
    }

    public Long getNextValueRefNumber() {
        String query = "select nextval('ref_number_seq') as row_number";
        return this.template.queryForObject(query,
                new MapSqlParameterSource(),
                (resultSet, i) -> resultSet.getLong(1));
    }
}
