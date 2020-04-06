package cn.zyzpp.monitor.entity;

import java.util.List;
import java.util.Map;

/**
 * Create by yster@foxmail.com 2018/9/4/004 20:35
 */
public class Result {
    private int status;//200
    private String message;
    private int total;
    private Map<String,List<Item>> rows;

    public Result() {
    }

    public Result(int status, String message, int total, Map<String, List<Item>> rows) {
        this.status = status;
        this.message = message;
        this.total = total;
        this.rows = rows;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Map<String, List<Item>> getRows() {
        return rows;
    }

    public void setRows(Map<String, List<Item>> rows) {
        this.rows = rows;
    }
}
