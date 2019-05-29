package com.tov2.framework.core.context;

import com.tov2.framework.core.context.bean.RegType;

import java.util.List;

public interface BeanContext {

    boolean put(String name, Object bean, RegType regType);

    boolean put(String name, Object bean);

    Object getByName(String name);

    <T> List<T> listByType(Class<T> type);

    boolean exist(String name);

    boolean exist(Class type);
}
