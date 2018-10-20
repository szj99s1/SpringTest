package spring.test.interceptor;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import spring.test.annotation.FieldCheck;
import spring.test.annotation.ParametersCheck;
import spring.test.exception.ParameterException;
import spring.test.util.RequestContextUtil;

public class ParameterCheckInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        ParametersCheck interceptor = method
                .getAnnotation(ParametersCheck.class);
        Map<String, String> signMap = RequestContextUtil.getContext();
        if (interceptor != null) {
            FieldCheck[] checks = interceptor.checks();
            for (FieldCheck check : checks) {           	
                if (StringUtils.isEmpty(request.getParameter(check.name()))) {
                    throw new ParameterException(request, check.desc() + "不能为空");
                }
            }

        }
        return true;
    }
}
