package com.dx.util.tools;

import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/** 
 * 描述： 获取bean的工具类，可用于在线程里面获取bean
 * 创建人: zbh
 */
@SuppressWarnings({"unchecked","static-access"})
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context = null;

	@Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.context = applicationContext;
        
    }

    public static <T> T getBean(String beanName){
        return (T) context.getBean(beanName);
    }

    public static String getMessage(String key){
        return context.getMessage(key, null, Locale.getDefault());
    }

}