package com.shoppertrak.exampleappname.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
public class ExampleRepository {

    JdbcTemplate jdbcTemplate

    @Autowired
    ExampleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate
    }

    Object[] example_query(String exampleArg1, String exampleArg2) {
        jdbcTemplate.queryForList(
                ExampleQueries.EXAMPLE_QUERY,
                [exampleArg1, exampleArg2] as Object[]
        )
    }
}
