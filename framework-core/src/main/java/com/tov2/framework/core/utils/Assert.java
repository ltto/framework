package com.tov2.framework.core.utils;

public class Assert {
    public static void notNull(Object o, String msg) {
        if (o == null) {
            throw new RuntimeException(msg);
        }
    }

    public static void isTrue(boolean b, String msg) {
        if (!b) {
            throw new RuntimeException(msg);
        }
    }
}
