package spring.test.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class ExceptionResolver implements HandlerExceptionResolver {
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		String message = ex.getMessage();
		ModelMap model = new ModelMap();
		model.put("message", message);
		return new ModelAndView("error", model);
	}
}
