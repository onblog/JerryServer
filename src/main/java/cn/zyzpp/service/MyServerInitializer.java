package cn.zyzpp.service;

import cn.zyzpp.handler.MyServerChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Create by yster@foxmail.com 2018-05-04
**/
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();  
        /** 
         * http-request解码器 
         * http服务器端对request解码 
         */  
        pipeline.addLast("decoder", new HttpRequestDecoder());  
        /** 
         * http-response解码器 
         * http服务器端对response编码 
         */  
        pipeline.addLast("encoder", new HttpResponseEncoder());  
        pipeline.addLast("deflater", new HttpContentCompressor());  
        pipeline.addLast("handler", new MyServerChannelHandler());  
	}
}
