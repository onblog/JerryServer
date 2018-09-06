package cn.zyzpp.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.zyzpp.util.ProjectPath;
import cn.zyzpp.util.StringUtil;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * 配置信息：读取配置文件
 * <br>
 * Create by yster@foxmail.com 2018-05-04
 **/
public class HttpServerConfig {

    public static final String WEBAPPS = "webapps";
    /**
     * WEB_ROOT不是以分隔符结尾的
     */
    public static final String WEB_ROOT = ProjectPath.getRootPath(WEBAPPS);

    private static final String CONFIG = ProjectPath.getRootPath("config\\config.properties");

    public static String NO_FOUND;

    public static int Monitor;

    public static int Port;

    public static String Config_Json;

    public static String PROJECT;

    public static String INDEX;

    public static String MONITOR_LOG;

    public static Charset charset;

    public static int maxElementsInMemory;

    public static int timeToIdleSeconds;

    public static int timeToLiveSeconds;

    private static Properties prop;

    static {
        try {
            prop = new Properties();
            prop.load(new InputStreamReader(new FileInputStream(CONFIG),"UTF-8"));
            prop.list(System.out);

            MONITOR_LOG = WEB_ROOT + getMonitorLog();
            NO_FOUND = WEB_ROOT + getDefault404();
            Config_Json = getConfig();
            Monitor = getMonitor();
            Port = getPort();
            INDEX = getIndexPage();
            PROJECT = getDefaultProject();
            maxElementsInMemory = getMaxElementsInMemory();
            timeToIdleSeconds = getTimeToIdleSeconds();
            timeToLiveSeconds = getTimeToLiveSeconds();
            charset = getCharset();
            setLogLevel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 端口
     * 优先读取配置端口，发生异常默认8888
     *
     * @return
     */
    private static int getPort() {
        try {
            return Integer.parseInt(prop.getProperty("port"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 8888;
    }

    /**
     * 默认首页
     * 优先读取配置，默认index.html
     *
     * @return
     */
    private static String getIndexPage() {
        try {
            return prop.getProperty("index");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index.html";
    }

    /**
     * 默认项目
     * 优先读取配置，默认ROOT
     *
     * @return
     */
    private static String getDefaultProject() {
        try {
            return prop.getProperty("project");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ROOT";
    }

    /**
     * 全局404页面
     * 优先读取配置，默认\template\404.html
     *
     * @return
     */
    private static String getDefault404() {
        try {
            return StringUtil.divideStr(prop.getProperty("404"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtil.divideStr("\\template\\404.html");
    }

    /**
     * 监控日志输出目录
     * 优先读取配置，默认/manage/log.json
     *
     * @return
     */
    private static String getMonitorLog() {
        try {
            return StringUtil.divideStr(prop.getProperty("monitorLog"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtil.divideStr("\\manage\\log.json");
    }

    /**
     * 全局配置文件名称
     * 优先读取配置，默认page.json
     *
     * @return
     */
    private static String getConfig() {
        try {
            return prop.getProperty("config");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "page.json";
    }

    /**
     * 监控刷新频率
     * 写入文件的频率，默认10000ms,10s
     *
     * @return
     */
    private static int getMonitor() {
        try {
            int t = Integer.parseInt(prop.getProperty("monitor"));
            if (t<1000){
                t = 1000;
            }
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 10000;
    }

    /**
     * 缓存：最大存储元素个数
     * 默认10000个
     *
     * @return
     */
    private static int getMaxElementsInMemory() {
        try {
            return Integer.parseInt(prop.getProperty("maxElementsInMemory"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 10000;
    }

    /**
     * 缓存：最大发呆时间(秒/s)
     *
     * @return
     */
    private static int getTimeToIdleSeconds() {
        try {
            return Integer.parseInt(prop.getProperty("timeToIdleSeconds"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 120;
    }

    /**
     * 缓存：最大存活时间(秒/s)
     *
     * @return
     */
    private static int getTimeToLiveSeconds() {
        try {
            return Integer.parseInt(prop.getProperty("timeToLiveSeconds"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 600;
    }
    /**
     * JE文件的默认编码
     *
     * @return
     */
    private static Charset getCharset() {
        try {
            return Charset.forName(prop.getProperty("charset"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Charset.forName("UTF-8");
    }
    /**
     * 日志级别
     * 默认INFO
     * @return
     */
    private static void setLogLevel(){
        String level;
        try {
            level =  prop.getProperty("level");
        } catch (Exception e) {
            e.printStackTrace();
            level = "INFO";
        }
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(Level.valueOf(level));
    }

}
