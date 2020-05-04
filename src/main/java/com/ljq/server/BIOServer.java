package com.ljq.server;

import com.ljq.MyServer;
import com.ljq.http.MyRequest;
import com.ljq.http.MyResponse;
import com.ljq.util.Constants;
import com.ljq.util.TomcatConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class BIOServer {
    private static Logger logger = LoggerFactory.getLogger(BIOServer.class);
    private int port;
    public BIOServer(int port){
        this.port = port;
    }
    public void start(){
        ServerSocket serverSocket = null;
        int coresize = Integer.valueOf(TomcatConfig.map.get(Constants.CORESIZE));
        int maxsize = Integer.valueOf(TomcatConfig.map.get(Constants.MAXSIZE));
        int queuesize = Integer.valueOf(TomcatConfig.map.get(Constants.QUEUESIZE));
//        int coresize = 5;
//        int maxsize = 10;
//        int queuesize = 5;
        long keep = 10L;  //线程存活时间
        TimeUnit unit = TimeUnit.SECONDS; //线程存活时间的单位
        //可以设置队列的容量
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue(queuesize);
        //直接抛出异常
        RejectedExecutionHandler defaultHandler = new ThreadPoolExecutor.AbortPolicy();
        //这个又提供默认的
        //ThreadFactory threadFactory;
        ExecutorService executorService = new ThreadPoolExecutor(coresize, maxsize, keep, unit, queue, defaultHandler);
        try {
            serverSocket = new ServerSocket(port);
            logger.info("BIO server is starting ...");
            while (true){
                Socket socket = serverSocket.accept();
                executorService.execute(new BIOThread(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
