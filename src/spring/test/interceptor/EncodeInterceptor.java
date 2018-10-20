package spring.test.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class EncodeInterceptor extends HandlerInterceptorAdapter {
	private static Logger logger = Logger
            .getLogger(EncodeInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        request.setCharacterEncoding("UTF-8");
        logger.info("EncodeInterceptor preHandle");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        response.setCharacterEncoding("UTF-8");
        logger.info("EncodeInterceptor postHandle");

    }
}
