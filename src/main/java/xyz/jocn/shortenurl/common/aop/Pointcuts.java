package xyz.jocn.shortenurl.common.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

	@Pointcut("execution(* xyz.jocn.shortenurl..*Service.*(..))")
	void servicePointCut() {
	}

	@Pointcut("@annotation(xyz.jocn.shortenurl.common.aop.ExecutionTime)")
	void annotationPointCut() {
	}
}
