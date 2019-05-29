package com.tov2.framework.core.context.bean;

import com.tov2.framework.core.context.annotation.Component;
import com.tov2.framework.core.utils.ContextUtils;
import com.tov2.framework.core.utils.StrUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class RegisterComponentHold implements Injection {
    private Class clazz;
    private Component component;
    private String beanName;
    private List<RegisterBeanHold> beanHolds = new ArrayList<>();
    private List<RegisterResourceHold> resourceHolds = new ArrayList<>();

    private boolean register = false;

    public RegisterComponentHold(Class clazz, Component component, String beanName) {
        this.clazz = clazz;
        this.component = component;
        this.beanName = beanName;
    }

    @Override
    public void doInjection() {
        if (register || ContextUtils.exist(beanName)) return;
        String name = component.value();
        String beanName = StringUtils.isBlank(name) ? StrUtils.fixBeanName(clazz.getSimpleName()) : name;
        Object bean;
        try {
            bean = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        log.info("@Component - key:" + beanName + " class:" + clazz.getName());
        ContextUtils.put(beanName, bean, RegType.COMPONENT);
        register = true;
    }

    @Override
    public Class injectionAfter() {
        return component.loadAfter();
    }

    @Override
    public Class injectionType() {
        return clazz;
    }

}
