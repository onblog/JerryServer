package com.github.onblog.jerry;

import com.github.onblog.balance.LoadBalance;
import com.github.onblog.util.Connection;
import com.github.onblog.config.HttpServerConfig;
import com.github.onblog.connect.Connect;
import com.github.onblog.page.EntityJson;
import com.github.onblog.exception.CustomException;
import com.github.onblog.http.JerryRequest;
import com.alibaba.fastjson.JSON;
import net.sf.json.util.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by Martin 2018-05-06
 **/
public class JEConnection {
    private static Logger logger = LoggerFactory.getLogger(JEConnection.class);

    /**
     * 解析本地配置
     * 请求接口数据
     *
     * @param request 页面名字
     * @param JData    本地配置
     * @return
     */
    public static Map<Integer, Object> connPort(JerryRequest request, String JData){
        String filePath = request.getFilePath();
        Map<Integer, Object> map = new HashMap<>();
        List<EntityJson> entity = JSON.parseArray(JData, EntityJson.class);
        for (EntityJson en : entity) {
            if (filePath.equalsIgnoreCase(en.getPage())) {
                //代理请求，得到响应字符串
                HttpURLConnection data = getProxyData(en, request);
                //保留响应对象
                request.setHttpURLConnection(data);
                //判断json对象的类型
                Object value = new JSONTokener(Connection.ConnectionUtil.getBody(data,HttpServerConfig.jk_charset)).nextValue();
                if(value instanceof net.sf.json.JSONObject){
                    Map<String,Object> object = (Map<String, Object>) value;
                    //加到map里
                    map.put(en.hashCode(), object);
                }else if (value instanceof net.sf.json.JSONArray){
                    List<Object> object = (List<Object>) value;
                    //加到map里
                    map.put(en.hashCode(), object);
                }
                //只加载第一条接口配置
                break;
            }
        }
        return map;
    }

    /**
     * 代理请求服务端json数据
     * @param en
     * @return
     */
    private static HttpURLConnection getProxyData(EntityJson en, JerryRequest request) {
        //负载均衡
        LoadBalance loadBalance = new LoadBalance(en);
        //代理请求接口数据
        HttpURLConnection receive = null;
        while (loadBalance.interUsableNum(en) > 0 && receive == null) {
            String url = loadBalance.loadBalance(en);
            logger.info("Load Balance : "+url);
            receive = Connect.receive(url, request);
            if (receive == null) {
                //记录不可用接口
                logger.warn(" Exception, The record is unavailable to the interface : "+url);
                loadBalance.interError(en, url);
            }
        }
        if (receive == null) {
            throw new CustomException("All configured interfaces are not available. Please check the configured interfaces");
        }
        return receive;
    }
}
