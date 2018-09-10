package cn.zyzpp.handler;

import cn.zyzpp.http.JerryRequest;
import cn.zyzpp.http.JerryResponse;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 绑定处理程序中的简单通道
 * Create by yster@foxmail.com 2018-05-04
 **/
@Sharable
public class MyServerChannelHandler extends SimpleChannelInboundHandler<HttpObject> {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg){
		if (msg instanceof FullHttpRequest) {
			JerryRequest request = new JerryRequest((FullHttpRequest) msg);
			JerryResponse response = new JerryResponse(request);
			if (request.isRedirect()) {
				response.redirect();
				response.complete(ctx);
			} else {
				response.prepare();
				response.complete(ctx);
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.warn("exception occurred !");
		ctx.channel().close();
		cause.printStackTrace();
	}

}
