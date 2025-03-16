package com.example.javaBase.copy;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.copy
 * @ClassName : .java
 * @createTime : 2025/3/5 23:00
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * key 浅拷贝：创建一个新对象，这个对象有一个指向原对象中子对象的引用。Java 中浅拷贝可以通过实现Cloneable接口并覆盖clone方法来实现。
 * key 重写clone()方法与实现Cloneable接口在 Java 中进行对象拷贝时是两个关键步骤
 *
 * clone()这个方法也不是抽象的吧为什么要重写clone方法？
 * 虽然 clone() 方法定义在 Object 类中，key 并且不是抽象方法，但是为了实现浅拷贝，我们需要在我们的类中重写它。
 * 默认的 clone() 方法是 key 受保护 protected 的，如果你不重写，你不能直接在你的类的实例上调用它。
 * 可见性：Object 类中的 clone() 方法是 protected，我们需要将它重写为 public（如果需要在外部调用）或保持 protected 使其在包或子类中都可见。
 * 功能定制：即使我们只是调用 super.clone()，它也能创建一个浅拷贝。重写允许我们对克隆过程进行自定义，比如进行深拷贝时，可以在重写方法中拷贝非基本类型的字段。
 *
 * 为什么要实现接口Cloneable？
 * key Cloneable 接口虽然没有定义任何方法（是个标记接口），但它在 Java 对象克隆机制中起着非常重要的作用。
 * java.lang.Object 类中的 clone() 方法检查该对象是否实现了 Cloneable 接口。如果没有实现接口，它会抛出 CloneNotSupportedException 异常。
 * key 标记接口作用：Cloneable 接口作为标记接口，表明该类是可以安全地使用 clone() 方法的。
 * 确保功能性：如果没有实现 Cloneable 接口，调用 clone() 方法时将抛出 CloneNotSupportedException 异常。key 这是一个安全机制，确保开发者明确表明其意图该类可以被克隆。
 *
 * 通过重写 clone() 方法，我们能够调用 super.clone() key 来利用 Object 类所提供的默认浅拷贝实现，同时也可以增加我们自己的克隆逻辑。
 */
public class shallowCopy implements Cloneable{
    int a;
    int[] b;

    public shallowCopy(int a, int[] b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

class shallowCopyMain {
    public static void main(String[] args) throws CloneNotSupportedException {
        int[] arr = {1, 2, 3};
        shallowCopy example1 = new shallowCopy(10, arr);
        shallowCopy example2 = (shallowCopy) example1.clone();

        System.out.println(example1.b == example2.b); // true
    }
}
