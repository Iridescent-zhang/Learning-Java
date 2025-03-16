package com.example.javaBase.abstractExample;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.abstractExample
 * @ClassName : .java
 * @createTime : 2025/2/25 14:39
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
abstract class Shape {
    String color;

    Shape(String color) {
        this.color = color;
    }

    // 抽象方法
    abstract double area();

    // 非抽象方法
    void display() {
        System.out.println("This is a shape with color: " + color);
    }
}

/**
 * keypoint 继承的知识点
 * 当子类继承父类时，子类会获得父类的所有成员变量和方法（但不包括父类的构造方法）。
 * keypoint 但是访问这些成员变量时要考虑它们的访问修饰符：如果成员对子类不可见甚至连方法重写都不可以
 * public 变量：子类可以直接访问。
 * 没有修饰符（包级私有）：子类在同一个包中的时候可以访问。    注意没有修饰符不是说你写 default 也是可以的，它是接口扩展方法的关键字
 * protected 变量：子类也可以直接访问。
 * private 变量：子类不能直接访问，一般是通过父类提供的 public 或 protected 方法来访问。
 *
 * 为什么子类构造方法必须先调用 super()？
 * 每当创建一个子类对象时，它的构造方法会首先调用父类的构造方法。这是为了确保父类的初始状态在子类进行进一步初始化之前已经正确配置。
 * 所有继承的成员变量都能被正确地初始化。这个过程确保了对象构建的完整性和一致性。
 *
 * keypoint 保证对象状态的完整性：通过调用 super()，我们确保父类构造方法中的所有初始化逻辑都被正确执行了。父类可以有复杂的构造逻辑，子类可能需要基于父类的已初始化状态来进行进一步的初始化。
 * 不先对父类进行初始化可能导致 子类的对象 处于一种不完整或错误的状态
 */
class Circle extends Shape {
    double radius;

    Circle(String color, double radius) {
        // 调用父类构造方法，初始化继承自父类的成员变量
        super(color);
        // 初始化子类的成员变量
        this.radius = radius;
    }

    Circle(String color) {
        super(color);
    }

    @Override
    double area() {
        return Math.PI * radius * radius;
    }
}
