package com.github.onblog.jerry;

import com.github.onblog.template.FreeMark;
import com.github.onblog.config.HttpServerConfig;
import com.github.onblog.page.EntityJson;
import com.github.onblog.http.JerryRequest;
import com.github.onblog.util.IOUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Create by Martin 2018-05-05
 **/
public class JEResolver {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String JData = null;        //	本地page.Json配置字符串

    private JerryRequest request;

    public JEResolver(JerryRequest request) {
        setJData(request);
        this.request = request;
    }

    /**
     * 判断项目根目录下是否有Page.json配置文件
     *
     * @param request
     */
    private void setJData(JerryRequest request) {
        //获取项目名
        String project = null;
        try {
            project = request.getProject();
        } catch (Exception e) {
            logger.error("get project name failed !");
        }
        this.JData = IOUtil.readFile(HttpServerConfig.WEB_ROOT + File.separator + project + File.separator + HttpServerConfig.Config_Json, HttpServerConfig.js_charset);
    }

    /**
     * 处理含有语法的页面
     *
     * @param path
     * @return
     */
    public byte[] parse(String path) {
        //网络代理请求json转为Map对象
        Map<Integer, Object> map = JEConnection.connPort(request, JData);
        byte[] page = null;
        for (int key : map.keySet()) {
            Map<String, Object> objectMap = (Map<String, Object>) map.get(key);
            page = FreeMark.resolve(objectMap, path);
            break;
        }
        return page;

    }

    /**
     * 判断HTML是否需要JE处理
     *
     * @return
     */
    public boolean judge() {
        //无page.json配置数据
        if (JData == null) {
            return false;
        }
        List<EntityJson> entity = JSON.parseArray(JData, EntityJson.class);
        for (EntityJson en : entity) {
            if (request.getFilePath().equalsIgnoreCase(en.getPage())) {
                logger.debug("Find JE File :" + request.getFilePath() + " = " + en.getPage());
                return true;
            }
        }
        return false;
    }

}
