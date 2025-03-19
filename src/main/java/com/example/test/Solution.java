package com.example.test;

import com.example.interview.sendListFromAtoB.tryAgain.ListNode;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.atomic.LongAdder;
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
    public static void main(String[] args) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        int[] nums = new int[]{-2,1,-3,4,-1,2,1,-5,4};
        int ans = nums[0];
        int left = 0, right = 0;
        int sum = nums[0];
        int leftTmp = 0, rightTmp = 0;
        for(int i=1; i<nums.length; i++) {
            if(sum<0) {
                sum = 0;
                leftTmp = i;
            }
            sum += nums[i];
            rightTmp = i;
            if(sum > ans) {
                left = leftTmp;
                right = rightTmp;
                ans = sum;
            }
        }
        System.out.println("ans = " + ans);
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=left; i<right+1; i++) {
            list.add(nums[i]);
        }
        System.out.println("list = " + list);

        Socket socket = new Socket("", 5555);

        File file = new File("");
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        FileChannel fc = raf.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[10]);
        fc.position(0);
        fc.write(byteBuffer);
    }
}

