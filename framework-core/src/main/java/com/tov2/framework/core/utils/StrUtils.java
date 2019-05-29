package com.tov2.framework.core.utils;

public class StrUtils {
    public static String fixBeanName(String beanName) {
        if (org.apache.commons.lang3.StringUtils.isBlank(beanName)) return null;
        return String.valueOf(beanName.charAt(0)).toLowerCase() + beanName.substring(1);
    }
}
