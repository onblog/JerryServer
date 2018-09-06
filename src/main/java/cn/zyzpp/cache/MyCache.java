package cn.zyzpp.cache;

import cn.zyzpp.config.HttpServerConfig;
import cn.zyzpp.http.JerryRequest;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;

/**
 * Create by yster@foxmail.com 2018/9/5/005 17:43
 */
public class MyCache {
    public static Cache cache;

    static {
        // 1. 自定义配置
        CacheConfiguration config = new CacheConfiguration("myCache",HttpServerConfig.maxElementsInMemory);
        config.setTimeToIdleSeconds(HttpServerConfig.timeToIdleSeconds);
        config.setTimeToLiveSeconds(HttpServerConfig.timeToLiveSeconds);
        config.setEternal(false);
        config.setOverflowToDisk(true);
        // 配置文件
        Configuration configuration = new Configuration();
        configuration.cache(config);
        configuration.diskStore(new DiskStoreConfiguration().path("java.io.tmpdir"));
        // 2. 创建缓存管理器
        CacheManager cacheManager = CacheManager.create(configuration);
        // 3. 获取缓存对象
        cache = cacheManager.getCache("myCache");
    }

    /**
     * 过滤缓存路径
     * @param request
     * @return
     */
    public static boolean isCache(JerryRequest request){
        if (HttpServerConfig.MONITOR_LOG.equalsIgnoreCase(request.getUri())){
            return false;
        }
        return true;
    }

}
