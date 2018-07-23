# 一：前言
- 一个灵感，花了两天时间写的一个静态web服务器。
- 该服务器适用于前端开发人员，致力前后端完全分离。
-  相比Vue.js，本项目的特点就是站在了服务器的角度，非JS加载页面。
# 二：功能
- 只需要一行配置，写明服务端JSON接口地址，即可实现自动渲染。
- 支持自定义标签。
- 后期想要加入语法，因项目前景不明，该功能暂未实现。
- 我的邮箱：yster@foxmail.com

# 三：下载
Github下载：[https://github.com/yueshutong/JerryServer/blob/master/Jerry.rar](https://github.com/yueshutong/JerryServer/blob/master/Jerry.rar)


# 四：使用教程
CSDN blog：https://blog.csdn.net/yueshutong123/article/details/80343953


----------
# 五：核心代码

阅读前请参考

- [Netty入门（一）之webSocket聊天室](https://blog.csdn.net/yueshutong123/article/details/79439773)
- [Netty入门（二）之PC聊天室](https://blog.csdn.net/yueshutong123/article/details/79440054)

有了前两篇的使用基础，只需要在前两文的基础上稍微改动即可！

### Maven依赖

```
        <!-- Netty -->
        <dependency>
            <groupId>io.netty</groupId>
            <artifactId>netty-all</artifactId>
            <version>4.1.22.Final</version>
        </dependency>
```



## 一：启动类

关于启动类，需要我们做的就是自定义端口以及继承ChannelInitializer类。

```
/**
 * Netty服务器启动
 * Create by yster@foxmail.com 2018-05-04
 **/
public class HttpServerStartup {
    public void startup() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new MyServerInitializer());    //继承重写类
            Channel ch = b.bind(8888).sync().channel();    //自定义端口
            ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //优雅的退出程序
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}  
```



## 二：信道初始化

```
/**
 * 信道初始化
 * Create by yster@foxmail.com 2018-05-04
**/
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();  
        /** 
         * http-request解码器 
         * http服务器端对request解码 
         */  
        pipeline.addLast("decoder", new HttpRequestDecoder());  
        /** 
         * http-response解码器 
         * http服务器端对response编码 
         */  
        pipeline.addLast("encoder", new HttpResponseEncoder());  
        pipeline.addLast("deflater", new HttpContentCompressor());  
        pipeline.addLast("handler", new MyServerChannelHandler());  
	}
}
```



## 三：绑定处理程序中的信道

```
/**
 * 绑定处理程序中的简单通道
 * Create by yster@foxmail.com 2018-05-04
 **/
@Sharable
public class MyServerChannelHandler extends SimpleChannelInboundHandler<HttpObject> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg){
		if (msg instanceof HttpRequest) {
		   //request response都已经获取到！
		   DefaultFullHttpResponse response = new DefaultFullHttpResponse();
		   ctx.channel().writeAndFlush(response);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.channel().close();
		cause.printStackTrace();
	}

}

```
