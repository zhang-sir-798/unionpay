package com.dx.util.dbconfig;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import java.lang.reflect.Method;

public class DataSourceAspect {  
    public void before(JoinPoint point)  
    {  
        Object target = point.getTarget();  
        //System.out.println(target.toString());  
        String method = point.getSignature().getName();  
        //System.out.println(method);  
        Class<?> classz = target.getClass();  
        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature())  
                .getMethod().getParameterTypes();  
        try {  
            Method m = classz.getMethod(method, parameterTypes);  
            //System.out.println(m.getName());  
            if (m != null && m.isAnnotationPresent(DataSource.class)) {  
                DataSource data = m.getAnnotation(DataSource.class);  
                DynamicDataSourceHolder.putDataSource(data.value());  
            }  

        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}  
