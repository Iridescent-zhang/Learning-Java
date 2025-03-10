package com.example.interview.designPattern.factory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.designPattern.factory
 * @ClassName : .java
 * @createTime : 2025/3/1 22:00
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * key 抽象工厂方法模式（Factory Method Pattern）
 * key 定义抽象工厂，它们统一有一个创造对象的方法 createFruit()，各个工厂继承抽象类并重写方法得到各个子类工厂
 *
 * 每种具体产品有对应的具体工厂类。
 * key 客户端通过多态性获取对象，增加新产品无需修改现有代码，具备较好的扩展性。
 */
// key 定义一个抽象的工厂
abstract class AbstractFruitFactory {
    public abstract Fruit createFruit();
}

// 实现具体的工厂类
class AppleFactory extends AbstractFruitFactory {
    @Override
    public Fruit createFruit() {
        return new Apple();
    }
}

class OrangeFactory extends AbstractFruitFactory {
    @Override
    public Fruit createFruit() {
        return new Orange();
    }
}

// 使用具体工厂创建对象
public class FactoryMethodPattern {
    public static void main(String[] args) {
        AbstractFruitFactory appleFactory = new AppleFactory();
        AbstractFruitFactory orangeFactory = new OrangeFactory();

        Fruit apple = appleFactory.createFruit();
        Fruit orange = orangeFactory.createFruit();

        apple.eat();
        orange.eat();
    }
}
