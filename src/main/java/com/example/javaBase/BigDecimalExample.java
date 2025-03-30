package com.example.javaBase;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase
 * @ClassName : .java
 * @createTime : 2025/2/25 11:55
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * key  使用 BigDecimal 进行精确计算的原因在于 BigDecimal 类可以准确地表示和操作任意精度的有理数
 * 原因：
 * BigDecimal保证精度的解决思路其实极其的简单朴素，还是用一句话来解释：十进制整数在转化成二进制数时不会有精度问题，那么把十进制小数扩大 N 倍让它在整数(BigInteger)的维度上进行计算，并保留相应的精度信息(scale)。
 *
 * BigDecimal 使用 BigInteger 来存储任意精度的整数部分。BigInteger 本身是一个表示任意精度整数的类，因此可以处理非常大的数字，而不会像原生数据类型那样受限于有限的位数。
 * 通过 scale 来表示小数部分的位数，从而确保在计算过程中精确管理小数部分。例如在进行加减乘除和其他数学运算时都考虑了 scale 的精度。
 *

 //值的绝对long型表示
 private final transient long intCompact;

 //值的小数点后的位数
 private final int scale;

 private final BigInteger intVal;

 //值的有效位数，不包含正负符号
 private transient int precision;

 // 包含stringCache，因此创建BigDecimal对象时，优先转换成String类型，比如double转BigDecimal也是先double转成String,再String转成BigDecimal.
 private transient String stringCache;

 //加、减、乘、除、绝对值
 public BigDecimal add(BigDecimal augend) {}
 public BigDecimal subtract(BigDecimal subtrahend) {}
 public BigDecimal multiply(BigDecimal multiplicand) {}
 public BigDecimal divide(BigDecimal divisor) {}
 public BigDecimal abs() {}

 num2 = new BigDecimal("0.0002685")
 intCompact = 2685     //值的绝对long型表示
 scale = 7             //值的小数点后的位数
 precision = 4         //值的有效位数，不包含正负符号，就是 intCompact 的位数

 BigDecimal能更精确表示带小数点的数值，因为采用了 long intCompact 和 int scale 结合来表示数值，而不是浮点型的科学计数法。
 */
public class BigDecimalExample {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        BigDecimal num1 = new BigDecimal("0.1");
        BigDecimal num2 = new BigDecimal("0.0002685");


        BigDecimal sum = num1.add(num2);
        System.out.println("sum = " + sum);

        BigDecimal diff = num1.subtract(num2);
        System.out.println("diff = " + diff);

        BigDecimal product = num1.multiply(num2);
        System.out.println("product = " + product);

        BigDecimal quotient = num1.divide(num2, 4, RoundingMode.HALF_UP);
        System.out.println("quotient 商 = " + quotient);

        BigDecimal rounded = num1.setScale(2, RoundingMode.HALF_UP);
        System.out.println("rounded = " + rounded);

        /**
         * jdk 8 之后不能这样用反射了
         */
//        // 获取BigDecimal类的Class对象
//        Class<? extends BigDecimal> aClass = num2.getClass();
//
//        // 获取私有字段scale
//        Field scaleField = aClass.getDeclaredField("scale");
//        scaleField.setAccessible(true);  // 设置私有字段可访问
//        int scaleValue = scaleField.getInt(num2);  // 获取字段值
//
//        System.out.println("scale = " + scaleValue);
    }
}
