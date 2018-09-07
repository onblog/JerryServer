package cn.zyzpp.jerry;

import cn.zyzpp.balance.LoadBalance;
import cn.zyzpp.connect.JsoupConn;
import cn.zyzpp.entity.EntityJson;
import cn.zyzpp.exception.CustomException;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by yster@foxmail.com 2018-05-06
 **/
public class JEConnection {
    private static Logger logger = LoggerFactory.getLogger(JEConnection.class);

    /**
     * 解析本地配置
     * 请求接口数据
     *
     * @param filePath 页面名字
     * @param JData    本地配置
     * @return
     */
    public static Map<Integer, Object> connPort(String filePath, String JData) {
        Map<Integer, Object> map = new HashMap<>();
        List<EntityJson> entity = JSON.parseArray(JData, EntityJson.class);
        for (EntityJson en : entity) {
            if (filePath.equalsIgnoreCase(en.getPage())) {
                //代理请求
                String receive = getProxyData(en);
                //json转换为Map对象
                Map<String, Object> object = JSON.parseObject(receive);
                //加到map里
                map.put(en.hashCode(), object);
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
    private static String getProxyData(EntityJson en) {
        //负载均衡
        LoadBalance loadBalance = new LoadBalance(en);
        //代理请求接口数据
        String receive = null;
        while (loadBalance.interUsableNum(en) > 0 && receive == null) {
            String url = loadBalance.loadBalance(en);
            logger.debug("Load Balance : "+url);
            receive = JsoupConn.receive(en, url);
            if (receive == null) {
                //记录不可用接口
                loadBalance.interError(en, url);
            }
        }
        if (receive == null) {
            throw new CustomException("All configured interfaces are not available. Please check the configured interfaces");
        }
        return receive;
    }
}
