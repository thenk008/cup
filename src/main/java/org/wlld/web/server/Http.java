package org.wlld.web.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.wlld.web.WebIo.WebUrl;

import java.util.Map;

public class Http extends SimpleChannelInboundHandler<Object> {
    private static Map<String, byte[]> urlMap = WebUrl.getWebUrl().getRes();

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 用户掉线
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext arg0, Object msg) throws Exception {
        handleHttpRequest(arg0, (FullHttpRequest) msg);

    }

    private void handleHttpRequest(ChannelHandlerContext arg0, FullHttpRequest msg) throws Exception {// !msg.getDecoderResult().isSuccess()
        FullHttpResponse response;
        if (!msg.getDecoderResult().isSuccess()) {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        if (msg.content().readableBytes() > Config.MAX_LEN) {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        // 读取POST数据
        ByteBuf fu = msg.content();
        // 创建字节数组
        byte[] body = new byte[fu.readableBytes()];
        // 将缓存区内容读取到字节数组中
        fu.readBytes(body);
        // 字节数组转码UTF-8，字符串
        String url = msg.getUri();
        int urlNub = url.lastIndexOf(".");
        if (urlNub < 0) {
            if (!url.substring(url.length()-1).equals("/")) {
                url = url + "/index.html";
            } else {
                url = url + "index.html";
            }
        }
        urlNub = url.lastIndexOf(".") + 1;
        String fileFormat = url.substring(urlNub);
        byte[] br = urlMap.get(url);//字节码
        if (br != null) {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set("Content-Type", "text/" + fileFormat + "; charset=UTF-8");
            response.headers().set("Access-Control-Allow-Origin", "*");
            ByteBuf bu = response.content();
            bu.writeBytes(br);
        } else {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
        }
        arg0.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
