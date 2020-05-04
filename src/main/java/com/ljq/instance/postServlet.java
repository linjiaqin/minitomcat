package com.ljq.instance;

import com.ljq.MyServer;
import com.ljq.entity.Response;
import com.ljq.http.MyHttpSession;
import com.ljq.http.MyRequest;
import com.ljq.http.MyResponse;
import com.ljq.servlet.MyServlet;
import com.ljq.util.Constants;
import com.ljq.util.FileUtil;

import java.io.*;

public class postServlet extends MyServlet {

    @Override
    public void doGet(MyRequest request, MyResponse response) {
        String page = request.getUrl();
        String filePath= MyServer.class.getClassLoader().getResource("static/hello.html").getFile();
        MyHttpSession session = request.getSession();
        String jsessionid = request.getSession().getId();
        System.out.println(session.getAttribute("linjiaqin"));
        StringBuilder res = new StringBuilder("");
        res.append(FileUtil.getFileContent(filePath));
//        try {
//            FileInputStream in = new FileInputStream(new File(filePath));
//            BufferedInputStream bf = new BufferedInputStream(in);
//            while (bf.available() > 0){
//                res.append((char)bf.read());
//            }
//            in.close();
//            bf.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Response resStr = new Response();
        String content = res.toString();
        resStr.setBodys(content.getBytes());
        String cookies = "JSESSIONID=" + jsessionid;
        resStr.setHeader(Constants.COOKIES, cookies);
        resStr.setHeader(Constants.CONTEN_LENGTH, ""+content.getBytes().length);
        // 设置文件格式内容
        String path = request.getUrl();
        if (path.endsWith(".html")){
            resStr.setHeader(Constants.CONTENT_TYPE, "text/html; charset=UTF-8");
        }else if(path.endsWith(".js")){
            resStr.setHeader(Constants.CONTENT_TYPE, "application/x-javascript");
        }else if(path.endsWith(".css")){
            resStr.setHeader(Constants.CONTENT_TYPE, "text/css; charset=UTF-8");
        }
        System.out.println("http response 为： ***************************");
        System.out.println(resStr.getContent());
        System.out.println("***********************************");
        try {
            response.write(resStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(MyRequest request, MyResponse response) {

        String username = request.getParameter("username");
        MyHttpSession session = request.getSession();
        String jsessionid = session.getId();
        System.out.println("sessionid is:"+jsessionid);

        if (session.getAttribute(username) == null){
            System.out.println("设置属性");
            session.setAttributes(username,25);
        }
        else System.out.println("arrtribut is :"+session.getAttribute(username));
        String content = username+"hahaha"+session.getAttribute("linjiaqin");
        //System.out.println(session.getAttributes());
        Response resStr = new Response();
        resStr.setBodys(content.getBytes());
        String cookies = "JSESSIONID=" + jsessionid;
        resStr.setHeader(Constants.COOKIES, cookies);
        resStr.setHeader(Constants.CONTEN_LENGTH, ""+content.getBytes().length);
        System.out.println("http response 为： ***************************");
        System.out.println(resStr.getContent());
        System.out.println("***********************************");
        try {
            response.write(resStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
