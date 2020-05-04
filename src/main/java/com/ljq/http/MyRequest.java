package com.ljq.http;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MyRequest {
    private String url;
    private String method;
    private String header;
    private String boundary;
    private Map<String, String> map = new HashMap<String, String>();
    private static Logger logger = LoggerFactory.getLogger(MyRequest.class);

    //netty相关
    private ChannelHandlerContext chc;

    public MyRequest(ChannelHandlerContext chc, String httpMsg) {
        this.chc = chc;
        logger.debug(httpMsg);
        //解析http协议的方法行
        String http[] = httpMsg.split("\n");
        String methodLine = http[0];
        String[] tmp = methodLine.split("\\s");
        url = tmp[1];
        method = tmp[0];

        StringBuilder tmpHeader = new StringBuilder("");
        int crline = 0;
        for(int i = 1; i < http.length; i++){
            if(http[i].length() == 0 || http[i].trim().equals("")){
                crline = i;
                break;
            }
            tmpHeader.append(http[i]);
            //System.out.println(http[i]);
            if (http[i].contains("boundary=")){
                String kv[] = http[i].split("boundary=");
                boundary = kv[1].trim();
            }
            else{
                String[] kv = http[i].split(":");
                map.put(kv[0].trim(), kv[1].trim());
                if (kv[0].trim().equals("Cookie")){
                    int start = 0;
                    int end = kv[1].length();
                    for(int j = 0; j < kv[1].length(); j++){
                        if (kv[1].charAt(j) == '=') start = j+1;
                        if (kv[1].charAt(j) == ';') {
                            end = j;
                            break;
                        }
                    }
                    MyManagerBase.session.set(kv[1].substring(start, end).trim());
                    logger.info(getUrl() + "："+MyManagerBase.session.get());
                }
            }

            if (boundary != null){
                if (http[i].equals(boundary)){
                    break;
                }
            }
        }
        if (crline < http.length-1){
            for(int i = crline+1; i < http.length; i++){
                String body = http[i];
                String[] pairs = body.split("&");
                for(String e: pairs){
                    String[] kv = e.split("=");
                    map.put(kv[0].trim(), kv[1].trim());
                }
            }
        }

    }

    public MyHttpSession getSession() {
        return MyManagerBase.getSession();
    }

    public String getParameter(String key){
        return map.get(key);
    }

    public Map<String, String> getParameterMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public MyRequest(InputStream inputStream) throws IOException {
        String httpRequest = "";
        byte[] httpRequestByte = new byte[10240];
        int length = 0;
        if ((length = inputStream.read(httpRequestByte)) > 0){
            httpRequest = new String(httpRequestByte, 0, length);
        }

        System.out.println("http请求为：");
        System.out.println("*******************************************************");
        System.out.println(httpRequest);
        System.out.println("*******************************************************");
        //解析http协议的方法行
        String http[] = httpRequest.split("\n");
        String methodLine = http[0];
        String[] tmp = methodLine.split("\\s");
        url = tmp[1];
        method = tmp[0];

        StringBuilder tmpHeader = new StringBuilder("");
        int crline = 0;
        for(int i = 1; i < http.length; i++){
            if(http[i].length() == 0 || http[i].trim().equals("")){
                crline = i;
                break;
            }
            tmpHeader.append(http[i]);
            //System.out.println(http[i]);
            if (http[i].contains("boundary=")){
                String kv[] = http[i].split("boundary=");
                boundary = kv[1].trim();
            }
            else{
                String[] kv = http[i].split(":");
                map.put(kv[0].trim(), kv[1].trim());
                if (kv[0].trim().equals("Cookie")){
                    int start = 0;
                    int end = kv[1].length();
                    for(int j = 0; j < kv[1].length(); j++){
                        if (kv[1].charAt(j) == '=') start = j+1;
                        if (kv[1].charAt(j) == ';') {
                            end = j;
                            break;
                        }
                    }
                    MyManagerBase.session.set(kv[1].substring(start, end).trim());
                    System.out.println("携带的sessionid："+MyManagerBase.session.get());
                }
            }

            if (boundary != null){
                if (http[i].equals(boundary)){
                    break;
                }
            }
        }
        if (crline < http.length-1){
            for(int i = crline+1; i < http.length; i++){
                String body = http[i];
                String[] pairs = body.split("&");
                for(String e: pairs){
                    String[] kv = e.split("=");
                    map.put(kv[0].trim(), kv[1].trim());
                }
            }
        }


    }
}
