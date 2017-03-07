package application;

import application.db.Database;
import application.db.DatabaseFakeImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public Database database() {
        return new DatabaseFakeImpl();
    }
}
