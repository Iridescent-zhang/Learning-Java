package com.example.interview.singleton;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.singleton
 * @ClassName : .java
 * @createTime : 2025/1/8 13:43
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class LazySingleton {
    // 这种方法在需要时才创建实例，并通过双重检查锁定来保证线程安全。
    private static volatile LazySingleton instance;

    private LazySingleton(){}

    public static LazySingleton getInstance() {
        if (instance == null) {
            synchronized (LazySingleton.class) {
                if (instance == null) {
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
    }
}
