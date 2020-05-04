package com.ljq.instance;

import com.ljq.MyServer;
import com.ljq.entity.Response;
import com.ljq.http.MyHttpSession;
import com.ljq.http.MyRequest;
import com.ljq.http.MyResponse;
import com.ljq.server.ServletUtil;
import com.ljq.servlet.MyServlet;
import com.ljq.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StaticServlet extends MyServlet {
    private Logger logger = LoggerFactory.getLogger(StaticServlet.class);
    @Override
    public void doGet(MyRequest request, MyResponse response) {
        MyHttpSession session = request.getSession();
        String jsessionid = request.getSession().getId();
        String url = request.getUrl();
        String servleturl = ServletUtil.getServlet(url);
        int start = servleturl.length();
        String fileurl = url.substring(start);
        logger.info(url+"请求的资源是："+fileurl);
        String filePath= MyServer.class.getClassLoader().getResource("static/"+fileurl).getFile();
        File file = new File(filePath);
        FileInputStream fis = null;
        byte[] temp = null;
        try {
            fis = new FileInputStream(file);
            long size = file.length();
            temp = new byte[(int) size];
            fis.read(temp, 0, (int) size);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Response resStr = new Response();
        resStr.setBodys(temp);
        String cookies = "JSESSIONID=" + jsessionid;
        resStr.setHeader(Constants.COOKIES, cookies);
        resStr.setHeader(Constants.CONTEN_LENGTH, ""+temp.length);
        // 设置文件格式内容
        String path = request.getUrl();
        if (path.endsWith(".html")){
            resStr.setHeader(Constants.CONTENT_TYPE, "text/html; charset=UTF-8");
        } else if(path.endsWith(".js")){
            resStr.setHeader(Constants.CONTENT_TYPE, "application/x-javascript");
        } else if(path.endsWith(".css")){
            resStr.setHeader(Constants.CONTENT_TYPE, "text/css; charset=UTF-8");
        } else if(path.endsWith(".ico")){
            resStr.setHeader(Constants.CONTENT_TYPE, "image/ico");
        }
        logger.debug(resStr.getContent());
        try {
            response.write(resStr);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.toString());
        }


    }

    @Override
    public void doPost(MyRequest request, MyResponse response) {

    }
}
