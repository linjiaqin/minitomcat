package com.ljq.entity;

import com.ljq.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class Response {
    private String content;
    private HashMap<String, String> firstLine = new HashMap<>();
    private HashMap<String, String> header = new HashMap<>();
    private String body;

    //private byte[] headers;
    private byte[] bodys;
    //填充默认状态行和头部
    public Response(){
        setDefaultFirstLine();
        setDefaultHeader();
    }
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setFirstLine(String name, String value){
        firstLine.put(name, value);
    }

    public void setDefaultFirstLine() {
        firstLine.put("http", "HTTP/1.1");
        firstLine.put("status", "200");
        firstLine.put("reason", "OK");
    }

    public void setDefaultHeader(){
        header.put(Constants.SERVER,"Windows/Miniomcat");
        header.put(Constants.CACHE,"no-cache");
        header.put(Constants.CONTENT_TYPE,"text/html;charset=utf-8");
        header.put(Constants.CONTEN_LENGTH, "0");
        header.put(Constants.KEEPALIVE, "timeout=5, max=1000");
        header.put(Constants.COOKIES, "");
    }

    public byte[] getHeaders(){
        StringBuilder first = new StringBuilder("");
        first.append(firstLine.get("http")+" ")
                .append(firstLine.get("status")+" ")
                .append(firstLine.get("reason")+"\r\n");
        StringBuilder headers = new StringBuilder("");
        for (Map.Entry e: header.entrySet()){
            headers.append(e.getKey()+": "+e.getValue()+"\r\n");
        }
        String res = first.toString() +headers.toString()+"\r\n";
        return res.getBytes();
    }
    public byte[] getBodys(){
        return bodys;
    }

    public void setHeaders(){

    }
    public void setBodys(byte[] bytes){
        body = new String(bytes);
        bodys = bytes;
    }
    public String getContent() {
        StringBuilder first = new StringBuilder("");
        first.append(firstLine.get("http")+" ")
                .append(firstLine.get("status")+" ")
                .append(firstLine.get("reason")+"\r\n");
        StringBuilder headers = new StringBuilder("");
        for (Map.Entry e: header.entrySet()){
            headers.append(e.getKey()+": "+e.getValue()+"\r\n");
        }
        content = first.toString() +headers.toString()+"\r\n"+body;

        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeader(String name) {
        return header.get(name);
    }

    public void setHeader(String name, String value) {
        header.put(name, value);
    }

}
