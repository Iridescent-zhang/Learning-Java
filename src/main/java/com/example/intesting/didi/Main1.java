package com.example.intesting.didi;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * https://mp.weixin.qq.com/s/9zFf_CNqOMHscVniDVttGg
 * 形如1212121这种由不同的2个数字交替出现的数称为波浪数，k重波浪数则是指在k种不同的进制下都是波浪数的数。
 *
 * 五个整数(a, b, l, r, k)，其中([a,b])表示应当考虑的进制区间，([l,r])以十进制表示询问的数字区间，k表示波浪数的重数。
 * 数据范围：(2 <= a <= b <= 32)，(1 <= l <= r <= 10000000)，(k 属于 {2,3,4})
 */
public class Main1 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. 读取输入
        int a = sc.nextInt(); // 进制区间的起始
        int b = sc.nextInt(); // 进制区间的结束
        int l = sc.nextInt(); // 数字区间的起始
        int r = sc.nextInt(); // 数字区间的结束
        int k = sc.nextInt(); // 要求的波浪数进制重数

        sc.close();

        // 2. 初始化计数器数组
        // counts[i] 用于记录数字 (l+i) 在多少个进制下是波浪数
        // 这是整个算法的核心数据结构，一个频率映射表
        int lenRange = r - l + 1;
        int[] counts = new int[lenRange];

        // 3. 主循环：遍历每一个待检查的进制 B
        for (int B = a; B <= b; ++B) {

            // 计算在当前进制 B 下，不超过 r 的数的最大位数
            int maxLen = 1;
            long pow = B; // 使用 long 类型避免在计算幂时溢出
            while (pow <= r) {
                maxLen++;
                // 提前判断下一次乘法是否会溢出 long 的范围或超过 r，避免数值错误
                // (long)r / B 是为了保证计算在 long 类型下进行
                if (pow > (long)r / B) {
                    break;
                }
                pow *= B;
            }

            // 使用哈希集合存储当前进制下已确认的波浪数，用于去重
            // 确保在一次进制B的扫描中，每个波浪数只对全局计数贡献一次
            Set<Integer> seenThisBase = new HashSet<>();

            // 4. 生成波浪数
            // A. 处理 1 位波浪数 (特殊规则)
            // 数字 d (1 <= d < B) 在进制 B 下是一位数
            for (int d = 1; d < B; ++d) {
                if (d >= l && d <= r) {
                    seenThisBase.add(d);
                }
            }

            // B. 处理 2 位及以上的波浪数 (模式: x y x y ...)
            // len: 波浪数的位数
            for (int len = 2; len <= maxLen; ++len) {
                // x: 第一个交替数，作为最高位，不能为 0 (1 <= x < B)
                for (int x = 1; x < B; ++x) {
                    // y: 第二个交替数，可以为 0 (0 <= y < B)，但不能与 x 相同
                    for (int y = 0; y < B; ++y) {
                        if (x == y) {
                            continue;
                        }

                        // 按模式从高位到低位构造当前长度的波浪数
                        long val = 0;
                        for (int pos = 0; pos < len; ++pos) {
                            // 偶数位(0, 2, 4...)用 x, 奇数位(1, 3, 5...)用 y
                            int digit = (pos % 2 == 0) ? x : y;
                            val = val * B + digit;

                            // 关键优化：如果构造中的数值已超过 r，则无需继续
                            if (val > r) {
                                break;
                            }
                        }

                        // 检查完整构造的数是否在目标区间 [l, r] 内
                        if (val >= l && val <= r) {
                            // 注意 val 是 long 类型，但题目范围 r <= 1000000，所以可以安全转为 int
                            seenThisBase.add((int) val);
                        }
                    }
                }
            }

            // 5. 更新全局计数
            // 遍历当前进制 B 下找到的所有波浪数，为它们在全局计数器中的计数加一
            for (int v : seenThisBase) {
                counts[v - l]++;
            }
        }

        // 6. 输出结果
        // 遍历计数器数组，找出计数值恰好为 k 的数
        for (int i = 0; i < lenRange; ++i) {
            if (counts[i] == k) {
                // 将数组索引 i 转换回原始数字 l+i
                System.out.println(l + i);
            }
        }
    }
}