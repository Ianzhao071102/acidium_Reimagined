package org.izdevs.acidium.utils;

import org.izdevs.acidium.AcidiumApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;


public class SpringBeanUtils implements ApplicationContextAware {
    @Autowired
    BeanFactory factory;

    Logger logger = LoggerFactory.getLogger("SpringBeanUtils");
    static ApplicationContext context;
    public void setApplicationContext(@NonNull  ApplicationContext applicationContext) throws BeansException {
        logger.debug("application context is set");
        logger.info(this.getClass().getCanonicalName() + " has been deprecated for multiple reasons...");
    }

    public SpringBeanUtils(ApplicationContext context){
        logger.debug("bean injection");
        SpringBeanUtils.context = context;
        if(context == null) this.setApplicationContext(AcidiumApplication.getContext());
    }

    public static Object getBean(String name){
        if(context != null){
            return context.getBean(name);
        }
        else{
            if(AcidiumApplication.context == null){
                return new StaticListableBeanFactory().getBean(name);
            }
            else{
                SpringBeanUtils.context = (AcidiumApplication.context);
                return AcidiumApplication.context.getBean(name);
            }
        }
    }
}
