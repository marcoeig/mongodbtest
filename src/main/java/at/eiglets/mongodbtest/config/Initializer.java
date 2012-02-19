package at.eiglets.mongodbtest.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class Initializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext context) throws ServletException {
		createSpringRootContext(context);
		createSpringWebContext(context);
	}

	private void createSpringRootContext(ServletContext context) {
		final AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(AppConfiguration.class);
		context.addListener(new ContextLoaderListener(rootContext));
	}

	private void createSpringWebContext(ServletContext context) {
		final AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext();
		mvcContext.register(WebConfiguration.class);
		final ServletRegistration.Dynamic dispatcher = context.addServlet(
				"dispatcher", new DispatcherServlet(mvcContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
		
		context.addFilter("hiddenHttpMethodFilter",
				new HiddenHttpMethodFilter()).addMappingForServletNames(
				EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD),
				false, "dispatcher");
	}
	
}
