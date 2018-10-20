package spring.test.exception;

import javax.servlet.http.HttpServletRequest;

public class ParameterException extends Exception{

	public ParameterException(HttpServletRequest request, String message){
		super(message);
	}
}
