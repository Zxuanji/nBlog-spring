package sg.nus.iss.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import sg.nus.iss.blog.interceptor.AdminInterceptor;
import sg.nus.iss.blog.interceptor.SecurityInterceptor;

@Component
public class WebAppConfig implements WebMvcConfigurer {
	
	@Autowired
	SecurityInterceptor securityInterceptor;
	@Autowired
	AdminInterceptor adminInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(securityInterceptor).addPathPatterns("/home/*", "/user/*");
		registry.addInterceptor(adminInterceptor).addPathPatterns("/admin/*");
	}
}

