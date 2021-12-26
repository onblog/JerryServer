package com.github.onblog;

import com.github.onblog.service.HttpServerStartup;

public class App {
	/**
	 * Jerry server startup !
	 */
	public static void main(String[] args) {
		new HttpServerStartup().startup();
	}
}
