package com.example.javaBase.classLoad;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.classLoad
 * @ClassName : .java
 * @createTime : 2025/2/26 20:39
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * 类加载过程;
 * 加载（Loading）：
 *      虚拟机根据类的全限定名找到字节码（.class 文件），并加载到方法区中，生成类对象。
 * 验证（Verification）：
 *      确保加载的类的字节码符合 Java 语言规范，没有安全问题。
 * 准备（Preparation）：
 *      key 为类的静态变量分配内存，并将其初始化为默认值。例如，int 类型的静态变量初始值为 0。(注意：所谓类加载类加载，那自然只能加载和类强相关的，这里就是为类静态变量分配内存，然后初始化为默认值)
 * 解析（Resolution）：
 *      将类、接口、字段和方法符号引用转化为直接引用。
 * 初始化（Initialization）：
 *      key 进行类的初始化，这包括：（这里就很关键了，和小林说的不太一样，是进行真正意义上的我们自定义的类的初始化，包括静态变量显式初始化、执行静态代码块）
 *      key 执行类的静态变量的显式初始化。
 *      key 执行类中的静态初始化块（static block）。
 */

/**
 * key 当父类和子类有 静态代码块、静态方法、静态变量等等的加载过程
 * key 静态代码块是在类加载时执行的代码块，并且在类加载时只执行一次。（静态代码块是加载用来执行的）
 * 静态方法 和静态变量是在类加载时与类一起加载的，可以通过类名直接调用。只会加载一次。（静态方法不会在类加载时自动执行，它们需要通过类名调用。）
 *
 * 执行顺序： key 子类只有它的静态块能执行的稍微早点
 * key 类加载过程：
 * 首先会加载父类，然后再加载子类。
 * 在加载父类时，会执行父类的静态代码块（如果有多个静态代码块，按代码顺序执行）。
 * 加载子类，执行子类的静态代码块（同样按代码顺序执行）。
 * key 实例化过程：
 * key 执行父类的实例初始化块（如果有），然后执行父类的构造函数。
 * 执行子类的实例初始化块（如果有），然后执行子类的构造函数。
 *
 */
class Parent {
    static {
        System.out.println("父类静态代码块");
    }

    {
        System.out.println("父类实例初始化块");
    }

    public Parent() {
        System.out.println("父类构造函数");
    }

    public static void parentStaticMethod() {
        System.out.println("父类静态方法");
    }

    public void instanceMethod(){
        System.out.println("父类实例方法");
    }
}

class Child extends Parent {
    static {
        System.out.println("子类静态代码块");
    }

    {
        System.out.println("子类实例初始化块");
    }

    public Child() {
        System.out.println("子类构造函数");
    }

    public static void childStaticMethod() {
        System.out.println("子类静态方法");
    }

    public void instanceMethod(){
        System.out.println("子类实例方法");
    }
}

public class ClassLoad {
    /**
     * 1、执行 main 方法时，首先加载 Child 类。由于 Child 类依赖于 Parent 类，先加载 Parent 类，从而执行 Parent 类的静态代码块。接着加载 Child 类，并执行 Child 类的静态代码块。（注意这些静态的与类相关的只会加载一次）
     * 2、key 创建 Child 对象时，在调用 Parent 构造函数之前，执行 Parent 实例初始化块。
     * 3、 接着执行 Child 类的实例初始化块。最后执行 Child 类的构造函数。
     */
    public static void main(String[] args) {
        System.out.println("main方法开始");
        Child child = new Child();
        System.out.println("main方法结束");
        System.out.println("------------------------------");

        /**
         * key 在 Java 中，子类不能重写父类的静态方法。重写是对象多态的一部分，而静态方法是类级别的方法，不属于任何对象，因此多态不适用于静态方法。
         * 不过，子类可以定义一个与父类静态方法同名的新静态方法，这叫做方法隐藏（method hiding）。虽然方法名称和签名相同，但它们属于不同的类，因此不会被认为是重写。
         * key 尽管 parent 实际上指向的是一个 Child 对象，但因为静态方法与实例无关，是类级别的，所以调用时完全基于引用变量的的类的类型（即 Parent），不会发生多态，因此输出 "父类静态方法"。
         * key 调用静态方法时，依据的是引用变量的类型，而非对象的实际类型。
         *
         * 首先，在 Java 中，静态方法是可以通过类名和对象来调用的，这是允许的语法。但从设计和实际应用来看，推荐使用类名来调用静态方法，以便显式地表明静态方法的类归属。
         * 从底层来看：
         * 静态方法是类的方法，每个类在方法区中都有唯一的入口（Class对象），当类加载到内存后，静态方法和类的其他元数据会存储在方法区中。
         * parent 是一个 Parent 类型的引用，指向一个 Child 类型的对象。
         * key 方法调用解析：当通过 parent.staticMethod() 调用静态方法时，编译器根据 parent 引用的静态类型（即 Parent）去查找 Parent 类的 staticMethod 方法的地址，而不会查看实际对象的类型。所以，最后实际执行的是 Parent 类的 staticMethod。
         *
         * key 在 Java 中，引用变量有两个类型：静态类型和实际类型。
         * 静态类型：又称为编译时类型，是在编译时确定的，是声明变量时所使用的类型。例如，Parent parent 中的 Parent 就是 parent 变量的静态类型。
         * 实际类型：又称为运行时类型，是在程序运行时确定的，是变量实际引用的对象的类型。例如，在 Parent parent = new Child(); 中，parent 变量的实际类型是 Child。
         * 编译时：编译器根据 parent 变量的静态类型（Parent），生成对 Parent 类方法的调用字节码。
         * 运行时：
         */
        Parent parent = new Child();
        parent.parentStaticMethod();   // key 注意：尽管静态方法是类级别上的方法，仍然可以通过对象引用来调用。不过，这种方式通常不被推荐，因为它可能会引起混淆。静态方法的实质和内存管理使这种现象变得更加复杂。
        System.out.println("------------------------------");

        /**
         * key 这里父类引用子类对象调用实例方法，使用的是子类重写的方法！这里注意是实例方法，像上面使用静态方法就根据的是引用变量的类型了
         * 这是因为 Java 中的实例方法调用是基于动态绑定（Dynamic Binding）的。
         *
         * 对象找到方法主要依靠虚方法表（vtable 或 Virtual Method Table，简称 VMT），这是 Java 实现 key 多态性和动态绑定的核心机制之一。
         * 当类加载器加载一个类时，JVM 会在方法区为这个类生成相应的类信息，包括方法表。在方法表中，记录了每个方法的实现地址。
         * 当一个对象被创建时（例如通过 new 操作），堆内存中为这个对象分配内存空间，并为这个对象分配一个指向方法表的指针。
         * key 如果子类重写了某些方法，方法表中记录的对应父类方法地址会指向子类的实现。
         *
         * 虚方法表的概念仍然适用于当前的 JVM。每个类包含一个虚方法表，类实例包含一个指向对应虚方法表的指针。
         * key 动态绑定依赖于运行时找到适当的方法实现，这通常需要类似虚方法表的机制。静态绑定应该是要使用的方法实现在编译的时候就确定了
         */
        Parent child1 = new Child();
        child1.instanceMethod();

    }
}
