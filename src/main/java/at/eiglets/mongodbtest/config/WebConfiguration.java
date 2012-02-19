package at.eiglets.mongodbtest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

//http://static.springsource.org/spring/docs/current/spring-framework-reference/html/mvc.html
@EnableWebMvc
@Configuration
@ComponentScan(value = "at.eiglets.mongodbtest.web")
public class WebConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("/public-resources/")
				.setCachePeriod(31556926);
	}

	@Bean
	public InternalResourceViewResolver configureInternalResourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	// @Bean
	// public MultipartResolver multipartResolver() {
	// return new CommonsMultipartResolver();
	// }

}
