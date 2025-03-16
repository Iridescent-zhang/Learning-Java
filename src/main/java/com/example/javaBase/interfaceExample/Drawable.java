package com.example.javaBase.interfaceExample;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.interfaceExample
 * @ClassName : .java
 * @createTime : 2025/2/25 14:42
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * 只包含抽象方法，没有方法体。（Java 8 之后也可以包含默认方法和静态方法）
 * 接口没有构造方法和成员变量（Java 8 之后，可以有常量，也即静态常量）。
 * keypoint 适用于表示完全不同的类之间的共同行为。
 *
 * 接口方法不写修饰符默认就是抽象方法，那么此时不能有方法体。如果要写一个有方法体的默认方法，那一定要写 default 修饰符。
 * keypoint 这个 default 是接口的扩展方法的专用关键字
 */
interface Drawable {
    // 抽象方法
    void draw();

    // keypoint 默认方法，default 一定要写，不然就成抽象方法了。可以被实现类选择性地重写。
    default void resize() {
        System.out.println("Resizing shape...");
    }

    // keypoint 静态方法可以直接通过接口名调用。
    static void description() {
        System.out.println("This is a drawable shape.");
    }

    // keypoint 常量默认都是是 public static final
    String TYPE = "SHAPE";

    int size = 4;
}

class Rectangle implements Drawable {
    int width, height;

    Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a rectangle with width: " + width + " and height: " + height + " size: " + size);
    }

    // 可以选择重写默认方法
    @Override
    public void resize() {
        System.out.println("Custom resizing of rectangle...");
    }

    public static void main(String[] args) {
        Rectangle rectangle = new Rectangle(5, 5);
        rectangle.draw();
        rectangle.resize();
    }
}
