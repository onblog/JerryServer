package com.github.onblog.connect;

import com.github.onblog.config.HttpServerConfig;
import com.github.onblog.exception.CustomException;
import com.github.onblog.http.JerryRequest;
import com.github.onblog.util.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Create by Martin 2018-05-05
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
