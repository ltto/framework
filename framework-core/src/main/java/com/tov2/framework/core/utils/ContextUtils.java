package com.tov2.framework.core.utils;

import com.tov2.framework.core.context.DefaultBeanContext;
import com.tov2.framework.core.context.bean.RegType;

import java.util.List;

public class ContextUtils {
    private static DefaultBeanContext context = DefaultBeanContext.getInstance();

    public static boolean put(String name, Object bean, RegType regType) {
        return context.put(name, bean, regType);
    }

    public static boolean put(String name, Object bean) {
        return context.put(name, bean);
    }

    public static Object getByName(String name) {
        return context.getByName(name);
    }

    public static <T> List<T> listByType(Class<T> type) {
        return context.listByType(type);
    }

    public static boolean exist(String name) {
        return context.exist(name);
    }

    public static boolean exist(Class type) {
        return context.exist(type);
    }
}
