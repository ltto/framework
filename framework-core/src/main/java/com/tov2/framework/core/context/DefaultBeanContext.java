package com.tov2.framework.core.context;

import com.tov2.framework.BaseInfo;
import com.tov2.framework.core.context.annotation.Single;
import com.tov2.framework.core.context.bean.BeanBox;
import com.tov2.framework.core.context.bean.RegType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Single
public class DefaultBeanContext implements BeanContext {
    private Map<String, BeanBox> IOCMap = new ConcurrentHashMap<>();

    private static DefaultBeanContext instance = null;

    private DefaultBeanContext() {
        IOCMap.put("baseInfo", new BeanBox(new BaseInfo(), "baseInfo", BaseInfo.class, null));
    }

    public static DefaultBeanContext getInstance() {
        if (instance == null) {
            synchronized (DefaultBeanLoader.class) {
                if (instance == null) {
                    instance = new DefaultBeanContext();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean put(String name, Object bean, RegType regType) {
        if(regType == null ) throw new NullPointerException("regType can not null");
        if (exist(name)) {
            return false;
        } else {
            BeanBox beanBox = new BeanBox(bean, name, bean.getClass(), regType);
            IOCMap.put(name, beanBox);
            return true;
        }
    }

    @Override
    public boolean put(String name, Object bean) {
        return put(name, bean, RegType.MANUAL);
    }

    @Override
    public Object getByName(String name) {
        BeanBox beanBox = IOCMap.get(name);
        return beanBox == null ? null : beanBox.getInstance();
    }

    @Override
    public <T> List<T> listByType(Class<T> type) {
        return IOCMap.values().stream()
                .filter(beanBox -> type.isAssignableFrom(beanBox.getBeanType()))
                .map(beanBox -> (T) (beanBox.getInstance())).collect(Collectors.toList());
    }

    @Override
    public boolean exist(String name) {
        return IOCMap.containsKey(name);
    }

    @Override
    public boolean exist(Class type) {
        return IOCMap.values().stream().anyMatch(beanBox -> type.isAssignableFrom(beanBox.getBeanType()));
    }
}
