package iob.logic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Profile("enableMethodLogging")
public class LogMethodAspect {
	private Log logger = LogFactory.getLog(LogMethodAspect.class);
	
	
//	@Before("@annotation(iob.logic.LogThisMethod)")
//	public void printLogger(JoinPoint jp) {
//		Object targetObject = jp.getTarget();
//		Class<?> targetClass = targetObject.getClass();
//		String className = targetClass.getName();
//		
//		
//		String methodName = jp.getSignature().getName();
//		this.logger.trace("***" + className + "." + methodName + "()");
//	}
	
	
	@Around("@annotation(iob.logic.LogThisMethod)")
	public Object printLogger(ProceedingJoinPoint jp) throws Throwable{
		// pre processing
		Object targetObject = jp.getTarget();
		Class<?> targetClass = targetObject.getClass();
		
		String className = targetClass.getName();
		String methodName = jp.getSignature().getName();
		
		this.logger.trace("%%%" + className + "." + methodName + "() - begins");
		
		// invoke method
		try {
			
			Object rv = jp.proceed();
			// success post processing
			// .... 
			this.logger.trace("%%%" + className + "." + methodName + "() - success");
			return rv;
			
		}catch (Throwable t){
			// fail post processing
			this.logger.trace("%%%" + className + "." + methodName + "() - failure");
			throw t;
		}
	}
}
