package com.tov2.framework.core.context.bean;

public interface Injection {
    void doInjection();

    Class injectionAfter();

    Class injectionType();
}
