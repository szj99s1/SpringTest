package spring.test.exception;

import javax.servlet.http.HttpServletRequest;

/**
 *@Author shenzj
 *@Date 2018年10月15日下午4:08:47
 *@Description 
 */
public class SystemException extends Exception{

	public SystemException(){
		super("系统异常");
	}
}

