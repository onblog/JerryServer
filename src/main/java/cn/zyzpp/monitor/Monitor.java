package cn.zyzpp.monitor;

import cn.zyzpp.http.JerryRequest;
import cn.zyzpp.monitor.entity.Item;
import cn.zyzpp.monitor.thread.LogThread;
import cn.zyzpp.monitor.thread.LogWriteThread;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 监控系统
 * 运行在另一条线程
 * Create by yster@foxmail.com 2018/9/4/004 20:15
 */
public class Monitor {
    //线程安全，用来记录日志。
    private static final Map<String, Item> map = new Hashtable<>();
    //线程池
    private static ExecutorService executor = Executors.newCachedThreadPool();
    //是否更新log
    private static boolean updata;
    //日志写入到文件的线程
    private static LogWriteThread logFileThread = new LogWriteThread();
    //更新日志的线程
    private LogThread logThread = new LogThread();

    public static boolean isUpdata() {
        return updata;
    }

    public static void setUpdata(boolean updata) {
        Monitor.updata = updata;
    }

    public void openMonitor(JerryRequest request, long time) {
        logThread.prepare(request, time, map);
        executor.execute(logThread);
        Monitor.setUpdata(true);
        if (!logFileThread.isAlive()){
            logFileThread.setName("writeLogThread");
            logFileThread.setMap(map);
            logFileThread.setDaemon(true);
            logFileThread.start();
        }
    }
}
