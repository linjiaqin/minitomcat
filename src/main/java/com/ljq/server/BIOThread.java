package com.ljq.server;

import com.ljq.http.MyRequest;
import com.ljq.http.MyResponse;
import com.ljq.servlet.MyServlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class BIOThread implements Runnable {
    private Socket socket;
    BIOThread(Socket socket) {
        this.socket = socket;
    }
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            MyRequest request = new MyRequest(inputStream);
            MyResponse response = new MyResponse(outputStream);
            ServletUtil.dispatch(request, response);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
