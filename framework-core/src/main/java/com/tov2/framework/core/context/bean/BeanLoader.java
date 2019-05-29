package com.tov2.framework.core.context.bean;

public interface BeanLoader {
    void loadBean(String beanPath) throws Exception;

    void loadBeanByName(String beanName);

    void loadBeanByType(Class clazz);
}
