package com.github.onblog.page;

/**
 * 对于web项目的page.json文件
 * Create by Martin 2018/9/4/004 9:56
 */
public class EntityInter {
    private String link;
    private int weight;

    public EntityInter() {
    }

    public EntityInter(String link, int weight) {
        this.link = link;
        this.weight = weight;
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
