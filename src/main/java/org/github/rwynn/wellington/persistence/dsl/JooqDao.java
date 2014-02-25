package org.github.rwynn.wellington.persistence.dsl;


import org.github.rwynn.wellington.properties.DatabaseProperties;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class JooqDao {

    @Autowired
    DatabaseProperties databaseProperties;

    @Autowired
    JdbcTemplate jdbcTemplate;

    protected final DSLContext getDslContext() {
        Settings settings = new Settings();
        settings.setStatementType(StatementType.STATIC_STATEMENT);
        String jooqDialect = databaseProperties.getJooqDialect();
        DSLContext context = DSL.using(SQLDialect.valueOf(jooqDialect), settings);
        return context;
    }
}
