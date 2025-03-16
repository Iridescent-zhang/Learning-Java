package com.example.javaBase.classLoad;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.classLoad
 * @ClassName : .java
 * @createTime : 2025/2/27 14:45
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * 在类加载最后一步的初始化阶段会进行：
 * 初始化（Initialization）：
 * 进行类的初始化，这包括：
 *      执行类的静态变量的显式初始化。
 *      执行类中的静态初始化块（static block）。
 */
public class MyClass {
    // 静态变量的显式初始化
    public static int myStaticVariable = 42;

    // 静态初始化块
    static {
        System.out.println("Static block executed");
        myStaticVariable = 50;  // 可以重赋值
    }

    public MyClass() {
        System.out.println("Constructor executed");
    }

    public static void main(String[] args) {
        System.out.println("Main method executed");
        System.out.println("Static Variable: " + MyClass.myStaticVariable);

        MyClass obj = new MyClass();  // 执行构造器
    }
}
