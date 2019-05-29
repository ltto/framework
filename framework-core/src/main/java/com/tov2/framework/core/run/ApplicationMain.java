package com.tov2.framework.core.run;

import com.tov2.framework.core.context.BeanContext;
import com.tov2.framework.core.context.annotation.Application;

@Application
public class ApplicationMain {
    public static void main(String[] args) {
        BeanContext context = ApplicationRunner.run(ApplicationMain.class, args);
    }
}
