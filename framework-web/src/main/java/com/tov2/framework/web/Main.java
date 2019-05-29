package com.tov2.framework.web;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

public class Main {
    private static String classpath = Main.class.getResource("/").getPath();
    private static String webappDirLocation = classpath + "webapp/";


    public static void main(String[] args) throws Exception {

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8432);

        StandardContext ctx = (StandardContext) tomcat.addWebapp("", webappDirLocation);
        ctx.setParentClassLoader(Main.class.getClassLoader());
        WebResourceRoot resources = new StandardRoot(ctx);

        WebResourceSet resourceSet = new DirResourceSet(resources, "/WEB-INF/classes", classpath, "/");

        resources.addPreResources(resourceSet);
        ctx.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }
}
