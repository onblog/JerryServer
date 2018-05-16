package cn.zyzpp.je.data;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.zyzpp.config.HttpServerConfig;
import cn.zyzpp.entity.EntityJson;
import cn.zyzpp.http.JerryRequest;
import cn.zyzpp.util.IOUtil;

/**
 * Create by yster@foxmail.com 2018-05-05
 **/
@SuppressWarnings("unchecked")
public class JEResolver {
	private final  Logger logger = LoggerFactory.getLogger(getClass());
	
	private String JData;		//	本地page.Json配置
	
	private JerryRequest request;
	
	public JEResolver(JerryRequest request) {
		setJData(request);
		this.request = request;
	}
	
	public void setJData(JerryRequest request) {
		String uri = request.getUri().substring(1, request.getUri().length());
		if(uri.lastIndexOf("/") == -1) {
			this.JData = null;
			logger.error("get page.json failed !");
			return;
		}
		uri = uri.substring(0, uri.lastIndexOf("/"));
		this.JData = IOUtil.readFile("/" + uri + HttpServerConfig.PAGE_JSON);
	}
	
	/**
	 * 处理含JE语法页面的对外接口
	 * @param request
	 * @param responseBody
	 * @return
	 */
	public String parse(String responseBody) {
		if(responseBody == null) {
			return null;
		}
		//网络请求json数据
		Map<String, Object> map = JEConnection.connPort(request.getFileName(), JData);	
		String body = responseBody;
		for (String key : map.keySet()) {
			Map<String, Object> jmap = (Map<String, Object>) map.get(key);
			body = JEArithmetic.replace(key, body, jmap);	//JE核心算法
		}
		return body;

	}
	
	/**
	 * 判断HTML是否需要JE处理
	 * 
	 * @param request
	 * @return
	 */
	public  boolean judge() {
		if(JData == null) {
			return false;
		}
		List<EntityJson> entity = JSON.parseArray(JData, EntityJson.class);
		for (EntityJson en : entity) {
			if (request.getFileName().equals(en.getPage())) {
				return true;
			}
		}
		return false;
	}

}
