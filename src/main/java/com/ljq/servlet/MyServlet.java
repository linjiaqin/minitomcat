package com.ljq.servlet;

import com.ljq.http.MyRequest;
import com.ljq.http.MyResponse;

public abstract class MyServlet {
    public abstract void doGet(MyRequest request, MyResponse response);

    public abstract void doPost(MyRequest request, MyResponse response);

    public void service(MyRequest request, MyResponse response){
        if (request.getMethod().equals("POST")){
            doPost(request, response);
        }else if (request.getMethod().equals("GET")){
            doGet(request, response);
        }
    }

    public void init(){
        //System.out.println("1.初始化");
    }
    public void destory(){
        //System.out.println("3.卸载");
    }
}
