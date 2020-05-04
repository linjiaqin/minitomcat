package com.ljq.util;

import com.ljq.servlet.ServletMapping;
import com.ljq.util.XMLParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServletConfig {
    private static Logger logger = LoggerFactory.getLogger(ServletConfig.class);
    public static List<ServletMapping> servletMapList = new ArrayList<ServletMapping>();

    public static ConcurrentHashMap<String, String> servletUrlMap = new ConcurrentHashMap<String, String>();
    public static void init() {

        XMLParse.parseWebXML();
        for (Map.Entry e: XMLParse.servletUrl.entrySet()){
            String url = (String)e.getKey();
            String name = (String)e.getValue();
            String clazz = XMLParse.servletName.get(name);
            servletMapList.add(new ServletMapping(name, url, clazz));
        }
        initServletMapping();
    }

    public static void initServletMapping(){
        for(ServletMapping servletMapping: ServletConfig.servletMapList){
            servletUrlMap.put(servletMapping.getUrl(), servletMapping.getClazz());
        }
    }

//    public static void main(String[] args) {
//        for (ServletMapping e:servletMapList){
//            System.out.println(e);
//        }
//    }
}
