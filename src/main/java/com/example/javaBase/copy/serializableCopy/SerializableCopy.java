package com.example.javaBase.copy.serializableCopy;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.javaBase.copy.serializableCopy
 * @ClassName : .java
 * @createTime : 2025/3/16 20:30
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class SerializableCopy {
    public static void main(String[] args) {
        MyClass myClass = new MyClass();
        MyClass copu = myClass.deepCopy();
    }
}

class MyClass implements Serializable {
    private String filed1;
    private NestedClass nestedObject;

    public MyClass deepCopy(){
        try {
            FileOutputStream fos = new FileOutputStream("object.ss");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.flush();
            oos.close();

            FileInputStream fis = new FileInputStream("object.ss");
            ObjectInputStream ois = new ObjectInputStream(fis);
            MyClass myClass = (MyClass)ois.readObject();
            return myClass;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

class NestedClass implements Serializable {
    private int nestedField;
}
