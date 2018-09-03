# 一：前言
- 一个灵感，用了两天写的一个web服务器，特点是实现自动渲染。
- 该服务器适用于前端开发人员，致力前后端完全分离。
- 相比JS渲染，本项目的特点就是站在了服务器的角度，非JS加载页面。
- 可以用一句话描述为：前端工程师使用的Tomcat服务器，所以可以称呼为Jreey。
# 二：功能
- 只需要一行配置，写明服务端JSON接口地址，即可实现自动渲染。
- 支持自定义标签，但千万不要和html标签重复。
- 后期想要加入语法或者负载均衡等功能，因项目前景不明，该功能暂未实现。
- 我的邮箱：yster@foxmail.com
- 我的博客：https://yueshutong.cnblogs.com/

# 三：下载
Github下载：[https://github.com/yueshutong/JerryServer/blob/master/Jerry.rar](https://github.com/yueshutong/JerryServer/blob/master/Jerry.rar)

Gitee下载：[https://gitee.com/zyzpp/JerryServer/blob/master/Jerry.rar](https://gitee.com/zyzpp/JerryServer/blob/master/Jerry.rar)

# 四：如何启动
###### 1.在config可以配置启动端口，在template里有404页面，webapps里放置项目。
![这里写图片描述](https://oscimg.oschina.net/oscnet/facd79c02198850dd045971f1e6a24a34b9.jpg)
##### 2.	windows用户双击startup.exe启动服务器，Linux执行./startup.sh
![这里写图片描述](https://oscimg.oschina.net/oscnet/c9782f0ef183d53629f01f53f7437f13dfe.jpg)

# 五：使用文档
##### 1.	web项目目录
![这里写图片描述](https://oscimg.oschina.net/oscnet/d3574c8c7b74d0ae89c56f48932c2fd7e83.jpg)
##### 2.编辑page.json 
![这里写图片描述](https://oscimg.oschina.net/oscnet/630ad31bf5b236f158cca1296b38a0f62dd.jpg)
##### 3.带JE自定义标签的HTML
![这里写图片描述](https://oscimg.oschina.net/oscnet/7f7c504e3c545fd29bb7a3e5c269c473fc9.jpg)
##### 4.访问该HTML页面
![这里写图片描述](https://oscimg.oschina.net/oscnet/dab778e4bc44db48c89437773d5287091cd.jpg)
##### 5.对比服务端提供的JSON数据
![这里写图片描述](https://oscimg.oschina.net/oscnet/b4ead4c097c2a8191886c4b621c04c92f38.jpg)


----------
# 六：核心依赖

### Maven

```
        <!-- Netty -->
        <dependency>
            <groupId>io.netty</groupId>
            <artifactId>netty-all</artifactId>
            <version>4.1.22.Final</version>
        </dependency>
```
# 七：响应类型
响应类型非常易扩展！只需要找到此方法扩展一下即可。
```
	public void prepare() {
		if (ENUMTYPE.getType(request.getType()) == null) {
			response = response(response, request, request.getType());
			return;
		}
		switch (ENUMTYPE.getType(request.getType())) {
		case HTML:
			response = response(response, request, ENUMTYPE.HTML.getType());
			break;
		case CSS:
			response = response(response, request, ENUMTYPE.CSS.getType());
			break;
		case JS:
			response = response(response, request, ENUMTYPE.JS.getType());
			break;
		case PNG:
			response = response(response, request, ENUMTYPE.PNG.getType());
			break;
		case GIF:
			response = response(response, request, ENUMTYPE.GIF.getType());
			break;
		case ICO:
			response = response(response, request, ENUMTYPE.ICO.getType());
			break;
		case JPG:
			response = response(response, request, ENUMTYPE.JPG.getType());
			break;
		}
	}
```

## 声明：原创作品，禁止申请专利！
