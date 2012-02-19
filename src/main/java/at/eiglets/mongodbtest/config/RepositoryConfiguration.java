package at.eiglets.mongodbtest.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("at.eiglets.mongodbtest.repository")
public class RepositoryConfiguration {
	
}
