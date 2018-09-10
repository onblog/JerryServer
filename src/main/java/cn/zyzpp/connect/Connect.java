package cn.zyzpp.connect;

import cn.zyzpp.config.HttpServerConfig;
import cn.zyzpp.exception.CustomException;
import cn.zyzpp.http.JerryRequest;
import cn.zyzpp.config.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Create by yster@foxmail.com 2018-05-05
 **/
public class Connect {
    private static Logger logger = LoggerFactory.getLogger(Connect.class);

    public static HttpURLConnection receive(String url, JerryRequest request) {
        try {
            HttpURLConnection response = getResponse(url, request);
            if (response.getResponseCode() == 200) {
                return response;
            }
            throw new CustomException("Url exception: " + url + " [code]" + response.getResponseCode());
        } catch (IOException e) {
            logger.error("Url exception: " + url);
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection getResponse(String url, JerryRequest request) throws IOException {
        HttpURLConnection execute = Connection.connect(url + request.getGetParm())
                .setRequestMethod(request.getMethod())
                .setHeaders(request.headers())
                .setData(request.getPostParm())
                .setReadTimeout(HttpServerConfig.TimeOut)
                .execute();
        return execute;
    }

}
