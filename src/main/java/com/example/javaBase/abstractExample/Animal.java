package com.example.javaBase.abstractExample;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase
 * @ClassName : .java
 * @createTime : 2025/2/25 11:51
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import java.io.Serializable;

/**
 * 抽象类 (Abstract Class):
 * 可以包含抽象方法（没有方法体）和非抽象方法（有方法体）。
 * 可以有构造方法和成员变量。
 * keypoint 适用于表示具有共同行为和属性的类的父类(基类)。（相比接口多了个属性）
 */
public abstract class Animal implements Serializable {
    String name;

    protected Animal(String name){
        this.name = name;
    }

    // 抽象方法，没有方法体
    abstract void sound();

    // 非抽象方法，有方法体
    void eat(){
        System.out.println("This animal eats.");
    }
}

class Dog extends Animal {

    Animal animal;
    Dog(String name) {
        super(name);
        this.name = name+name;
    }

    void sound() {
        System.out.println(name + "Bark");
    }

    public static void main(String[] args) {
        Dog dog = new Dog("test");
        dog.sound();
    }
}


