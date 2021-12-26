package com.github.onblog.http;

import com.github.onblog.config.HttpServerConfig;
import com.github.onblog.util.IOUtil;
import com.github.onblog.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MixedAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 封装HttpRequest Create by Martin 2018-05-04
 **/
public class JerryRequest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private FullHttpRequest request;

    private HttpURLConnection httpURLConnection;

    private HttpPostRequestDecoder decoder;

    public JerryRequest(FullHttpRequest request) {
        this.request = request;
        decoder = new HttpPostRequestDecoder(request);
        decoder.offer(request);

    }

    /**
     * Get Mothod
     *
     * @return
     */
    public String getMethod() {
        return request.method().name().toUpperCase();
    }

    /**
     * Get headers
     *
     * @return
     */
    public Map<String, String> headers() {
        HttpHeaders headers = request.headers();
        List<Map.Entry<String, String>> entries = headers.entries();
        Map<String, String> map = new HashMap<>();
        for (Map.Entry entry : entries) {
            map.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return map;
    }

    /**
     * Get protocolVersion
     *
     * @return
     */
    public HttpVersion protocolVersion() {
        return request.protocolVersion();
    }

    /**
     * Get HttpRequest
     *
     * @return
     */
    public FullHttpRequest getRequest() {
        return request;
    }

    /**
     * Get Uri
     * HTTP URl转File Path
     *
     * @return
     */
    public String getUri() {
        //处理带有参数的URL
        String uri = request.uri();
        int w = uri.indexOf("?");
        if (w != -1) {
            uri = uri.substring(0, w);
        }
        //uri分隔符转为系统路径分隔符
        uri = StringUtil.divideStr(uri);
        // 访问根目录
        if (File.separator.equals(uri)) {
            String path = HttpServerConfig.WEB_ROOT + File.separator + HttpServerConfig.PROJECT + File.separator + HttpServerConfig.INDEX;
            if (IOUtil.isFile(path)) {
                return path;
            }
            return HttpServerConfig.NO_FOUND;
        }
        // 以../结尾的目录
        if (File.separator.equals(uri.substring(uri.length() - 1))) {
            String path = HttpServerConfig.WEB_ROOT + uri + HttpServerConfig.INDEX;
            if (IOUtil.isFile(path)) {
                return path;
            }
            String path1 = HttpServerConfig.WEB_ROOT + File.separator + HttpServerConfig.PROJECT + uri + HttpServerConfig.INDEX;
            if (IOUtil.isFile(path1)) {
                return path1;
            }
            return HttpServerConfig.NO_FOUND;
        }

        String path = HttpServerConfig.WEB_ROOT + uri;
        // 访问项目文件 ../index.js ../css.css
        if (IOUtil.isFile(path)) {
            return path;
        }
        // 访问项目文件夹 ../js
        // 会被重定向到../
        if (IOUtil.isDirectory(path)) {
            return path;
        }

        // 找不到文件再去ROOT目录下找
        path = HttpServerConfig.WEB_ROOT + File.separator + HttpServerConfig.PROJECT + uri;
        if (IOUtil.isFile(path)) {
            return path;
        }
        if (IOUtil.isDirectory(path)) {
            return path;
        }

        // 没找到这个请求资源
        return HttpServerConfig.NO_FOUND;
    }

    /**
     * Get Content-Type
     *
     * @return
     */
    public String getContentType() {
        //利用JDK自带的类判断文件ContentType
        Path path = Paths.get(getUri());
        String content_type = null;
        try {
            content_type = Files.probeContentType(path);
        } catch (IOException e) {
            logger.error("Read File ContentType Error");
        }
        //若失败则调用另一个方法进行判断
        if (content_type == null) {
            content_type = new MimetypesFileTypeMap().getContentType(new File(getUri()));
        }
        return content_type;
    }

    /**
     * Get File Name
     * style.css
     *
     * @return
     */
    public String getFileName() {
        String name = getUri().substring(getUri().lastIndexOf(File.separator) + 1);
        int w = name.indexOf("?");
        if (w != -1) {
            name = name.substring(0, w);
        }
        return name;
    }

    /**
     * Get GET Parmas
     * ?name=xxx
     * <br>或者null
     *
     * @return
     */
    public String getGetParm() {
        if (request.uri().contains("?")) {
            int i = request.uri().indexOf("?");
            return request.uri().substring(i);
        }
        return "";
    }

    /**
     * Get POST Parma
     * Map
     *
     * @return
     */
    public Map<String, String> getPostParm() throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        try {
            List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
            for (InterfaceHttpData parm : parmList) {
                MixedAttribute data = (MixedAttribute) parm;
                data.setCharset(Charset.forName("UTF-8"));
                map.put(data.getName(), data.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Get Content
     */
    public String getContent() {
        ByteBuf content = request.content();
        String s = content.toString(Charset.forName("UTF-8"));
        return s;
    }

    /**
     * Get File Path
     * css/style.css
     *
     * @return
     */
    public String getFilePath() {
        String filePath = StringUtil.httpStr(getUri());
        for (String s : filePath.split(getProject() + "/")) {
            filePath = s;
        }
        return filePath;
    }

    /**
     * 获取页面对应的项目目录名
     * ROOT
     *
     * @return
     */
    public String getProject() {
        boolean ready = false;
        StringTokenizer tokenizer = new StringTokenizer(getUri(), File.separator);
        while (tokenizer.hasMoreTokens()) {
            if (tokenizer.nextToken().equalsIgnoreCase(HttpServerConfig.WEB_NAME)) {
                ready = true;
            }
            if (ready) {
                return tokenizer.nextToken();
            }
        }
        return null;
    }

    /**
     * 是否需要重定向
     * <br>
     * 针对/ROOT?这种URI路径
     * @return
     */
    public boolean isRedirect() {
        String uri = request.getUri();
        if (uri.contains("?")){
            uri = uri.substring(0,uri.indexOf("?"));
        }
        //如果不是以/结尾的url
        if (!"/".equals(uri.substring(uri.length() - 1))) {
            String uri1 = getUri();
            if (IOUtil.isDirectory(uri1)) {
                logger.debug("redirect" + uri1);
                return true;
            }
        }
        return false;
    }


    public HttpURLConnection getHttpURLConnection() {
        return httpURLConnection;
    }

    public void setHttpURLConnection(HttpURLConnection httpURLConnection) {
        this.httpURLConnection = httpURLConnection;
    }
}
