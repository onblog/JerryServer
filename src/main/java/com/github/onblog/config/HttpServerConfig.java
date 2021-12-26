package com.github.onblog.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.github.onblog.util.PathUtil;
import com.github.onblog.util.StringUtil;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * 配置信息：读取配置文件
 * <br>
 * Create by Martin 2018-05-04
 **/
public class HttpServerConfig {

    public static final String WEB_NAME = "webapps";
    /**
     * WEB_ROOT不是以分隔符结尾的
     */
    public static final String WEB_ROOT = PathUtil.getRootPath(WEB_NAME);

    private static final String CONFIG = PathUtil.getRootPath("config\\config.properties");

    public static String NO_FOUND;

    public static int Monitor;

    public static int Port;

    public static String Config_Json;

    public static String PROJECT;

    public static String INDEX;

    public static String MONITOR_LOG;

    public static Charset js_charset;

    public static Charset fm_charset;

    public static int TimeOut;

    public static int maxElementsInMemory;

    public static int timeToIdleSeconds;

    public static int timeToLiveSeconds;

    public static Charset jk_charset;

    private static Properties prop;

    static {
        try {
            prop = new Properties();
            prop.load(new InputStreamReader(new FileInputStream(CONFIG),"UTF-8"));
//            prop.list(System.out);

            MONITOR_LOG = WEB_ROOT + getMonitorLog();
            NO_FOUND = WEB_ROOT + getDefault404();
            Config_Json = getConfig();
            Monitor = getMonitor();
            TimeOut = getTimeOut();
            Port = getPort();
            INDEX = getIndexPage();
            PROJECT = getDefaultProject();
            maxElementsInMemory = getMaxElementsInMemory();
            timeToIdleSeconds = getTimeToIdleSeconds();
            timeToLiveSeconds = getTimeToLiveSeconds();
            js_charset = getJs_charset();
            fm_charset = getFmCharset();
            jk_charset = getJk_charset();
            setLogLevel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析接口响应的编码
     * @return
     */
    private static Charset getJk_charset() {
        try {
            return Charset.forName(prop.getProperty("jk_charset"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Charset.forName("UTF-8");
    }

    /**
     * 请求接口超时时间
     * @return
     */
    private static int getTimeOut() {
        try {
            return Integer.parseInt(prop.getProperty("timeout"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 10000;
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
     * 接口配置的默认编码
     * @return
     */
    private static Charset getJs_charset() {
        try {
            return Charset.forName(prop.getProperty("js_charset"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Charset.forName("UTF-8");
    }

    /**
     * Freemark的默认编码
     * @return
     */
    private static Charset getFmCharset() {
        try {
            return Charset.forName(prop.getProperty("fm_charset"));
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
