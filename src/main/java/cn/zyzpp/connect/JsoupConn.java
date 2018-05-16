package cn.zyzpp.connect;

import org.jsoup.Jsoup;

import java.io.IOException;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

/**
 * Create by yster@foxmail.com 2018-05-05
**/
public class JsoupConn {

	public static String receive(String url){
		Response response = null;
		try {
			response = Jsoup.connect(url).ignoreContentType(true)
					.method(Method.GET)
					.timeout(10000).execute();
			return response.body();
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}
	}

}
