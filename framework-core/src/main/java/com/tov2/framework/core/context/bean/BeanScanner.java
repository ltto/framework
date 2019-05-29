package com.tov2.framework.core.context.bean;

import java.util.Set;

public interface BeanScanner {

    Set<Class<?>> scan(String beanPath);

}
