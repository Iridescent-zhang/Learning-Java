package com.example.interview.javaBase;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase
 * @ClassName : .java
 * @createTime : 2025/2/25 13:15
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * keypoint 访问修饰符基础
 * 访问修饰符 public 用在类上和用在类的成员（如方法和变量）上有着不同的意义和作用。
 * keypoint 当我们在一个类上使用访问修饰符 public 时，它指定了这个类本身的可见性和是否可以被其他包中的类使用，并不能影响类的内部成员的访问级别
 *
 * 一个标记为 public 的类可以在任何包中访问和使用。
 * 如果一个类没有显式的访问修饰符，即 default 访问级别，那么这个类只能在它所在的包内访问。
 *
 * keypoint 类的内部成员（变量和方法）的访问级别由它们自己的修饰符决定，而不是由定义该类的修饰符决定。
 *
 * keypoint Java 语言规范中不允许顶级类（即非嵌套类）声明为 protected 访问级别。顶级类只允许使用 public 和默认包级访问（缺省，即不提供任何修饰符）。
 */
public class AccessModificationExample {
    // 类的成员变量和方法
    // keypoint private 只能在同一个类中访问。
    private int privateVar;
    // keypoint protected 可以在同一个包内访问，并且在不同包中的子类也可以访问。
    protected int protectedVar;
    // keypoint default 只能在同一个包内访问
    int publicVar;

    private void privateMethod() {}
    protected void protectedMethod() {}
    void publicMethod() {}
}
/**
 * keypoint 在 Java 中，当子类覆盖（override）父类的方法时，子类中的方法访问修饰符不能比父类中的更严格。访问修饰符从严格到宽松依次为：
 * private
 * 默认（package-private）
 * protected
 * public
 *
 * 在 Java 中，当子类重写（override）父类的方法时，有几个重要的访问修饰符规则需要遵守：
 * keypoint 1、子类必须可以看到父类中的方法。
 * keypoint 2、子类中的方法访问级别不能比父类中的更严格。
 */

/**
 * keypoint 子类可以定义与父类同名的变量，这称为 “隐藏”（hiding）而不是 “覆盖”（overriding）。这种隐藏并不会影响父类变量，只是在子类中隐藏了父类同名变量。
 * 不过，不推荐这样做，容易导致代码混乱。
 */

