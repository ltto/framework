package com.tov2.framework.core.context;

import com.tov2.framework.core.context.annotation.Bean;
import com.tov2.framework.core.context.annotation.Component;
import com.tov2.framework.core.context.annotation.Single;
import com.tov2.framework.core.context.bean.BeanLoader;
import com.tov2.framework.core.context.bean.DefaultBeanScanner;
import com.tov2.framework.core.context.bean.Injection;
import com.tov2.framework.core.context.bean.RegisterBeanHold;
import com.tov2.framework.core.context.bean.RegisterComponentHold;
import com.tov2.framework.core.context.bean.RegisterResourceHold;
import com.tov2.framework.core.context.bean.exception.NotBeanTypeException;
import com.tov2.framework.core.utils.Assert;
import com.tov2.framework.core.utils.ContextUtils;
import com.tov2.framework.core.utils.StrUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Single
@Slf4j
public class DefaultBeanLoader implements BeanLoader {

    private static DefaultBeanLoader instance = null;

    private Map<String, RegisterComponentHold> componentHoldMap = new HashMap<>();

    private BeanContext beanContext = DefaultBeanContext.getInstance();

    private List<String> loadedPackage = new ArrayList<>();

    private DefaultBeanLoader() {
    }

    public static DefaultBeanLoader getInstance() {
        if (instance == null) {
            synchronized (DefaultBeanLoader.class) {
                if (instance == null) {
                    instance = new DefaultBeanLoader();
                }
            }
        }
        return instance;
    }


    @Override
    public void loadBean(String beanPath) throws Exception {
        if (beanPath == null) return;
        beanPath = beanPath.trim();
        if (loadedPackage.contains(beanPath)) return;
        log.info("scanning bean path" + beanPath);
        Set<Class<?>> scan = new DefaultBeanScanner().scan(beanPath);
        for (Class<?> clazz : scan) {
            //接口不能注册为Component 必须是可实例化的class
            if (clazz.isInterface()) continue;
            //添加到等待容器中 方便之后实例化
            addComponent(clazz);
        }
        ArrayList<RegisterComponentHold> holds = new ArrayList<>(componentHoldMap.values());
        holds.forEach(componentHold -> {
            //某些Bean需要在指定Bean加载之后再加载
            injectionBean(componentHold);
            List<RegisterResourceHold> resourceHolds = componentHold.getResourceHolds();
            resourceHolds.forEach(hold -> {
                try {
                    hold.doRegister();
                } catch (NotBeanTypeException e) {
                    //不存的bean可能未注册 进行注册
                    loadBeanByType(hold.getField().getType());
                    try {
                        //继续尝试注册Bean
                        hold.doRegister();
                    } catch (NotBeanTypeException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        });
        //后置清扫工作
        clearMarkReg();
        componentHoldMap.clear();
        System.gc();
        loadedPackage.add(beanPath);
    }

    private void injectionBean(Injection injection) {
        Class after = injection.injectionAfter();
        Class type = injection.injectionType();
        //没有继承关系并未注册
        if (!after.isAssignableFrom(type) && !ContextUtils.exist(after)) {
            //加载并实例化前置bean
            loadBeanByType(after);
        }
        //注入容器
        injection.doInjection();
    }

    @Override
    public void loadBeanByName(String beanName) {
        Injection injection = nameInj.get(beanName);
        Assert.notNull(injection, "loadBeanByName失败");
        injectionBean(injection);
    }

    @Override
    public void loadBeanByType(Class clazz) {
        Injection injection = typeInj.get(clazz);
        if (injection == null) {
            List<Injection> injections = typeInj.values().stream()
                    .filter(inj -> clazz.isAssignableFrom(inj.injectionType()))
                    .collect(Collectors.toList());
            Assert.isTrue(injections.size() != 0 && injections.get(0) != null, "loadBeanByType失败");
            injectionBean(injections.get(0));
        } else {
            injectionBean(injection);
        }
    }

    private void addComponent(Class<?> clazz) {
        Component component;
        if ((component = clazz.getAnnotation(Component.class)) == null) return;
        String beanName = component.value();
        //注册component的key
        beanName = StrUtils.fixBeanName(StringUtils.isBlank(beanName) ? clazz.getSimpleName() : beanName);
        //相当于spring的 beanDefinition
        RegisterComponentHold componentHold = new RegisterComponentHold(clazz, component, beanName);
        //存入等待容器
        componentHoldMap.put(beanName, componentHold);
        //标记注册组件之后为他注入依赖的field
        markRegComponent(componentHold);
        //扫描@Bean 并标记
        componentHold.getBeanHolds().addAll(addRegBean(clazz, beanName, componentHold));
        //扫描@Resource 并标记
        componentHold.getResourceHolds().addAll(addRegResource(clazz, beanName));
    }

    private List<RegisterBeanHold> addRegBean(Class<?> clazz, String targetBeanName, RegisterComponentHold componentHold) {
        Method[] methods = clazz.getDeclaredMethods();
        List<RegisterBeanHold> list = new ArrayList<>();
        for (Method method : methods) {
            Bean bean = method.getAnnotation(Bean.class);
            Class<?> returnType = method.getReturnType();
            //目前只支持0参数@Bean
            if (bean == null || method.getParameterCount() != 0 || returnType == null) continue;
            String beanName = bean.value().trim();
            //设置Component的Key
            if (StringUtils.isBlank(beanName)) beanName = StrUtils.fixBeanName(returnType.getSimpleName());
            if (beanContext.exist(beanName)) continue;
            method.setAccessible(true);
            //加入并标记 之后修复依赖关系后进行实例化
            RegisterBeanHold hold = new RegisterBeanHold(returnType, beanName, bean, method, targetBeanName, componentHold);
            list.add(hold);
            //标记 之后修复依赖关系后进行实例化
            markRegBean(hold);
        }
        return list;
    }

    private List<RegisterResourceHold> addRegResource(Class clazz, String targetBeanName) {
        Field[] fields = clazz.getDeclaredFields();
        List<RegisterResourceHold> list = new ArrayList<>();
        for (Field field : fields) {
            Resource resources = field.getAnnotation(Resource.class);
            if (resources == null) continue;
            String needName = resources.name();
            if (StringUtils.isBlank(needName)) {
                //设置需要注入的Component Key
                needName = StrUtils.fixBeanName(field.getType().getSimpleName());
            }
            list.add(new RegisterResourceHold(targetBeanName, needName, field));
        }
        return list;
    }

    private Map<Class, Injection> typeInj = new HashMap<>();
    private Map<String, Injection> nameInj = new HashMap<>();

    private void markRegComponent(RegisterComponentHold hold) {
        typeInj.put(hold.getClazz(), hold);
        nameInj.put(hold.getBeanName(), hold);
    }

    private void markRegBean(RegisterBeanHold hold) {
        typeInj.put(hold.getClazz(), hold);
        nameInj.put(hold.getBeanName(), hold);
    }

    private void clearMarkReg() {
        typeInj.clear();
        nameInj.clear();
    }
}
