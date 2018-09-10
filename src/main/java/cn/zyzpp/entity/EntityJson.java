package cn.zyzpp.entity;

import java.util.List;

/**
 * Create by yster@foxmail.com 2018-05-05
**/
public class EntityJson {
	private String page;
	private String id;
	private List<EntityInter> inter;
	
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

}
