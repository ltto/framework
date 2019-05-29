package com.tov2.framework.core.context.bean;

import lombok.Data;

@Data
public class BeanBox {
    //实例
    private Object instance;
    private String beanName;
    private Class beanType;
    //注入方式
    private RegType regType;

    public BeanBox(Object instance, String beanName, Class beanType, RegType regType) {
        this.instance = instance;
        this.beanName = beanName;
        this.beanType = beanType;
        this.regType = regType;
    }

}
