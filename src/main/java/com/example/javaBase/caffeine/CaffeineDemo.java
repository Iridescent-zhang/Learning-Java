package com.example.javaBase.caffeine;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.javaBase.caffeine
 * @ClassName : .java
 * @createTime : 2025/4/14 23:02
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import com.github.benmanes.caffeine.cache.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.TimeUnit;

public class CaffeineDemo {
    static System.Logger logger = System.getLogger(CaffeineDemo.class.getName());

    public static void hello(String[] args) {
        System.out.println("args = " + args);
    }


    public static void main(String... args) throws Exception {
        Cache<String, String> cache =  Caffeine.newBuilder()
                //最大个数限制
                //最大容量1024个，超过会自动清理空间
                .maximumSize(1024)
                //初始化容量
                .initialCapacity(1)
                //访问后过期（包括读和写）
                //5秒没有读写自动删除
                .expireAfterAccess(5, TimeUnit.SECONDS)
                //写后过期
                .expireAfterWrite(2, TimeUnit.HOURS)
                //写后自动异步刷新
                .refreshAfterWrite(1, TimeUnit.HOURS)
                //记录下缓存的一些统计数据，例如命中率等
                .recordStats()
                .removalListener(((key, value, cause) -> {
                    //清理通知 key,value ==> 键值对   cause ==> 清理原因
                    System.out.println("removed key="+ key);
                }))
                //使用CacheLoader创建一个LoadingCache
                .build(new CacheLoader<String, String>() {
                    //同步加载数据
                    @Nullable
                    @Override
                    public String load(@NonNull String key) throws Exception {
                        System.out.println("loading  key="+ key);
                        return "value_" + key;
                    }

                    //异步加载数据
                    @Nullable
                    @Override
                    public String reload(@NonNull String key, @NonNull String oldValue) throws Exception {
                        System.out.println("reloading  key="+ key);
                        return "value_" + key;
                    }
                });

        //添加值
        cache.put("name", "疯狂创客圈");
        cache.put("key", "一个高并发 研究社群");

        //获取值
        @Nullable String value = cache.getIfPresent("name");
        System.out.println("value = " + value);
        //remove
        cache.invalidate("name");
        value = cache.getIfPresent("name");
        System.out.println("value = " + value);
    }

}


