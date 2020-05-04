package com.ljq.http;

import com.ljq.entity.Response;
import com.ljq.util.Constants;
import com.ljq.util.TomcatConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class MyResponse {

    private static Logger logger = LoggerFactory.getLogger(MyResponse.class);
    private ChannelHandlerContext chc;

    public MyResponse(ChannelHandlerContext chc) {
        this.chc = chc;
    }

    //下面是BIO
    private OutputStream outputStream;

    public MyResponse(OutputStream outputStream){
        this.outputStream = outputStream;
    }
    public void write(Response response) throws IOException {
        String IOType = TomcatConfig.map.get(Constants.IOType);
        if(IOType.equals("BIO")){
            //outputStream.write(response.getContent().getBytes());
            outputStream.write(response.getHeaders());
            outputStream.write(response.getBodys());
            outputStream.close();
        }
        else if (IOType.equals("Netty")){
            //logger.info(response.getContent());
//            byte[] bytes = response.getContent().getBytes();
//            ByteBuf buf = Unpooled.wrappedBuffer(bytes);
//            chc.writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE);
//
            byte[] headerByte = response.getHeaders();
            ByteBuf headerBuf = Unpooled.wrappedBuffer(headerByte);
            chc.write(headerBuf);
            chc.flush();

            byte[] bodyByte = response.getBodys();
            //System.out.println("bodys length is---------------"+bodyByte.length);
            ByteBuf bodyBuf = Unpooled.wrappedBuffer(bodyByte);
            chc.write(bodyBuf);
            chc.flush();
            chc.close();
        }
    }
}
