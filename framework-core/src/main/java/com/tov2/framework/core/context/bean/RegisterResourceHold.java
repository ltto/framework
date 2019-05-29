package com.tov2.framework.core.context.bean;

import com.tov2.framework.core.context.bean.exception.NotBeanTypeException;
import com.tov2.framework.core.utils.Assert;
import com.tov2.framework.core.utils.ContextUtils;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RegisterResourceHold {
    private String target;
    private Field field;
    private String injection;
    private boolean register = false;

    public RegisterResourceHold(String target, String injection, Field field) {
        this.target = target;
        this.injection = injection;
        this.field = field;
    }

    public void doRegister() throws NotBeanTypeException {
        if (register) return;
        Field field = this.getField();
        field.setAccessible(true);
        Object injection = ContextUtils.getByName(this.getInjection());
        Object target = ContextUtils.getByName(this.getTarget());
        Assert.notNull(target, "target is null");
        //beanName找不到 通过type注入
        if (injection == null) {
            List<?> list = ContextUtils.listByType(field.getType());
            int size = list.size();
            //不存在的类型
            if (size == 0 || list.get(0) == null) {
                throw new NotBeanTypeException("@Resource not fund Field:" + field.getName());
            } else if (size > 1) {//存在多个
                list = list.stream().filter(o -> o.getClass().equals(field.getType())).collect(Collectors.toList());
                size = list.size();
                if (size == 0 || list.get(0) == null) {//找不到唯一的bean
                    throw new NotBeanTypeException("@Resource not fund Field:" + field.getName());
                } else if (size > 1) {//找不因为唯一的bean
                    throw new NotBeanTypeException("@Resource have size>1 Field:" + field + " List:" + list);
                }
            }
            try {
                field.set(target, list.get(0));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                field.set(target, injection);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        register = true;
    }
}
