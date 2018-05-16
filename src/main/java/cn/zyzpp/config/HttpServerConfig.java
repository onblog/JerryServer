package cn.zyzpp.config;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * 配置信息：后期读取配置文件
 * 
 * Create by yster@foxmail.com 2018-05-04
 **/
public class HttpServerConfig {
	/**
	 * 根目录
	 */
	public static final String ROOT = System.getProperty("user.dir").substring(0,
			System.getProperty("user.dir").lastIndexOf("\\"));
	
	public static final String WEB_ROOT = System.getProperty("user.dir") + "\\webapps";
	
	public static final String NO_FOUND = System.getProperty("user.dir") + "\\template\\404.html";
	
	public static final String CONFIG = System.getProperty("user.dir") + "\\config\\config.properties";
	
	public static final String PAGE_JSON = "\\page.json";

	/**
	 * 端口
	 * @return
	 */
	public static int getPort() {
		Properties prop = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(CONFIG);
			prop.load(fis);
			prop.list(System.out);
			int port = Integer.parseInt(prop.getProperty("port"));
			return port;
		} catch (Exception e) {
			//e.printStackTrace();
			return 8888;
		} 

	}

}
