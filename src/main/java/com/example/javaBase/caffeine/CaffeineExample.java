package com.example.javaBase.caffeine;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.javaBase.caffeine
 * @ClassName : .java
 * @createTime : 2025/4/5 22:58
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

/**
 * 关于Count-Min Sketch算法，可以看作是布隆过滤器的同源的算法，假如我们用一个hashmap来存储每个元素的访问次数，那这个量级是比较大的，并且hash冲突的时候需要做一定处理，否则数据会产生很大的误差，Count-Min Sketch算法将一个hash操作，扩增为多个hash，这样原来hash冲突的概率就降低了几个等级，且当多个hash取得数据的时候，取最低值，也就是Count Min的含义所在。
 *
 * 下图展示了Count-Min Sketch算法简单的工作原理：
 *
 * 假设有四个hash函数，每当元素被访问时，将进行次数加1；
 * 此时会按照约定好的四个hash函数进行hash计算找到对应的位置，相应的位置进行+1操作；
 * 当获取元素的频率时，同样根据hash计算找到4个索引位置；
 * 取得四个位置的频率信息，然后根据Count Min取得最低值作为本次元素的频率值返回，即Min(Count)
 *
 * 填充策略（Population）
 * 填充策略是指如何在 键 不存在的情况下，如何创建一个对象进行返回，主要分为下面四种
 *
 *
 */
public class CaffeineExample {
    public static void main(String[] args) {
        Cache<String, String> cache = Caffeine.newBuilder()
                // 5秒没有读写自动删除
                .expireAfterAccess(5, TimeUnit.SECONDS)
                // 最大容量1024个，超过会自动清理空间
                .maximumSize(1024)
                .removalListener(((key, value, cause) -> {
                    //清理通知key,value ==> 键值对   cause ==> 清理原因
                    System.out.println("remove entry: " + key + " " + value);
                    System.out.println("cause = " + cause);
                }))
                .build();

//        //添加值
//        cache.put("张三", "浙江");
//        //获取值
//        String ifPresent = cache.getIfPresent("张三");
//        System.out.println("ifPresent = " + ifPresent);
//        //remove
//        cache.invalidate("张三");


        /**
         * 手动填充 (Manual)
         * cache.getIfPresent ： 一个入参
         * cache.get ： 两个入参，一个为 lambda，当key不存在时，可以创建出对象来返回，返回值不会为空
         */
        String age2 = cache.get("张三", k -> {
            System.out.println("k:" + k);
            return "18";
        });
        String age3 = cache.getIfPresent("张三");
        System.out.println(age2);
        System.out.println("age3 = " + age3);
    }
}
