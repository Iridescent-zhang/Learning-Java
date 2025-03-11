package com.example.interview.javaBase.classLoad;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.classLoad
 * @ClassName : .java
 * @createTime : 2025/3/11 16:32
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * 在 Java 中，类加载器（ClassLoader）负责加载类。默认情况下，类加载器会遵循双亲委派模型，即先委托父类加载器加载类，如果父类加载器无法加载，才由自己加载。
 * 如果要加载同一个类两次，可以通过自定义类加载器，并绕过双亲委派模型来实现。
 */
public class CustomClassLoader extends ClassLoader {
    public CustomClassLoader() {
        super(null); // 不指定父加载器
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 从指定路径加载类的字节码
        byte[] classData = loadClassData(name);
        if (classData == null) {
            throw new ClassNotFoundException(name);
        }
        return defineClass(name, classData, 0, classData.length);
    }

    private byte[] loadClassData(String name) {
        // 实现加载类的字节码逻辑
        // 例如从文件或网络中读取
        return null; // 实际实现需要返回字节数组
    }

    public static void main(String[] args) throws Exception {
        CustomClassLoader loader1 = new CustomClassLoader();
        CustomClassLoader loader2 = new CustomClassLoader();

        // 使用不同的类加载器加载同一个类
        Class<?> class1 = loader1.loadClass("com/example/interview/javaBase/classLoad/MyClass.java");
        Class<?> class2 = loader2.loadClass("com/example/interview/javaBase/classLoad/MyClass.java");

        System.out.println("Class 1: " + class1);
        System.out.println("Class 2: " + class2);
        System.out.println("Are the classes the same? " + (class1 == class2)); // 输出 false
    }
}
