package com.example.intesting;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.intesting
 * @ClassName : .java
 * @createTime : 2025/3/10 20:40
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.util.*;

public class LRUCacheWithTTL<K, V> {
    private final int capacity;
    private final long ttl;
    private final Map<K, V> cache;
    private final Map<K, Long> expireTimeMap;

    public LRUCacheWithTTL(int capacity, long ttl) {
        this.capacity = capacity;
        this.ttl = ttl;
        this.cache = new LinkedHashMap<K, V>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > LRUCacheWithTTL.this.capacity;
            }
        };
        this.expireTimeMap = new HashMap<>();
    }

    public synchronized V get(K key) {
        if (!cache.containsKey(key)) {
            return null;
        }
        if (isExpired(key)) {
            cache.remove(key);
            expireTimeMap.remove(key);
            return null;
        }
        return cache.get(key);
    }

    public synchronized void put(K key, V value) {
        long expireTime = System.currentTimeMillis() + ttl;
        cache.put(key, value);
        expireTimeMap.put(key, expireTime);
    }

    private boolean isExpired(K key) {
        Long expireTime = expireTimeMap.get(key);
        if (expireTime == null) {
            return true;
        }
        return System.currentTimeMillis() > expireTime;
    }

    public synchronized void remove(K key) {
        cache.remove(key);
        expireTimeMap.remove(key);
    }

    public synchronized int size() {
        return cache.size();
    }

    public static void main(String[] args) throws InterruptedException {
        LRUCacheWithTTL<String, String> lruCache = new LRUCacheWithTTL<>(3, 2000);
        lruCache.put("a", "apple");
        lruCache.put("b", "banana");
        lruCache.put("c", "cherry");

        System.out.println("Initial cache:");
        System.out.println(lruCache.cache);

        Thread.sleep(1000);
        lruCache.put("d", "date");

        System.out.println("After 1 second, added 'd':");
        System.out.println(lruCache.cache);

        Thread.sleep(2500);

        System.out.println("After another 1.5 seconds:");
        System.out.println("a (apple) should be expired");
        System.out.println(lruCache.cache);

        String value = lruCache.get("a");
        System.out.println("Trying to access 'a', expecting null due to expiration: " + value);

        value = lruCache.get("b");
        System.out.println("Trying to access 'b', should not be expired: " + value);
    }
}
