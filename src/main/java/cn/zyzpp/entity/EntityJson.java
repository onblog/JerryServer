package cn.zyzpp.entity;

import java.util.List;
import java.util.Map;

/**
 * Create by yster@foxmail.com 2018-05-05
**/
public class EntityJson {
	private String page;
	private String id;
	private String method;
	private int timeout;
	private List<EntityInter> inter;
	private Map<String,String> header;
	
	public EntityJson() {
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

    public List<EntityInter> getInter() {
        return inter;
    }

    public void setInter(List<EntityInter> inter) {
        this.inter = inter;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
