package cn.zyzpp;

import cn.zyzpp.service.HttpServerStartup;

public class App {
	/**
	 * Jerry server startup !
	 */
	public static void main(String[] args) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				new HttpServerStartup().startup();
			}
		}).start();;
	}
}
