package com.tov2.framework.core.context.bean;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class AbstractBeanScanner implements BeanScanner {

    public String packageDir(String packagePath) {
        return packagePath.replace('.', '/');
    }

    public Set<Class> scanJarFile(String jarPath) {
        JarFile jarFile = null;
        Set<Class> set = new TreeSet<>();
        try {
            jarFile = new JarFile(new File(jarPath));
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String classPath = jarEntry.getName();
                if (!jarEntry.isDirectory() && classPath.endsWith(".class")) {
                    String classPackage = classPath.substring(0, classPath.length() - 6).replaceAll("/", ".");
                    set.add(Class.forName(classPackage));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return set;
    }

    public Set<Class<?>> scan(String packagePath) {
        Reflections reflections = new Reflections(packagePath, new SubTypesScanner(false));
        return reflections.getSubTypesOf(Object.class);
    }
}
