package com.banana.annotation;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DB;

public class DbOpenWithTransactionAnnotationInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Before " + invocation.getMethod().getName());
        Object result;
        try (DB db = new DB()) {
            db.open();
            Base.openTransaction();
            result = invocation.proceed();
            System.out.println("After " + invocation.getMethod().getName());
            Base.commitTransaction();
        } catch (Exception e) {
            Base.rollbackTransaction();
            throw e;
        }
        return result;
    }
}
