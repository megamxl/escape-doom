package lector.portal.dataaccess.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "lector.portal.dataaccess")
@EntityScan(basePackages = "lector.portal.dataaccess.entity")
public class JpaConfig {
}
