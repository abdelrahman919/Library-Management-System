package com.maids.cc.Library_Management_System.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

//Logging is done based on task requirements
//Transactions, book save and updates calls and exceptions

@Aspect
@Component
public class LoggingAspect {

    Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.maids.cc.Library_Management_System.service.BookService.*save*(..)) || execution(* com.maids.cc.Library_Management_System.service.BookService.*update*(..))")
    public void bookAdditionOrUpdateLog() {
    }

    @Pointcut("execution(* com.maids.cc.Library_Management_System.service..*(..)) && @annotation(org.springframework.transaction.annotation.Transactional)")
    public void serviceTransactions() {
    }


    @Around("bookAdditionOrUpdateLog()")
    public Object logBookSaveOrUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info(String.format("""
                                        
                        Executing method: %s
                        With args: %s
                        """
                , joinPoint.getSignature(),
                Arrays.toString(joinPoint.getArgs())));

        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - start;
        log.info("Method: " + joinPoint.getSignature().getName() +
                " ,Executed in: " + totalTime + " ms"
        );
        return proceed;
    }

    @AfterThrowing(value = "bookAdditionOrUpdateLog()", throwing = "e")
    public void logBookSaveOrUpdateExceptions(JoinPoint joinPoint, Throwable e) {
        log.error(String.format("""
                                        
                        Exception in method: %s,
                        with args: %s,
                        with error message: %s
                        """,
                joinPoint.getSignature(),
                Arrays.toString(joinPoint.getArgs()),
                e.getMessage()));
    }

    @Around("serviceTransactions()")
    public Object logServiceTransactions(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info(String.format("""
                                        
                        Executing transaction method: %s,
                        with args: %s,
                        """,
                joinPoint.getSignature(),
                Arrays.toString(joinPoint.getArgs())));

        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - start;
        log.info(String.format("""
                        Transaction: %s executed in %d ms
                        """
                , joinPoint.getSignature().getName()
                , totalTime
        ));
        return proceed;
    }

    @AfterThrowing(value = "serviceTransactions()", throwing = "e")
    public void logServiceTransactionExceptions(JoinPoint joinPoint, Throwable e) {
        log.error(String.format("""
                                        
                        Exception in transaction method: %s,
                        with args: %s,
                        with error message: %s
                        """,
                joinPoint.getSignature(),
                Arrays.toString(joinPoint.getArgs()),
                e.getMessage()));
    }

}
