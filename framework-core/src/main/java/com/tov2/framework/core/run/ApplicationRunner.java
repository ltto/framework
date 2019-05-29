package com.tov2.framework.core.run;

import com.tov2.framework.core.context.BeanContext;
import com.tov2.framework.core.context.DefaultBeanContext;
import com.tov2.framework.core.context.DefaultBeanLoader;
import com.tov2.framework.core.context.annotation.Application;
import com.tov2.framework.core.life.ApplicationStarter;

import java.util.Comparator;
import java.util.List;

public class ApplicationRunner {

    public static BeanContext run(Class<?> mainClass, String[] args) {
        DefaultBeanLoader contextObserver = DefaultBeanLoader.getInstance();
        DefaultBeanContext context = DefaultBeanContext.getInstance();
        try {
            //加载框架资源
            contextObserver.loadBean("com.tov2.framework");
        } catch (Exception e) {
            e.printStackTrace();
            return context;
        }
        //加载自定义资源
        String[] packages = getBasePackages(mainClass);
        for (String beanPackage : packages) {
            try {
                contextObserver.loadBean(beanPackage);
            } catch (Exception e) {
                e.printStackTrace();
                return context;
            }
        }
        //通知所有的 ApplicationStarter
        List<ApplicationStarter> starters = context.listByType(ApplicationStarter.class);
        starters.sort(Comparator.comparingInt(ApplicationStarter::startOrder));
        starters.forEach(ApplicationStarter::startup);
        return context;
    }

    private static String[] getBasePackages(Class<?> clazz) {
        Application annotation = clazz.getAnnotation(Application.class);
        String[] packages = annotation.scanBasePackages();
        if (packages.length == 1) {
            packages = new String[]{clazz.getPackage().getName()};
        }
        return packages;
    }
}
