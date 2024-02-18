package sg.nus.iss.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;

@Configuration
public class MainWebAppInitializer implements WebApplicationInitializer {

    private static final String TMP_FOLDER = "/tmp"; 
    private static final int MAX_UPLOAD_SIZE = 5 * 1024 * 1024; 
    
    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        
        ServletRegistration.Dynamic appServlet = sc.addServlet("mvc", new DispatcherServlet(
          new GenericWebApplicationContext()));

        appServlet.setLoadOnStartup(1);
        
        final MultipartConfigElement multipartConfigElement = new MultipartConfigElement(TMP_FOLDER, 
          MAX_UPLOAD_SIZE, MAX_UPLOAD_SIZE * 2L, MAX_UPLOAD_SIZE / 2);
        
        appServlet.setMultipartConfig(multipartConfigElement);
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
