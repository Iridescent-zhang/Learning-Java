package com.example.javaBase.copy;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.copy
 * @ClassName : .java
 * @createTime : 2025/3/5 23:11
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * key 眼拙了，cloned.b = this.b.clone() 这样的写法其实已经达到了嵌套
 * key 深拷贝：创建一个新对象，同时递归地复制原对象所有可达对象(b)所引用的对象。可以通过显式编码达成深度拷贝。
 */
public class deepCopy implements Cloneable {
    int a;
    int[] b;

    public deepCopy(int a, int[] b) {
        this.a = a;
        this.b = b;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        deepCopy cloned = (deepCopy) super.clone();
        cloned.b = cloned.b.clone();   // key 这里其实就是递归拷贝了
        return cloned;
    }
}

class deepCopyMain {
    public static void main(String[] args) throws CloneNotSupportedException {
        int[] arr = {1, 2, 3};
        deepCopy example1 = new deepCopy(10, arr);
        deepCopy example2 = (deepCopy) example1.clone();

        System.out.println(example1.b == example2.b); // false
    }
}