package cn.zyzpp.jerry;

import cn.zyzpp.util.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * Create by yster@foxmail.com 2018-05-06
 **/
public class JEArithmetic {
    private static final String before = "${";
    private static final String after = "}";
	/**
	 * 处理JE语法的算法
	 *
	 * @param Body
	 * @param jmap
	 * @return
	 */
	public static String replace(String Body, Map<String, Object> jmap) {
		if(jmap==null) {
			return Body;
		}	
		StringBuffer body = new StringBuffer(Body);
		while (true) {
			// 循环：每次获取第一个标签，然后更新页面内容，直到标签没有，结束。
			int start = body.lastIndexOf(before);
			int end = body.lastIndexOf(after);
			if (start < 0) {
				break;
			}

			// 获取用户标签内的字段
			String sub = body.substring(start + before.length(), end);

			// 遍历用户接口的json数据，比对key值与字段名
			for (String k : jmap.keySet()) {

				// 形如${message}
                int end1 = end + after.length();
                if (k.equals(sub)) {
					body.replace(start, end1, (String) jmap.get(k));
					continue;
				}

				if (sub.length() > k.length() && k.equals(sub.substring(0, k.length()))) {
					String subs = sub;
					Object json = jmap;

					// 形如${data[1].time} , ${state.message}
					for (int i = 0; i < StringUtil.appearNumber(sub, "\\.") + 1; i++) {
						String s = subs;
						if (subs.contains(".")) {
							s = subs.substring(0, subs.indexOf(".")); // 分段
						}

						if (s.contains("[") && s.contains("]")) {
							int in = Integer.parseInt(s.substring(s.length() - 2, s.length() - 1));// 数组下标
							String sn = s.substring(0, s.length() - 3);// 数组名
							subs = subs.substring(subs.indexOf(".") + 1);// 更新字符串
							List<Object> list = (List<Object>) ((Map<String, Object>) json).get(sn);
							json = list.get(in);// 更新
							continue;
						}
						
						subs = subs.substring(subs.indexOf(".") + 1);// 更新字符串
						json = ((Map<String, Object>) json).get(s);
					}
					body.replace(start, end1, json.toString());
				}
				//比较字段完毕
			}
		} // 遍历所有标签结束
		return body.toString();
	}
}
