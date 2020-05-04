package com.ljq;

import com.ljq.server.BIOServer;
import com.ljq.server.NettyServer;
import com.ljq.util.Constants;
import com.ljq.util.ServletConfig;
import com.ljq.servlet.ServletMapping;
import com.ljq.util.TomcatConfig;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyServer {

    private static Logger logger = LoggerFactory.getLogger(MyServer.class);
    private int port = 8080;

    public int type = 1;
    public MyServer(int port){
        this.port = port;
    }

    public void start(String type){
        switch (type){
            case "BIO":
                new BIOServer(port).start();
            case "Netty":
                new NettyServer(port).start();
            default:
                new BIOServer(port).start();
        }
    }

    public static void main(String[] args) {
        ServletConfig.init();
        try {
            TomcatConfig.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int port = Integer.valueOf(TomcatConfig.map.get(Constants.PORT));
        String type = (String)TomcatConfig.map.get(Constants.IOType);
        new MyServer(port).start(type);
    }
}
