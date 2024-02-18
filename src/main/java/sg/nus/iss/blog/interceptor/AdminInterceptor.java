package sg.nus.iss.blog.interceptor;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sg.nus.iss.blog.model.BlogUser;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		HttpSession sessionObj = request.getSession();
		BlogUser activeUser = (BlogUser) sessionObj.getAttribute("activeUser");
		if (activeUser != null && activeUser.getEmail().equals("nblog_12@outlook.com")) {
			return true;
		}
		else {
			response.sendRedirect("/account/login");
			return false;
		}
	} 
}
