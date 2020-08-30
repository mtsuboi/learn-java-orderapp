package com.example.orderapp.logging;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.ArrayUtils;

@Aspect
@Component
public class LoggingAspects {
	private static final Logger logger =LoggerFactory.getLogger(LoggingAspects.class);
	
	@Before("execution(* *..repository.*Impl.*(..)) || execution(* *..logic.*Impl.*(..))")
	public void startLog(JoinPoint joinpoint) {
		// 開始ログを出力
		Signature signature = joinpoint.getSignature();
		String args = "none";
		if(!ArrayUtils.isEmpty(joinpoint.getArgs())) {
			args = Arrays.toString(joinpoint.getArgs());
		}
		logger.info("Before: " + signature.getName() + "[" + signature.getDeclaringTypeName() + "] args:" + args);
	}

	@AfterReturning(pointcut="execution(* *..repository.*Impl.*(..)) || execution(* *..logic.*Impl.*(..))", returning="returnValue")
	public void endLog(JoinPoint joinpoint, Object returnValue) {
		// 終了ログを出力
		Signature signature = joinpoint.getSignature();
		logger.info("After: " + signature.getName() + "[" + signature.getDeclaringTypeName() + "] returnValue:" + returnValue);
	}
}