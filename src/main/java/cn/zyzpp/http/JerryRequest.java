package cn.zyzpp.http;

import cn.zyzpp.config.HttpServerConfig;
import cn.zyzpp.util.IOUtil;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;

/**
 * 
 * 封装HttpRequest Create by yster@foxmail.com 2018-05-04
 **/
@SuppressWarnings("deprecation")
public class JerryRequest {
	
	private HttpRequest request;

	public JerryRequest(HttpRequest request) {
		this.request = request;
	}

	/**
	 * Get Mothod
	 * 
	 * @return
	 */
	public HttpMethod getMothod() {
		return request.getMethod();
	}

	/**
	 * Get Uri
	 * 
	 * @return
	 */
	public String getUri() {
		// 访问根目录
		if ("/".equals(request.getUri())) {
			if(IOUtil.isFile("/ROOT/index.html")) {
				return "/ROOT/index.html";
			}
			return HttpServerConfig.NO_FOUND;
		}	
		// 以../结尾，默认访问该目录下index.html
		if("/".equals(request.getUri().substring(request.getUri().length() - 1))) {
			// ../
			if(IOUtil.isFile(request.getUri() + "index.html")){
				return request.getUri() + "index.html";
			}
			return HttpServerConfig.NO_FOUND;
		}
		// 访问项目文件 ../index.js ../css.css
		if(IOUtil.isFile(request.getUri())){
			return request.getUri();
		}
		// 访问项目文件夹 ../js
		if(IOUtil.isDirectory(request.getUri())) {
			return request.getUri();
		}
		// 没找到这个请求资源
		return HttpServerConfig.NO_FOUND;
	}

	/**
	 * Get Type
	 * 
	 * @return
	 */
	public String getType() {
		String s = getUri().substring(getUri().lastIndexOf("/") + 1);
		if (s.indexOf(".") != -1) {
			return s.substring(s.lastIndexOf(".") + 1);
		}
		return null;
	}
	/**
	 * File Name
	 * @return
	 */
	public String getFileName(){
		 return getUri().substring(getUri().lastIndexOf("/") + 1,getUri().lastIndexOf("."));
	}
	
	/**
	 * 是否需要重定向
	 * @return
	 */
	public boolean isRedirect() {
		if(!"/".equals(request.getUri().substring(request.getUri().length() - 1))) {
			if(IOUtil.isDirectory(request.getUri())) {
				return true;
			}
		}
		return false;
	}
	
}
