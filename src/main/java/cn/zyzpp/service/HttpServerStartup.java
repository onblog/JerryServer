package cn.zyzpp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zyzpp.config.HttpServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
* Create by yster@foxmail.com 2018-05-04
**/
public class HttpServerStartup {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private long start, end;
	
	public void startup() {
		start = System.currentTimeMillis();		//计时
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new MyServerInitializer());	//继承重写类
            Channel ch = b.bind(HttpServerConfig.getPort()).sync().channel();	//自定义端口
            
            end = System.currentTimeMillis();
            logger.info("Jerry server "+HttpServerConfig.getPort() +" startup, Time "+ (end-start) + "ms");
            logger.info("WEB_ROOT : " + HttpServerConfig.WEB_ROOT);
            
            ch.closeFuture().sync();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //优雅的退出程序
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            logger.info("Jerry server stop !");
        }
	}

}
