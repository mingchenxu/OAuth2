package com.oathc.authclient.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
@Slf4j
public class ApplicationContextRegister implements ApplicationContextAware {

    private static ApplicationContext APPLICATION_CONTEXT;


    /**
     * 设置spring上下文
     * * @param applicationContext spring上下文
     *
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        log.info("ApplicationContext registered-->{}", applicationContext);
        APPLICATION_CONTEXT = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    public static Object getBean(String name) {
        return APPLICATION_CONTEXT.getBean(name);
    }

    public static Object getBean(String name, Class<?> requiredType) {
        return APPLICATION_CONTEXT.getBean(name, requiredType);
    }

    public static boolean containsBean(String name) {
        return APPLICATION_CONTEXT.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return APPLICATION_CONTEXT.isSingleton(name);
    }

    public static Class<?> getType(String name) {
        return APPLICATION_CONTEXT.getType(name);
    }

    public static String[] getAliases(String name) {
        return APPLICATION_CONTEXT.getAliases(name);
    }

}
