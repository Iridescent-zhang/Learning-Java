package com.example.interview.singleton;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.singleton
 * @ClassName : .java
 * @createTime : 2025/1/8 12:41
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class EagerSingleton {
    /**
     * final确保 INSTANCE 一旦被初始化，引用不会再被修改。
     * 对于变量：表示该变量只能被赋值一次，其值在初始化后不会再变化。
     * 对于方法：表示该方法不能被重写。
     * 对于类：表示该类不能被继承。
     * static关键字在 Java 中用于修饰成员变量和方法，表示它们属于类本身，而不是类的某个实例，即它是类级别的。
     * 因此它会在类被加载时初始化，而不是在类的实例被创建时初始化。这是实现单例模式的关键，因为它确保在第一次访问类(类加载)时单例实例就已经创建好了。
     *
     * TODO final 关键字规定了变量在初始化之后不能更改。这意味着一旦一个final变量被赋值，它的值就不能再更改了。然而，final 变量的初始化可以是即时的，也可以是延迟的，只不过它们只能被初始化一次。
     * 即时初始化通常发生在编译时，即变量在声明时就被赋值。
     * 在编译时，final 关键字告诉编译器：这个变量在它被初始化后不会再被重新赋值。
     * 在运行时：变量会按照程序的指令第一次被初始化
     * TODO final 表示的是 “初始化后不可变”，而不是 “从出生(声明)到初始化都不可变”。在运行时的初始化过程是变量的第一次赋值，赋值之后变量的引用不能再改变。final 确保在初始化后不能再改变它的引用，而运行时的赋值正是这次唯一的初始化。
     *
     * 总的来说就是：final保证instance在初始化之后不会再变了。而static保证instance属于类，在类被第一次加载的时候instance也才被初始化。所以之后也不会再变了
     */
    private static final EagerSingleton instance = new EagerSingleton();

    private EagerSingleton(){}

    public static EagerSingleton getInstance() {
        return instance;
    }
}
