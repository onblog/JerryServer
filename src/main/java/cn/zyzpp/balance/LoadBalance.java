package cn.zyzpp.balance;


import cn.zyzpp.balance.entity.InterList;
import cn.zyzpp.balance.entity.InterReward;
import cn.zyzpp.entity.EntityInter;
import cn.zyzpp.entity.EntityJson;
import cn.zyzpp.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负载均衡
 * 主要考虑线程同步问题
 * Create by yster@foxmail.com 2018/9/4/004 9:27
 */
public class LoadBalance {
    // 负载均衡记录表<Id+Page,Table>
    private final static Map<Integer, InterList> map = new HashMap<>();

    synchronized static public void init(EntityJson entity) {
        //判断表中是否存在记录
        if (map.get(accessKey(entity)) == null) {
            List<InterReward> rewardList = new ArrayList<>();
            for (EntityInter e : entity.getInter()) {
                rewardList.add(new InterReward(e, true, 0));
            }
            map.put(accessKey(entity), new InterList(rewardList));
        }
    }

    /**
     * 负载均衡算法
     *
     * @param entity
     * @return
     */
    synchronized static public String loadBalance(EntityJson entity) {
        //是否存在接口
        if (entity.getInter().isEmpty()) {
            throw new CustomException("The interface is not configured. Please check the configuration file");
        }
        //判断是否需要负载均衡
        if (entity.getInter().size() <= 1) {
            return entity.getInter().get(0).getLink();
        }
        //开始负载均衡
        InterList interList = map.get(accessKey(entity));
        for (InterReward inter : interList.getInterRewardList()) {
            if (inter.isUsed() && inter.getCount() < inter.getWeight()) {
                //注意这里的线程安全问题。
                inter.setCount(inter.getCount() + 1);
                return inter.getLink();
            }
        }
        //已满则初始化为零
        for (InterReward inter : interList.getInterRewardList()) {
            inter.setCount(0);
        }
        interList.getInterRewardList().get(0).setCount(1);
        return interList.getInterRewardList().get(0).getLink();
    }

    /**
     * 记录已失效不可用的接口
     * @param en
     * @param url
     * @return
     */
    synchronized static public void interError(EntityJson en, String url) {
        InterList interList = map.get(accessKey(en));
        for (InterReward i : interList.getInterRewardList()) {
            if (i.getLink().equalsIgnoreCase(url)) {
                i.setUsed(false);
            }
        }
    }

    /**
     * 返回该ID中可用的接口数
     *
     * @param en
     * @return
     */
    public static int interUsableNum(EntityJson en) {
        InterList interList = map.get(accessKey(en));
        int num = 0;
        for (InterReward i : interList.getInterRewardList()) {
            if (i.isUsed()) {
                num++;
            }
        }
        return num;
    }

    /**
     * 主键
     * @param entityJson
     * @return
     */
    static private int accessKey(EntityJson entityJson){
        return (entityJson.getPage()).hashCode();
    }

}
