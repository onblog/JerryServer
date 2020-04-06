package cn.zyzpp;

import cn.zyzpp.service.HttpServerStartup;

public class App {
	/**
	 * Jerry server startup !
	 */
	public static void main(String[] args) {
		new HttpServerStartup().startup();
	}
}
