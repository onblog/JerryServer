package com.github.onblog.monitor.thread;

import com.github.onblog.http.JerryRequest;
import com.github.onblog.monitor.entity.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 进行日志记录的线程
 * Create by Martin 2018/9/4/004 21:02
 */
public class LogThread extends Thread {
    private Logger logger = LoggerFactory.getLogger(LogThread.class);
    private Map<String,Item> map;
    private JerryRequest request;
    private long time ;

    public void prepare(JerryRequest request, long time, Map<String,Item> map) {
        this.request = request;
        this.time = time;
        this.map = map;
    }

    @Override
    public void run() {
        Item item = new Item();
        item.setProject(request.getProject());
        item.setContentType(request.getContentType());
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        item.setData(format.format(new Date()));
        item.setHeaders(request.headers().toString());
        item.setMethod(request.getMethod().toString());
        item.setTime(time);
        item.setUrl(request.getFilePath());
        item.setProtocolVersion(request.protocolVersion().toString());
        item.setUri(request.getUri());
        map.put(request.getUri(),item);
    }
}
