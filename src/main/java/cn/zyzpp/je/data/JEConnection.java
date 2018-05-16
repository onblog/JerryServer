package cn.zyzpp.je.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import cn.zyzpp.connect.JsoupConn;
import cn.zyzpp.entity.EntityJson;

/**
 * Create by yster@foxmail.com 2018-05-06
**/
public class JEConnection {
	
	/**
	 * 解析本地配置
	 * 请求接口数据
	 * @param fileName 页面名字
	 * @param JData 本地配置
	 * @return
	 */
	public static Map<String, Object> connPort(String fileName, String JData) {
		Map<String, Object> map = new HashMap<>();
		List<EntityJson> entity = JSON.parseArray(JData, EntityJson.class);
		for (EntityJson en : entity) {
			if (fileName.equals(en.getPage())) {
				//网络请求接口数据
				Map<String, Object> object = JSON.parseObject(JsoupConn.receive(en.getInter()));
				map.put(en.getId(), object);
			}
		}
		return map;
	}
}
