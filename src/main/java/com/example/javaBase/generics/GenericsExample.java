package com.example.javaBase.generics;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.generics
 * @ClassName : .java
 * @createTime : 2025/3/1 23:15
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * key “? super E” 是 Java 泛型中下界通配符的用法。它表示希望接受 E 类型或 E 的任意父类型。
 *
 * key 下界通配符 ? super E 被用在你想要接收 一个泛型的父类型 的场景中，保证类型安全的同时增加了程序的灵活性。
 * ArrayList.forEach(Consumer<? super E> action) 这个例子中， Consumer<? super E> 可以消费类型为 E 的对象，或任何 E 的父类型的对象。
 *
 * key 方法引用是一种更简洁的 lambda 表达式，它允许用一种简洁的方式实现函数式接口。Consumer 是一个函数式接口@FunctionalInterface，它只有一个抽象方法 void accept(T t)。
 */
public class GenericsExample {
    public static void main(String[] args) {
        ArrayList<Animal> animalArrayList = new ArrayList<>();
        animalArrayList.add(new Dog());
        animalArrayList.add(new Cat());

        /**
         * 定义一个消费者，处理 Animal 类型：
         * key 这句很关键：消费者类型为 Animal（? super Animal 我们选择了Animal），这个消费者可以消费 Animal 对象及它的任一子类对象（如 Dog 和 Cat），因为都可以传递给里面的 void accept(Animal t)方法
         *
         * Animal::feed 是一种方法引用，它指向的是 Animal 类中的 feed 方法，这个 feed 方法没有参数，并返回 void。
         * key 方法引用的匹配
         * Consumer<Animal> 是一个泛型接口，其 accept 方法定义如下：
         * void accept(T t);
         * 在这个例子中，相当于具体的 accept 方法是 void accept(Animal t). key 当你使用方法引用 Animal::feed 时，Java 会自动完成以下匹配步骤：
         * 1、Animal::feed -> feed() 方法：无参，返回 void。
         * 2、当 feed 方法被用作 Consumer<Animal> 类型时，Java 会认为方法引用的目标正是被参数化后的 accept 方法，这会匹配到 Animal::feed 中的无参feed方法。
         * 3、在 forEach 调用过程中：animals.forEach(feed)，每个 Animal 实例被传递给 accept 方法，因此 feed.accept(animal) 实际上就是调用 each animal 对象的 feed() 方法。
         *
         * 真正的情况是：方法引用 Animal::feed 刚好匹配了 Consumer<Animal>::accept 方法的需求，满足了函数式接口的参数类型和返回值要求。
         *
         * key 核心：Animal::feed 是方法引用，它与 Consumer<Animal>::accept 方法契合，因为 feed() 方法符合 void accept(Animal t) 接口的要求，只不过不带参数（参见方法引用的自动匹配）。
         */
        Consumer<Animal> feed = Animal::feed;
        /**
         * key 调用 forEach 方法，并为每一个 Animal 元素执行 feed 方法。这是安全的，因为我们使用的 Consumer 需要满足它的泛型是 Animal 或其父类，那这样的泛型 Consumer 里面的方法是肯定能接收 Animal 的
         */
        animalArrayList.forEach(feed);

        /**
         * key 再来，你看，这样的方法引用 Animal::Rename 就匹配不上 Consumer<Animal>::accept 了
         */
        BiFunction<Animal, String, String> rename = Animal::Rename;
//        animalArrayList.forEach(rename);
    }
}


class Animal {
    public void feed() {
        System.out.println("Animal is being fed");
    }

    public String Rename(String name) {
        return "Re"+name;
    }
}

class Dog extends Animal {
    @Override
    public void feed() {
        System.out.println("Dog is being fed");
    }

    public String Rename(String name) {
        return "Dog"+name;
    }
}

class Cat extends Animal {
    @Override
    public void feed() {
        System.out.println("Cat is being fed");
    }

    public String Rename(String name) {
        return "Cat"+name;
    }
}