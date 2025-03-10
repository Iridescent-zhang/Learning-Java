package com.example.interview.protobuf;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.protobuf
 * @ClassName : .java
 * @createTime : 2024/12/20 14:16
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import com.example.interview.protobuf.PersonOuterClass.Person;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProtobufExample {

    public static void main(String[] args) {
        try {
            // 创建一个Person对象
            Person person = Person.newBuilder()
                    .setName("John Doe")
                    .setId(1234)
                    .setEmail("johndoe@example.com")
                    .build();

            // 序列化Person对象到文件
            try (FileOutputStream fos = new FileOutputStream("person.ser")) {
                person.writeTo(fos);
            }

            // 从文件反序列化Person对象
            Person deserializedPerson;
            try (FileInputStream fis = new FileInputStream("person.ser")) {
                deserializedPerson = Person.parseFrom(fis);
            }

            // 输出反序列化的Person对象
            System.out.println("Name: " + deserializedPerson.getName());
            System.out.println("ID: " + deserializedPerson.getId());
            System.out.println("Email: " + deserializedPerson.getEmail());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
