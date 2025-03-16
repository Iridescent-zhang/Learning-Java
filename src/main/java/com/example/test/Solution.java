package com.example.test;

import com.example.interview.sendListFromAtoB.tryAgain.ListNode;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.test
 * @ClassName : .java
 * @createTime : 2025/1/16 16:30
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
class Solution {
    public static void main(String[] args) {

        BigDecimal bigDecimal = new BigDecimal("0.1");
        BigDecimal bigDecimal1 = new BigDecimal("0.2");
        BigDecimal add = bigDecimal1.add(bigDecimal);
        System.out.println("add = " + add);
        BigInteger bigInteger = add.toBigInteger();
        System.out.println("bigInteger = " + bigInteger);
        int i = add.intValue();

        ArrayList<Integer> list = new ArrayList<>();
        int sum = list.stream().mapToInt(Integer::intValue).sum();
        String a = "hello";
        String b = new String("hello").intern();
        boolean b1 = a == b;
        System.out.println("b1 = " + b1);
    }
}

