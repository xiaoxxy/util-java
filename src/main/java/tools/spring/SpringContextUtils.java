package tools.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;



@Lazy(false)
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.context=applicationContext;
    }

    public static ApplicationContext getParent() {
        return context.getParent();
    }

    public static Environment getEnvironment() {
        return context.getEnvironment();
    }

    public static Object getBean(String name) throws BeansException {
        return context.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return context.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return context.getBean(requiredType);
    }

    public static ApplicationContext getContext(){
        return context;
    }

}
