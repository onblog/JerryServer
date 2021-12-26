package com.github.onblog.monitor.thread;

import com.github.onblog.config.HttpServerConfig;
import com.github.onblog.monitor.Monitor;
import com.github.onblog.monitor.entity.Item;
import com.github.onblog.monitor.entity.Result;
import com.github.onblog.util.IOUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by Martin 2018/9/5/005 14:54
 */
public class LogWriteThread extends Thread {
    private Logger logger = LoggerFactory.getLogger(LogWriteThread.class);
    private Map<String, Item> map;

    public void setMap(Map<String, Item> map) {
        this.map = map;
    }

    @Override
    public void run() {
        while (true) {
            if (Monitor.isUpdata()) {
                writeLog();
                Monitor.setUpdata(false);
            }
            try {
                Thread.sleep(HttpServerConfig.Monitor);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 更新日志文件
     */
    private void writeLog() {
        Map<String,List<Item>> rows = new HashMap<>();
        List<Item> items = new ArrayList<>();
        for (String s : map.keySet()) {
            items.add(map.get(s));
        }
        rows.put("item",items);
        Result result = new Result(200,"",items.size(),rows);
        String json = JSON.toJSONString(result);
        IOUtil.write(json,HttpServerConfig.MONITOR_LOG);
        logger.debug("Successfully updated monitoring log:"+HttpServerConfig.MONITOR_LOG);
    }
}
