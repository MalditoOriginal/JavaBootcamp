package edu.school21.spring.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.UUID;

@Configuration
@ComponentScan(basePackages = {
    "edu.school21.spring.service.repositories",
    "edu.school21.spring.service.services"
})
public class TestApplicationConfig {

    @Bean("hikariDataSource")
    public DataSource dataSource() {
        String dbName = "testdb_" + UUID.randomUUID().toString().replace("-", "");
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName(dbName + ";MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false")
                .addScript("schema.sql")
                .build();
    }
}
