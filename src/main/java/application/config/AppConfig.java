//package application.config;
//
//import org.flywaydb.core.Flyway;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//
//@Configuration
//public class AppConfig {
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public AppConfig(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Bean(initMethod = "migrate")
//    Flyway flyway() {
//
//        final Flyway flyway = new Flyway();
//
//        flyway.setBaselineOnMigrate(true);
//        flyway.setDataSource(jdbcTemplate.getDataSource());
//        flyway.setLocations("file:src/main/resources/db/migration/");
//
//
//        flyway.migrate();
//        return flyway;
//    }
//}

