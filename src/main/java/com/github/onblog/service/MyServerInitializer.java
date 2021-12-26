package com.github.onblog.service;

import com.github.onblog.handler.MyServerChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 信道初始化
 * Create by Martin 2018-05-04
**/
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();  
//        //http-request解码器
//        pipeline.addLast("decoder", new HttpRequestDecoder());
//        //http-response解码器
//        pipeline.addLast("encoder", new HttpResponseEncoder());
//        pipeline.addLast("deflater", new HttpContentCompressor());
//        pipeline.addLast("handler", new MyServerChannelHandler());
        pipeline.addLast(new HttpServerCodec());// HTTP解码器
        pipeline.addLast(new HttpObjectAggregator(65536));// 将多个消息转换为单一的一个FullHttpRequest
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new MyServerChannelHandler());
	}
}
