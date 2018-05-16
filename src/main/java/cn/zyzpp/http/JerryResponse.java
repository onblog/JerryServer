package cn.zyzpp.http;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zyzpp.config.HttpServerConfig;
import cn.zyzpp.enums.ENUMTYPE;
import cn.zyzpp.je.data.JEResolver;
import cn.zyzpp.util.IOUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * Create by yster@foxmail.com 2018-05-04
 **/
public class JerryResponse {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private JerryRequest request;
	private FullHttpResponse response;

	public JerryResponse(JerryRequest request) {
		this.request = request;
	}
	
	/**
	 * 比对request请求类型
	 */
	public void prepare() {
		if (ENUMTYPE.getType(request.getType()) == null) {
			response = response(response, request, request.getType());
			return;
		}
		switch (ENUMTYPE.getType(request.getType())) {
		case HTML:
			response = response(response, request, ENUMTYPE.HTML.getType());
			break;
		case CSS:
			response = response(response, request, ENUMTYPE.CSS.getType());
			break;
		case JS:
			response = response(response, request, ENUMTYPE.JS.getType());
			break;
		case PNG:
			response = response(response, request, ENUMTYPE.PNG.getType());
			break;
		case GIF:
			response = response(response, request, ENUMTYPE.GIF.getType());
			break;
		case ICO:
			response = response(response, request, ENUMTYPE.ICO.getType());
			break;
		case JPG:
			response = response(response, request, ENUMTYPE.JPG.getType());
			break;
		}
	}

	/**
	 * 在prepare()方法之后必须执行此方法完成提交
	 * 
	 * @param ctx
	 */
	public void complete(ChannelHandlerContext ctx) {
		// Write the response.
		ctx.channel().writeAndFlush(response);

	}
	/**
	 * 封装response响应内容
	 * @param response
	 * @param request
	 * @param TYPE
	 * @return
	 */
	public FullHttpResponse response(FullHttpResponse response, JerryRequest request, String TYPE) {
		logger.info("Find a request : " + request.getUri() + " TYPE : " + request.getType());

		ByteBuf buf;	
		JEResolver resolver = new JEResolver(request);
		
		//Or JE File  
		if(resolver.judge()) {
			String responseJEBody = resolver.parse( IOUtil.readFile(request.getUri()));
			buf = Unpooled.copiedBuffer(responseJEBody, Charset.forName("UTF-8"));
		}else {
			byte[] responseBody = IOUtil.readFileToByte(request.getUri());
			buf = Unpooled.copiedBuffer(responseBody);
		}
		
		if(HttpServerConfig.NO_FOUND.equals(request.getUri())) {
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, buf);
		}else {
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
		}
		response.headers().set("Content-Type", TYPE);
		response.headers().set("Content-Length", buf.readableBytes());
		return response;
	}
	/**
	 * 重定向
	 * @param response
	 * @param request
	 * @param TYPE
	 * @return
	 */
	public FullHttpResponse redirect() {
		response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
		response.headers().set("Location",request.getUri()+"/");
		return response;
	}

}
