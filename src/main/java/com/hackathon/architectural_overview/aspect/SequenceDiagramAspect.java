package com.hackathon.architectural_overview.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.*;

@Aspect
@Component
@EnableAspectJAutoProxy
public class SequenceDiagramAspect {
    private final ThreadLocal<List<String>> traceLog = ThreadLocal.withInitial(ArrayList::new);

    @Pointcut("within(com.hackathon.architectural_overview..*)")
    public void springComponents() {}

    @Around("springComponents()")
    public Object traceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String component = joinPoint.getTarget().getClass().getSimpleName();
        String method = joinPoint.getSignature().getName();
        String callerName = getCallerName(component);
        String args = Arrays.toString(joinPoint.getArgs());

        // Start of call
        traceLog.get().add(String.format("%s -> %s: %s(%s)",
                callerName, component, method,args));

        Object result = joinPoint.proceed();

        // End of call
        traceLog.get().add(String.format("\n %s --> %s: returns %s", component, callerName, result.toString()));

        return result;
    }

    // A helper method to determine the caller for the trace
    private String getCallerName(String targetName) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 3; i < stackTrace.length; i++) { // Skip the Aspect and Thread calls
            String className = stackTrace[i].getClassName();
            if (className.contains("com.hackathon") && !className.contains(targetName)) {
                className = className.substring(className.lastIndexOf('.') + 1);
                return className;
            }
        }
        return "Unknown";
    }

    public List<String> getTrace() {
        return traceLog.get();
    }

    public void clearTrace() {
        traceLog.remove();
    }
}
