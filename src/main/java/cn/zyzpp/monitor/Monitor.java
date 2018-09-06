package cn.zyzpp.monitor;

import cn.zyzpp.http.JerryRequest;
import cn.zyzpp.monitor.entity.Item;
import cn.zyzpp.monitor.thread.LogWriteThread;
import cn.zyzpp.monitor.thread.LogThread;

import java.util.Hashtable;
import java.util.Map;

/**
 * 监控系统
 * 运行在另一条线程
 * Create by yster@foxmail.com 2018/9/4/004 20:15
 */
public class Monitor {
    //线程安全，用来记录日志。
    private static final Map<String, Item> map = new Hashtable<>();
    private static boolean updata;//是否更新log
    //日志写入到文件的线程
    private static final LogWriteThread logFileThread = new LogWriteThread();
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
        logThread.start();
        Monitor.setUpdata(true);
        if (!logFileThread.isAlive()){
            logFileThread.setName("writeLogThread");
            logFileThread.setMap(map);
            logFileThread.setDaemon(true);
            logFileThread.start();
        }
    }
}
