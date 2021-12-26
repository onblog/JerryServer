package com.github.onblog.http;

import com.github.onblog.cache.MyCache;
import com.github.onblog.config.HttpServerConfig;
import com.github.onblog.jerry.JEResolver;
import com.github.onblog.monitor.Monitor;
import com.github.onblog.util.IOUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Create by Martin 2018-05-04
 **/
public class JerryResponse {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JerryRequest request;
    private FullHttpResponse response;

    public JerryResponse(JerryRequest request) {
        this.request = request;
    }

    /**
     * 重定向
     * /ROOT? -> /ROOT/?
     * @return
     */
    public void redirect() {
        response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        String uri = request.getRequest().getUri();
        if (uri.contains("?")){
            uri = uri.substring(0,uri.indexOf("?"))+"/"+uri.substring(uri.indexOf("?"));
        }else {
            uri=uri+"/";
        }
        response.headers().set("Location", uri);
    }

    /**
     * 在response前后进行处理
     */
    public void prepare() {
        Long start = System.currentTimeMillis();

        response = response(request);

        long time = System.currentTimeMillis() - start;
        logger.debug("Find a request: /" + request.getProject() + "/" + request.getFilePath() + " LoadTime：" + time + "Content-Type: " + request.getContentType());
        new Monitor().openMonitor(request, time);//页面监控
    }

    /**
     * 在prepare()方法之后必须执行此方法完成提交
     *
     * @param ctx
     */
    public void complete(ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush(response);// Write the response.
    }

    /**
     * 封装response响应内容
     *
     * @param request
     * @return
     */
    private FullHttpResponse response(JerryRequest request) {
        ByteBuf buf = getByteBuf(request);

        if (HttpServerConfig.NO_FOUND.equalsIgnoreCase(request.getUri())) {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, buf);
        } else {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        }
        if (request.getHttpURLConnection()!=null) {
            //把Cookies原封不动还给客户端
            Map<String, List<String>> headers = request.getHttpURLConnection().getHeaderFields();
            if (headers.containsKey("Set-Cookie")) {
                response.headers().set(HttpHeaders.Names.SET_COOKIE,headers.get("Set-Cookie"));
            }
        }
        response.headers().set("Content-Type", request.getContentType());
        response.headers().set("Content-Length", buf.readableBytes());
        return response;
    }

    /**
     * 读取本地文件
     *
     * @param request
     * @return
     */
    private ByteBuf getByteBuf(JerryRequest request) {
        //缓存
        Element element = MyCache.cache.get(request.getUri());
        if (element != null && MyCache.isCache(request)) {
            logger.debug("use cache: " + request.getUri());
            return Unpooled.copiedBuffer((byte[]) element.getObjectValue());
        }

        ByteBuf buf;
        JEResolver resolver = new JEResolver(request);//读取项目接口配置

        //Is JE File ?
        byte[] responseBody;
        if (resolver.judge()) {
            responseBody = resolver.parse(request.getProject() + "/" + request.getFilePath());
            buf = Unpooled.copiedBuffer(responseBody);
        } else {
            responseBody = IOUtil.readFileToByte(request.getUri());
            buf = Unpooled.copiedBuffer(responseBody != null ? responseBody : new byte[1]);
            //缓存
            MyCache.cache.put(new Element(request.getUri(), responseBody));
        }

        return buf;
    }

}
