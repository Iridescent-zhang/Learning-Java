package com.example.interview.designPattern.factory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.designPattern.factory
 * @ClassName : .java
 * @createTime : 2025/3/1 21:55
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * 工厂模式（Factory Pattern）和工厂方法模式（Factory Method Pattern）是设计模式中至关重要的一部分。key 两者都是通过将对象的创建过程封装起来来提高代码的灵活性和可维护性，但它们的实现方式和应用场景有所不同。
 * 就是(简单)工厂模式，又称为 key 静态工厂方法模式，是通过一个静态方法来创建对象。通过把具体创建对象的逻辑封装在方法内部，客户端通过调用这个方法来获取对象，而不必知道对象的具体类型和创建过程。
 * 看来关键是 key 向外提供创建对象的静态方法
 *
 * 通过静态方法创建对象。
 * 全部对象的创建逻辑集中在一个类中。
 * key 客户端只需知道工厂类和方法参数，但不具备扩展性。（不满足开闭，因为修改肯定得改内部类了）
 */
// 定义一个接口
interface Fruit {
    void eat();
}

// 实现两个具体的水果类
class Apple implements Fruit {
    @Override
    public void eat() {
        System.out.println("吃苹果");
    }
}

class Orange implements Fruit {
    @Override
    public void eat() {
        System.out.println("吃橙子");
    }
}
// key 工厂类
class FruitFactory {
    public static Fruit createFruit(String type) {
        if ("Apple".equalsIgnoreCase(type)) {
            return new Apple();
        } else if ("Orange".equalsIgnoreCase(type)) {
            return new Orange();
        }
        return null;
    }
}

// 使用工厂创建对象
public class FactoryPatternExample {
    public static void main(String[] args) {
        Fruit apple = FruitFactory.createFruit("Apple");
        Fruit orange = FruitFactory.createFruit("Orange");

        apple.eat();
        orange.eat();
    }
}