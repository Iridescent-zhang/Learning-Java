package com.example.interview.singleton;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.singleton
 * @ClassName : .java
 * @createTime : 2025/1/8 13:45
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public enum EnumSingleton {
    // 使用枚举来实现单例模式不仅可以防止反射攻击，还能防止反序列化破坏单例。

    INSTANCE;
}
