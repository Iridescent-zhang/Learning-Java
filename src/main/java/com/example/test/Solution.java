package com.example.test;

import com.example.interview.sendListFromAtoB.tryAgain.ListNode;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
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
    public static int maxSumDivThree(int[] nums) {
        int ans = 0;
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        for(int n : nums) {
            if(n%3==0) {
                ans += n;
            }else if(n%3==1) {
                list1.add(n);
            }else {
                list2.add(n);
            }
        }
        Collections.sort(list1, (o1,o2)-> o2-o1);
        Collections.sort(list2, (o1,o2)->o2-o1);
        int len1 = list1.size();
        int len2 = list2.size();
        int idx1 = 0, idx2 = 0;
        while(len1>=6 && len2>=6) {
            len1 -= 3;
            len2 -= 3;
            for(int k=0; k<3; k++) {
                ans += (list1.get(idx1++) + list2.get(idx2++));
            }
        }

        int[][] dp = new int[len1+1][len2+1];
        int max = 0;
        for(int i=0; i<=len1; i++) {
            for(int j=0; j<=len2; j++) {
                if(i>=3) {
                    int tmp = 0;
                    for(int k=0; k<3; k++) {
                        tmp += list1.get(idx1+i-1-k);
                    }
                    dp[i][j] = Math.max(dp[i][j], tmp+dp[i-3][j]);
                }
                if(j>=3) {
                    int tmp = 0;
                    for(int k=0; k<3; k++) {
                        tmp += list2.get(idx2+j-1-k);
                    }
                    dp[i][j] = Math.max(dp[i][j], tmp+dp[i][j-3]);
                }
                if(i>=1 && j>=1)
                    dp[i][j] = Math.max(dp[i][j], list1.get(idx1+i-1) + list2.get(idx2+j-1) +dp[i-1][j-1]);
                max = Math.max(max, dp[i][j]);
            }
        }
        class runnable implements Runnable{
            @Override
            public void run() {

            }
        }
        runnable runnable = new runnable();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        return ans+max;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{2,19,6,16,5,10,7,4,11,6};
        Solution.maxSumDivThree(nums);
        System.out.println((-4%3));

        Thread thread = new Thread(()->{
            System.out.println("哈哈哈哈");
        });
        thread.start();
    }
}


class ProcessExample {
    static int a;
    public static int b;
    public static void main(String[] args) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        // 设置要执行的命令
        processBuilder.command("sh", "-c", "echo Hello, World!");

        try {
            // 启动子进程
            Process process = processBuilder.start();

            // 等待子进程执行完成，并获取其返回状态
            int exitCode = process.waitFor();
            System.out.println("Exit codecodecodecode: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String s = null;
        try {
            char c = s.charAt(0);
            System.out.println(c);
        }catch (Exception e) {
        }
        System.out.println("jj");

        List<String> list = Arrays.asList("Apple", "Orange", "Banana", "Pear");

        // 使用lambda表达式在比较器中
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
        list.sort(comparator);

        System.out.println(list);

        // 使用方法引用在比较器中
        Comparator<String> comparatorMethodRef = String::compareTo;
        list.sort(comparatorMethodRef);

        StringBuilder sb = new StringBuilder();
        System.out.println(list);


        Callable<Integer> callable = new Callable<>() {
            @Override
            public Integer call() throws Exception {
                return null;
            }
        };
        FutureTask futureTask = new FutureTask(callable);
        Thread thread = new Thread(futureTask);

    }

}

 class TestUserHome {
    public static void main(String[] args) throws Exception {
        String userHome = System.getProperty("user.home");
        System.out.println("User Home Directory: " + userHome);
        Optional<String> optional = Optional.of("Hello, World!");
        Optional<String> userHome1 = Optional.ofNullable(userHome);
        userHome1.ifPresent(
                value -> System.out.println("value = " + value)
        );
        String s = userHome1.get();
        System.out.println("s = " + s);
        String s1 = userHome1.orElse("2");
        userHome1.orElseGet(()->"sjdw");
        userHome1.orElseThrow(()->new IllegalArgumentException("sjdk"));
        Optional<Integer> i = userHome1.map(String::length);

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
            }
        };

        // 匿名内部类（Anonymous Inner Class），在 Java 中用于基于某个类创建类的实例，并同时重写该类的方法。由于 LinkedHashMap 是一个普通类而不是抽象类或接口，我们可以直接使用匿名内部类来重写它的方法。
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>(10, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                return super.removeEldestEntry(eldest);
            }
        };


        class test extends LinkedHashMap{
            public test(int a, float b, boolean c){
                super(a, b, c);
            }
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return super.removeEldestEntry(eldest);
            }
        }

        test test = new test(10, 0.75f, true);

        Executor executor1 = new Executor() {
            @Override
            public void execute(Runnable command) {

            }
        };


        class Exam{
            int haha = 3;
        }

        Exam exam = new Exam();

        Executor executor = new Executor() {
            @Override
            public void execute(Runnable command) {

            }
        };

        executor.execute(()->{
            System.out.println("executor = " + executor);
        });

    }
}

class ReflectExample {

    ReflectExample(){}

    // Example class with a private field
    static class Person {

        private String name;

        public Person(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        ReflectExample reflectExample = new ReflectExample();
        // Create an instance of Person
        Person person = new Person("小超");

        // Get the Class object
        Class<?> personClass = person.getClass();

        // Get the Field object for the "name" field
        Field nameField = personClass.getDeclaredField("name");

        // Set the field accessible if it's private
        nameField.setAccessible(true);

        // Get the value of the "name" field for the specific instance
        String nameValue = (String) nameField.get(person);
        System.out.println("Name: " + nameValue);

        // Modify the value of the "name" field for the specific instance
        nameField.set(person, "新的名字");

        // Get the updated value of the "name" field for the specific instance
        String updatedNameValue = (String) nameField.get(person);
        System.out.println("Updated Name: " + updatedNameValue);

        Class<Person> personClass1 = Person.class;
        Field name = personClass1.getDeclaredField("name");
        name.setAccessible(true);
        String o = (String)name.get(person);
        System.out.println("o = " + o);

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    }
}





