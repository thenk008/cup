package org.wlld.web.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import sun.misc.BASE64Decoder;

import java.net.InetSocketAddress;

public class HttpServer {
	public  void connect() {
		EventLoopGroup group = new NioEventLoopGroup();
		EventLoopGroup boss = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group, boss).channel(NioServerSocketChannel.class).childOption(ChannelOption.TCP_NODELAY, true).childHandler(new Em());
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] decode = decoder.decodeBuffer(Config.startAni);
			String mar = new String(decode,"UTF-8");
			System.out.println(mar);
			System.out.println("启动");
			Channel nel = b.bind(new InetSocketAddress(Config.Server_PORT)).sync().channel();
			System.out.println("端口打开 "+Config.Server_PORT);
			nel.closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			group.shutdownGracefully();
			boss.shutdownGracefully();
		}
	}
}

class Em extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new HttpServerCodec());
		ch.pipeline().addLast(new HttpObjectAggregator(65536));
		ch.pipeline().addLast(new ChunkedWriteHandler());
		ch.pipeline().addLast(new Http());
	}

}