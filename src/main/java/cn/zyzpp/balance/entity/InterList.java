package cn.zyzpp.balance.entity;

import java.util.List;

/**
 * 一个ID一张表
 * Create by yster@foxmail.com 2018/9/4/004 11:49
 */
public class InterList {
    private List<InterReward> interRewardList;

    public InterList() {
    }

    public InterList(List<InterReward> interRewardList) {
        this.interRewardList = interRewardList;
    }

    public List<InterReward> getInterRewardList() {
        return interRewardList;
    }

    public void setInterRewardList(List<InterReward> interRewardList) {
        this.interRewardList = interRewardList;
    }

}
