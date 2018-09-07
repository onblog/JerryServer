# Jerry Server 开发文档

![](./picture/1536240835611.png)
## 1、Jerry 为何而生

以Java web举例，现在的网站系统开发模式，对于web端和服务端的数据交互以及页面渲染，无外乎两种：

1. 第一种是交给后端处理，Jsp，Freemark模板引擎之流，这种开发模式需要前端人员做好静态页面交给后端去处理一些其它工作。这种开发模式到如今也依旧流行，这也是招聘Java web程序员要求也要会HTML、JS、CSS的原因。到现在基本上都是采用这种开发模式，注意这种模式并非真正的前后端分离！
2. 另一种是交给前端处理，前端全部完成web端的页面渲染工作。要知道的是，前端处理只能使用JS，一些前端JS模板引擎也有不少，无论再花哨，本质依旧是JS。不可忽视的是，完全依赖JS处理前端页面存在弊端，如果没弊端的话那第一种模式也早就淘汰了。

## 2、Jerry 是什么

**Jerry是帮助前后端完全分离的工具，它可以帮助后端工程师只做后端，前端工程师只做前端。**

## 3、Jerry 的模式

1. 一种完全真正的前后端分离，Jerry采用如今最流行的JSON作为前后端数据交互的接口。

   对后端工程师来说，只需要关心接口的实现，不需要再接触前端页面，甚至不要求懂HTML，JS等。

2. 只做接口有什么优势？一套接口适用web、Android、ios各个平台，这对软件项目的可扩展性大大提升。不可否认的是，现在依旧有些网站采用后端直接返回html片段的开发模式，这对软件的扩展性非常不利。

3. 对前端工程师来说，任务脉络更为清晰而简洁。一是对页面进行类似JSP形式的渲染，这在之前是由后端工程师来做的。可以说是完全颠覆以往。而且，非JS渲染，自然不存在JS渲染页面的弊端。

## 4、Jerry 的优势

1. 快！启动快，毫秒级的启动！

   响应快，配置后台监控，响应时间一览无遗。

   优先读取各种文件的缓存，使用EhCache实现。

   对于后端接口，在缓存的基础上使用负载均衡。负载均衡算法为加权轮询。

   对于日志监控等耗时而且响应无关的操作，全部运行在其它线程。

   如果你还有其它加快速度的方法，欢迎联系我。

2. 轻！轻量级，没有过多依赖，大部分功能能自己实现就自己写。全部依赖如下：

   - Netty
   - slf4j + logback
   - fastjson
   - ehcache
   - jsoup
   - junit4

3. 自带监控系统。对页面的响应速度以及HTTP信息一览无遗。

## 5、Jerry 语法

Jerry 渲染HTML页面的语法类似EL表达式。形如：`${字段名}`，我们称之为JE表达式。

例如，服务端的JSON数据为：

```
{
	"message": "响应成功",
	"state": {
		"message": "ok",
	},
	"data": [{
		"time": "2018-04-25 13:25:07",
	}, {
		"time": "2018-04-25 13:25:07",
	}]

}
```

如果我们想获取 message的值渲染到html页面，只需要在html页面写入：`${message}`

对于state对象里的message，可以写入：`${state.message}`

对于data数组对象里的time值，可以写入：`${data[0].time}`

值得一提的是，JE暂时不支持if else，while等语法，这将是下一个版本的首要解决问题。

## 6、监控系统

监控系统对各个页面与文件的响应耗时与请求信息进行监控，监控频率可以在全局配置文件中进行自定义配置，默认为10s。

对于监控日志可以自定义输出目录，目录只支持相对路径（也就是只能在webapps目录下），监控日志格式为JSON。

默认提供的监控页面位于webapps/manage项目下。默认访问：http://ip地址:8888/manage

后台监控页面预览：

![](./picture/1536159902618.png)

监控系统运行流程：

![](./picture/1536216950162.png)

## 7、负载均衡

需要说明的是，对于Jerry的负载均衡与Nginx的负载均衡是不一样的。换一个角度来说，拥有cache的Jerry负载均衡功能更强大。因为Jerry已经缓存了整个文件，包括经过渲染后的含有JE语法的文件。所以在缓存时间内，它只会访问后端接口一次，只有缓存失效后，才会再次访问。如果你想体验加权负载均衡，可以把缓存时间设置为1。然后不断刷新页面。关于如何设置负载均衡，参考接口配置。



## 8、全局配置

全局配置文件位于config目录下，使用默认UTF-8编码进行读取。详细配置如下：

```
#开启端口
port=8888
#默认首页
index=index.html
#默认项目
project=ROOT
#全局404模板（webapps/）
404=/template/404.html
#接口配置文件名，要求内容为Json
config=page.json
#接口配置文件的编码
charset=UTF-8
#监控刷新频率(毫秒/ms)
monitor=10000
#监控文件目录（webapps/）
monitorLog=/manage/log.json
#缓存：最大存储元素个数
maxElementsInMemory=10000
#缓存：最大发呆时间(秒/s)
timeToIdleSeconds=120
#缓存：最大存活时间(秒/s)
timeToLiveSeconds=600
#控制台日志级别INFO/DEBUG
level=INFO
```

Jerry把所有的web项目与页面都放在了webapps下，服务器也只会响应webapps目录下的文件。

1. 关于全局404模板，默认即可。不过也支持自定义。
2. 关于接口配置文件，默认page.json。你可以自定义，但一定必须是json内容，而且位于项目根目录下（如ROOT/page.json）。
3. 接口配置文件的编码就是page.json文件的读取时的编码格式。默认utf-8
4. 监控刷新频率。最低为1s，默认10s。
5. 上面缓存的意思是在有效的600秒(10分钟)内，如果连续120秒(2分钟)未访问缓存，则缓存失效。就算有访问，也只会存活600秒。
6. 当你想查看运行日志时，切换为debug即可。默认info。

## 9、接口配置

先看一下示范文件，下面进行讲解。

```
[
  {
    "page": "index.html",
    "id": "je",
    "method": "GET",
    "timeout": 20000,
    "header": {
      "Connection": "keep-alive"
    },
    "inter": [
      {
        "link": "https://www.kuaidi100.com/query?type=shentong&postid=3374107234608&id=1",
        "weight": 1
      },
      {
        "link": "https://www.kuaidi100.com/query?type=yuantong&postid=801371015800473775",
        "weight": 2
      }
    ]
  },
  {
    "page": "admin/index.html",
    "id": "je",
    "method": "GET",
    "timeout": 20000,
    "header": {
      "Connection": "keep-alive"
    },
    "inter": [
      {
        "link": "https://www.kuaidi100.com/query?type=shentong&postid=3374107234608&id=1",
        "weight": 1
      }
    ]
  }
]
```

| 属性    | 说明                                      |
| ------- | ----------------------------------------- |
| page    | HTML文件路径。如配置xxx相当于：项目名/xxx |
| id      | ID名称。要求同一个page一定要有不同的id。  |
| method  | 对后端接口发起请求时的方法。              |
| timeout | 对后端接口发起请求的超时时间。            |
| header  | 对后端接口发起请求时的header。            |
| inter   | 后端接口对象，可以定义多个实现负载均衡。  |
| link    | 后端接口实际地址。                        |
| weight  | 权重，用于负载均衡。                      |

## 10、一些其它问题

### 1、Linux与Windows

在Linux部署Jerry服务器，访问文件严格区分大小写。而在Windows对大小写不敏感。

举个例子，访问/ROOT和/ROOt时，在Windows是可以的，在Linux是失败的。

如果在使用中您还遇到了其它Bug，欢迎在我的Git或者博客给我留言。

### 2、关于编码

对于不需要JE处理的HTML文件来说，不涉及编码问题。

对于需要JE处理的HTML文件，你在HTML文件中声明的`<meta charset="UTF-8">`关乎如何解析你的文件。若出现乱码，请检查你的HTML文件。

### 3、如何启动

请在Git克隆/下载本项目压缩包，解压后只需保留以下3个目录或文件，其它全部删除即可：

- /config
- /webapps
- JerryServer-0.0.1-SNAPSHOT.jar

然后在控制台或命令行执行`java -jar JerryServer-0.0.1-SNAPSHOT.jar`即可运行。

Linux系统如使其在后台运行，在末尾加个`&`即可。

然后使用的问题，一般来说，只需要把web项目放到webapps下，项目根目录新建page.json接口配置即可。
关于接口配置详细参数上面已经说明。然后在HTML页面使用JE表达式获取后端数据。例子：
API接口：
```{
       "message": "ok",
       "data": [
           {
               "time": "2018-09-06 16:57:43",
           }
   }
```
HTML页面：
```
<p>
    message：${message}<br>
    data[0].time：${data[0].time}
</p>
```
详见JE表达式用法。

## 11、尾声

### 禁止申请专利！
### 还有，非常感谢大家支持！

Jerry源于我四个月前的一个想法，经过了四个月的学习，然后最近把项目重构了一遍。发到了各个平台。不谈项目好坏，至少它让我对这段时间的学习做了一次实践。最后，很想说一句话，既然都在谈前后端分离，这次，不如分的彻底点！

我的博客：https://yueshutong.cnblogs.com/

Gitee：<https://gitee.com/zyzpp/JerryServer>

Github：<https://github.com/yueshutong/JerryServer/> 

开源中国：https://www.oschina.net/p/jerryserver