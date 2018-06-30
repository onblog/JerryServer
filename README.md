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


----------

# 四：web服务器启动
###### 1.在config可以配置启动端口，在template里有404页面，webapps里放置项目。
![这里写图片描述](https://img-blog.csdn.net/20180516220540990?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3l1ZXNodXRvbmcxMjM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
##### 2.	windows用户双击startup.exe启动服务器，Linux执行./startup.sh
![这里写图片描述](https://img-blog.csdn.net/20180516220602380?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3l1ZXNodXRvbmcxMjM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)


----------


# 五：使用文档
##### 1.	web项目目录
![这里写图片描述](https://img-blog.csdn.net/20180516220258276?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3l1ZXNodXRvbmcxMjM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
##### 2.编辑page.json 
![这里写图片描述](https://img-blog.csdn.net/20180516220444377?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3l1ZXNodXRvbmcxMjM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
##### 3.带JE自定义标签的HTML
![这里写图片描述](https://img-blog.csdn.net/20180516220458448?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3l1ZXNodXRvbmcxMjM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
##### 4.访问该HTML页面
![这里写图片描述](https://img-blog.csdn.net/20180516220511185?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3l1ZXNodXRvbmcxMjM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
##### 5.对比服务端提供的JSON数据
![这里写图片描述](https://img-blog.csdn.net/20180516220518838?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3l1ZXNodXRvbmcxMjM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)


----------
