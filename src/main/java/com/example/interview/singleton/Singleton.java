package com.example.interview.singleton;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.singleton
 * @ClassName : .java
 * @createTime : 2025/1/8 12:51
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class Singleton {
    private Singleton(){}

    private static class SingletonHolder {
        // 静态内部类在 Singleton 类被加载时不会立即实例化，直到调用 getInstance 方法时才实例化
        private static final Singleton instance = new Singleton();
    }

    public static Singleton getInstance() {
        return SingletonHolder.instance;
    }
}
