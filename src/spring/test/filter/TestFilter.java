package spring.test.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

/**
 *@Author shenzj
 *@Date 2018年10月16日上午8:50:35
 *@Description
 *
 */
public class TestFilter implements Filter{
	private static Logger logger = Logger
            .getLogger(TestFilter.class);
	public void init(FilterConfig config) throws ServletException {    // 初始化过滤器
        String initParam = config.getInitParameter("ref");     // 取得初始化参数
        logger.info("** 过滤器初始化，初始化参数 = " + initParam);
    }
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {    // 执行过滤
    	logger.info("** 执行doFilter()方法之前。");
        chain.doFilter(request, response);         // 将请求继续传递
        logger.info("** 执行doFilter()方法之后。");
    }
    public void destroy() {                // 销毁过滤
        logger.info("** 过滤器销毁。");
    }
}

