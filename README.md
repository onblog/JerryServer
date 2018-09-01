# 一：前言
- 一个灵感，用了两天写的一个web服务器，特点是实现自动渲染。
- 该服务器适用于前端开发人员，致力前后端完全分离。
- 相比Vue.js，本项目的特点就是站在了服务器的角度，非JS加载页面。
- 可以用一句话描述为：前端工程师使用的Tomcat服务器。
# 二：功能
- 只需要一行配置，写明服务端JSON接口地址，即可实现自动渲染。
- 支持自定义标签，但千万不要和html标签重复。
- 后期想要加入语法，因项目前景不明，该功能暂未实现。
- 我的邮箱：yster@foxmail.com
- 我的博客：https://yueshutong.cnblogs.com/

# 三：下载
Github下载：[https://github.com/yueshutong/JerryServer/blob/master/Jerry.rar](https://github.com/yueshutong/JerryServer/blob/master/Jerry.rar)

Gitee下载：[https://gitee.com/zyzpp/JerryServer/blob/master/Jerry.rar](https://gitee.com/zyzpp/JerryServer/blob/master/Jerry.rar)

# 四：文档及使用说明
https://gitee.com/zyzpp/JerryServer

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

PS：不要以不好看的文档而否认我的创意哦~~[捂脸]
## 声明：原创作品，禁止申请专利！
