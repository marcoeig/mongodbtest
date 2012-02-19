package at.eiglets.mongodbtest.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class Initializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext context) throws ServletException {
		createSpringWebContext(context);

		context.addFilter("hiddenHttpMethodFilter",
				new HiddenHttpMethodFilter()).addMappingForServletNames(
				EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD),
				false, "dispatcher");

	}

	private void createSpringWebContext(ServletContext context) {
		final AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext();
		mvcContext.register(WebConfiguration.class, AppConfiguration.class);
		final ServletRegistration.Dynamic dispatcher = context.addServlet(
				"dispatcher", new DispatcherServlet(mvcContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}
}
