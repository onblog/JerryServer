package cn.zyzpp.connect;

import cn.zyzpp.entity.EntityJson;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Create by yster@foxmail.com 2018-05-05
**/
public class JsoupConn {
    private static Logger logger = LoggerFactory.getLogger(JsoupConn.class);

	public static String receive(EntityJson en, String url){
		Response response;
		try {
			response = Jsoup.connect(url).ignoreContentType(true)
					.method(Method.valueOf(en.getMethod()))
                    .headers(en.getHeader())
					.timeout(en.getTimeout())
					.execute();
			return response.body();
		} catch (IOException e) {
            logger.error("Url request exception: " + url);
			e.printStackTrace();
			return null;
		}
	}

}
