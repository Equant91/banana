package com.banana.annotation;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DB;

public class DbOpenWithTransactionAnnotationInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result;
        try (DB db = new DB()) {
            db.open();
            Base.openTransaction();
            result = invocation.proceed();
            Base.commitTransaction();
        } catch (Exception e) {
            try {
                Base.rollbackTransaction();
            } catch (Exception a) {
                throw e;
            }
            throw e;
        }
        return result;
    }
}
