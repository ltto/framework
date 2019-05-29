package com.tov2.framework.web;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;

public class Test {
    public static void main(String[] args) throws MalformedURLException, UnsupportedEncodingException {
        String s = "//a/a/a/a//aaa//";
        File file = new File(s);


        System.out.println(s.replaceAll("/+", "/"));
        System.out.println("17777833438".replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        String encodeStr = "%E5%90%89EB9021";

        System.out.println(URLDecoder.decode(encodeStr, "UTF-8"));
        System.out.println(URLDecoder.decode(URLDecoder.decode(encodeStr, "UTF-8")));
    }
}
