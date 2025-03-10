package com.example.interview.designPattern.chain;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.designPattern.chain
 * @ClassName : .java
 * @createTime : 2025/3/1 11:52
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * 除了责任链之外学点 java 编译器对类和接口的组织规则
 *
 * key 一个文件中写多个类和接口的规则
 * key 任意数量的非 public 类和接口：你可以在一个 Java 源文件中定义任意数量的非 public 类和接口。
 * 最多一个 public 类或接口（接口还可以是 默认修饰符）：如果源文件中包含一个 public 类或 public 接口，源文件的名称必须与这个 public 类或接口的名称相同。
 * key 从底层来看，Java 编译器会将一个源文件（比如 Example.java）中的每个类和接口编译成单独的.class 文件。例如，上述代码会编译成 IHandler.class, HandlerA.class, HandlerB.class, 和 Example.class 四个.class 文件。
 *
 * 并且由于 Example 类没有显式声明为 public（所以文件名不需要强制和类名一致），并且可以包含主方法（main）。
 *
 * Java 的 main 方法需要是 public static 的，这是为了让 Java 虚拟机（JVM）能够在启动应用程序时正确地调用它。main 方法必须遵循如下签名：
 * key public static void main(String[] args)
 * public：确保 JVM 能够访问它。即使类本身不是 public，只要主方法是 public，JVM 就能调用它。
 * static：不需要创建类的实例来调用此方法。
 * void： 包含 main 方法的执行不返回任何值。
 * 参数 String[] args： 用于从命令行接收传递给程序的参数。
 *
 * key 在 Java 中，运行时环境（即 Java 虚拟机）根据启动配置找到一个 class 文件，然后根据文件中的 public static void main(String[] args) 方法入口执行程序。
 * 因为 main 方法是特定的静态入口点，Java 虚拟机无需创建类的实例就能调用它，实现了程序的启动。
 */
interface IHandler {
    void handler();
}

class HandlerA implements IHandler {

    @Override
    public void handler() {
        System.out.println("执行 HandlerA");
    }
}

class HandlerB implements IHandler {

    @Override
    public void handler() {
        System.out.println("执行 HandlerB");
    }
}

class Example {
    public static void main(String[] args) {
        ArrayList<IHandler> iHandlers = new ArrayList<>();
        iHandlers.add(new HandlerA());
        iHandlers.add(new HandlerB());
        iHandlers.forEach(IHandler::handler);
    }
}
