package cn.zyzpp.balance.entity;

import cn.zyzpp.page.EntityInter;

/**
 * 对 Inter接口 增强
 * Create by yster@foxmail.com 2018/9/4/004 11:49
 */
public class InterReward {
    private String link;
    private int weight;
    private boolean used;//是否能用
    private int count;//使用次数

    public InterReward() {
    }

    public InterReward(EntityInter entityInter, boolean used, int count) {
        this.link = entityInter.getLink();
        this.weight = entityInter.getWeight();
        this.used = used;
        this.count = count;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
