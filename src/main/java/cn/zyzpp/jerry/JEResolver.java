package cn.zyzpp.jerry;

import cn.zyzpp.config.HttpServerConfig;
import cn.zyzpp.entity.EntityJson;
import cn.zyzpp.http.JerryRequest;
import cn.zyzpp.util.IOUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Create by yster@foxmail.com 2018-05-05
 **/
@SuppressWarnings("unchecked")
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
        this.JData = IOUtil.readFile(HttpServerConfig.WEB_ROOT +File.separator+ project + File.separator + HttpServerConfig.Config_Json,HttpServerConfig.charset);
    }

    /**
     * 处理含JE语法页面的对外接口
     *
     * @param responseBody
     * @return
     */
    public String parse(String responseBody) {
        if (responseBody == null) {
            return null;
        }
        //网络代理请求json转为Map对象
        Map<Integer, Object> map = JEConnection.connPort(request.getFilePath(), JData);
        String body = responseBody;
        for (int key : map.keySet()) {
            Map<String, Object> jmap = (Map<String, Object>) map.get(key);
            try {
                body = JEArithmetic.replace(body, jmap);    //JE核心算法
            } catch (Exception e) {
                logger.error("JE Page syntax error, please check the page");
                e.printStackTrace();
            }
        }
        return body;

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
                logger.debug("Find JE File :"+request.getFilePath()+" = "+en.getPage());
                return true;
            }
        }
        return false;
    }

}
