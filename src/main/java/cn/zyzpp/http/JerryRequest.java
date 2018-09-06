package cn.zyzpp.http;

import cn.zyzpp.config.HttpServerConfig;
import cn.zyzpp.util.IOUtil;
import cn.zyzpp.util.StringUtil;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

/**
 * 封装HttpRequest Create by yster@foxmail.com 2018-05-04
 **/
@SuppressWarnings("deprecation")
public class JerryRequest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private HttpRequest request;

    public JerryRequest(HttpRequest request) {
        this.request = request;
    }

    /**
     * Get Mothod
     * @return
     */
    public HttpMethod getMethod() {
        return request.getMethod();
    }

    /**
     * Get headers
     *
     * @return
     */
    public HttpHeaders headers() {
        return request.headers();
    }

    /**
     * Get protocolVersion
     * @return
     */
    public HttpVersion protocolVersion() {
        return request.protocolVersion();
    }

    /**
     * Get HttpRequest
     * @return
     */
    public HttpRequest getRequest() {
        return request;
    }

    /**
     * Get Uri
     * HTTP路径转File文件
     * @return
     */
    public String getUri() {
        // 访问根目录
        if ("/".equals(request.getUri())) {
            String path = HttpServerConfig.WEB_ROOT + File.separator + HttpServerConfig.PROJECT + File.separator + HttpServerConfig.INDEX;
            if (IOUtil.isFile(path)) {
                return path;
            }
            return HttpServerConfig.NO_FOUND;
        }
        // 以../结尾
        if ("/".equals(request.getUri().substring(request.getUri().length() - 1))) {
            String path = HttpServerConfig.WEB_ROOT + StringUtil.divideStr(request.getUri()) + HttpServerConfig.INDEX;
            if (IOUtil.isFile(path)) {
                return path;
            }
            return HttpServerConfig.NO_FOUND;
        }
        String path = HttpServerConfig.WEB_ROOT + StringUtil.divideStr(request.getUri());
        //处理带有参数的URL
        int w = path.indexOf("?");
        if (w != -1) {
            path = path.substring(0, w);
        }
        // 访问项目文件 ../index.js ../css.css
        if (IOUtil.isFile(path)) {
            return path;
        }
        // 访问项目文件夹 ../js
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
     * Get File Path
     * css/style.css
     *
     * @return
     */
    public String getFilePath() {
        String filePath = StringUtil.httpStr(getUri());
        for (String s : filePath.split(getProject()+"/")) {
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
            if (tokenizer.nextToken().equalsIgnoreCase(HttpServerConfig.WEBAPPS)) {
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
     * 主要针对只访问域名的情况
     *
     * @return
     */
    public boolean isRedirect() {
        if (!"/".equals(request.getUri().substring(request.getUri().length() - 1))) {
            String uri = getUri();
            if (IOUtil.isDirectory(uri)) {
                logger.debug("redirect" + uri);
                return true;
            }
        }
        return false;
    }

}
