package com.ljq.util;

import com.ljq.MyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

public class TomcatConfig {
    private static Logger logger = LoggerFactory.getLogger(TomcatConfig.class);
    public static HashMap<String, String> map = new HashMap<String, String>();
    public static void defaultInit(){
        map.put(Constants.IOType, "BIO");
        map.put(Constants.CORESIZE, new Integer(10).toString());
        map.put(Constants.MAXSIZE, new Integer(20).toString());
        map.put(Constants.QUEUESIZE, new Integer(10).toString());
        map.put(Constants.PORT, new Integer(8080).toString());
    }
    public static void init() throws IOException {
        //先加载默认配置，防止
        defaultInit();
        //加载配置文件
        String filePath= MyServer.class.getClassLoader().getResource("minitomcat.properties").getFile();
        logger.info("开始解析tomcat配置文件："+filePath);
        Properties properties = new Properties();
        properties.load(new InputStreamReader(new FileInputStream(filePath)));
        System.out.println();
        if (properties.get(Constants.PORT) != null)
            map.put(Constants.PORT, (String)properties.get(Constants.PORT));
        if (properties.get(Constants.IOType) != null)
            map.put(Constants.IOType, (String)properties.get(Constants.IOType));
        if (properties.get(Constants.CORESIZE) != null)
            map.put(Constants.CORESIZE, (String)properties.get(Constants.CORESIZE));
        if (properties.get(Constants.MAXSIZE) != null)
            map.put(Constants.MAXSIZE, (String)properties.get(Constants.MAXSIZE));
        if (properties.get(Constants.QUEUESIZE) != null)
            map.put(Constants.QUEUESIZE, (String)properties.get(Constants.QUEUESIZE));

        logger.info("IOType is:"+map.get(Constants.IOType));
        logger.info("server.port is:"+map.get(Constants.PORT));

    }
}
