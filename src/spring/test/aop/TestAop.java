package spring.test.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TestAop {
	private static Logger logger = Logger
            .getLogger(TestAop.class);
	@Pointcut("execution(* spring.test.service.*Service*.*(..))")
	public void pointCut() {
		logger.info("pointCut");
	}
    
    //前置通知
	@Before("pointCut()")
    public void beforeTransaction(JoinPoint joinPoint){
		Object[] args= joinPoint.getArgs();
		for(Object arg:args){
			logger.info("arg:"+arg.toString());
		}
		String kinds=(String)joinPoint.getSignature().getName();
		logger.info("begin transaction ["+kinds+"]");
    }
    
    @After("pointCut()")
    public void afterTransaction(JoinPoint joinPoint){
		Object[] args= joinPoint.getArgs();
		for(Object arg:args){
			logger.info("arg:"+arg.toString());
		}
		String kinds=(String)joinPoint.getSignature().getName();
    	logger.info("after transaction ["+args+"]["+kinds+"]");
    }
    
 //  @Around("pointCut()") 
    public Object around(ProceedingJoinPoint jp) throws Throwable{
		String kinds=(String)jp.getKind();
    	logger.info("around begin transaction["+kinds+"]");
        
    	try{
			Object result = jp.proceed();

			logger.info("调用了 "+jp.getTarget()+" 的 "+jp.getSignature().getName()
					+" 方法。");
			return result;
		}catch(Exception e){
			logger.error(jp.getSignature().getName()+" Exception...方法发生异常： "+e);
			throw e;
		}finally{
			logger.info(jp.getSignature().getName()+" ENDING.....方法执行结束");
		}        

    }
    
}