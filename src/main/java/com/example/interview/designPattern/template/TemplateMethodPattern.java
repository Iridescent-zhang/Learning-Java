package com.example.interview.designPattern.template;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.designPattern.template
 * @ClassName : .java
 * @createTime : 2025/3/1 22:49
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * 模板方法模式（Template Method Pattern）是一种行为设计模式，key ·它定义一个操作中的算法骨架，并将一些步骤延迟到子类中。模板方法模式使得子类可以在不改变总体算法结构的情况下重新定义算法中的某些步骤。
 *
 * 模板方法模式通常涉及以下角色：
 * 抽象类（AbstractClass）：定义了一个模板方法和一些抽象方法。模板方法定义了算法的骨架，这些抽象方法由子类实现。
 * 具体类（ConcreteClass）：继承自抽象类，实现抽象方法，从而实现模板方法中的步骤。
 */
// key CaffeineBeverage 是一个抽象类，它定义了一个模板方法 prepareRecipe，该方法中包含了制作饮料的步骤。这些步骤包括：烧水、冲泡、倒入杯中、添加调料。
abstract class CaffeineBeverage {

    // key 模板方法，定义了算法的骨架
    final void prepareRecipe() {
        boilWater();
        brew();
        pourInCup();
        if (customerWantsCondiments()) { // 钩子（hook）方法
            addCondiments();
        }
    }

    // 具体方法
    void boilWater() {
        System.out.println("Boiling water");
    }

    // 具体方法
    void pourInCup() {
        System.out.println("Pouring into cup");
    }

    // 抽象方法，由子类实现
    abstract void brew();

    // 抽象方法，由子类实现
    abstract void addCondiments();

    // 钩子方法，可以由子类选择性覆盖
    boolean customerWantsCondiments() {
        return true;
    }
}

// 具体类（比如咖啡类）
class Coffee extends CaffeineBeverage {

    @Override
    void brew() {
        System.out.println("Dripping Coffee through filter");
    }

    @Override
    void addCondiments() {
        System.out.println("Adding Sugar and Milk");
    }

    @Override
    boolean customerWantsCondiments() {
        // 可以根据具体条件返回 true or false，这里简单返回 true
        return true;
    }
}

//具体类（比如茶类）
class Tea extends CaffeineBeverage {

    @Override
    void brew() {
        System.out.println("Steeping the tea");
    }

    @Override
    void addCondiments() {
        System.out.println("Adding Lemon");
    }

    @Override
    boolean customerWantsCondiments() {
        // 可以根据具体条件返回 true or false，这里简单返回 false
        return false;
    }
}

public class TemplateMethodPattern {

    public static void main(String[] args) {
        // key 注意这里父类引用子类对象是怎么执行子类方法的
        CaffeineBeverage tea = new Tea();
        CaffeineBeverage coffee = new Coffee();

        System.out.println("\nMaking tea...");
        tea.prepareRecipe();

        System.out.println("\nMaking coffee...");
        coffee.prepareRecipe();
    }
}
