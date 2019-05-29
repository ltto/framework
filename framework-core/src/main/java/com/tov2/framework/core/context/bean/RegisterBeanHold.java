package com.tov2.framework.core.context.bean;

import com.tov2.framework.core.context.annotation.Bean;
import com.tov2.framework.core.utils.ContextUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Data
@Slf4j
public class RegisterBeanHold implements Injection {
    private Class clazz;
    private String beanName;
    private Method method;
    private String target;
    private Bean bean;
    private RegisterComponentHold componentHold;


    private boolean register = false;

    public RegisterBeanHold(Class clazz, String beanName, Bean bean, Method method, String target, RegisterComponentHold componentHold) {
        this.clazz = clazz;
        this.beanName = beanName;
        this.bean = bean;
        this.method = method;
        this.target = target;
        this.componentHold = componentHold;
    }

    @Override
    public void doInjection() {
        if (register || ContextUtils.exist(beanName)) return;
        componentHold.doInjection();
        Method method = this.getMethod();
        method.setAccessible(true);
        try {
            Object bean = method.invoke(ContextUtils.getByName(this.getTarget()));
            log.info("@Bean - key:" + beanName + " class:" + bean.getClass().getName());
            ContextUtils.put(this.beanName, bean, RegType.BEAN);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return;
        }
        register = true;
    }

    @Override
    public Class injectionAfter() {
        return bean.loadAfter();
    }

    @Override
    public Class injectionType() {
        return clazz;
    }

}
