package at.eiglets.mongodbtest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ ServiceConfiguration.class, RepositoryConfiguration.class,
		MongoDBConfiguration.class })
public class AppConfiguration {

}
