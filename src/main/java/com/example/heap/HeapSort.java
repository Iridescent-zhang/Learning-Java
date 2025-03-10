package com.example.heap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.heap
 * @ClassName : .java
 * @createTime : 2024/12/25 13:46
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class HeapSort {
    // 堆排序方法
    public static void heapSort(int[] arr) {
        int n = arr.length;

        // 构建堆（重新排列数组）
        for (int i = n / 2 - 1; i >= 0; i--) {  //这里0也要，让最大值浮出水面
            heapify(arr, n, i);
        }

        // 依次从堆中提取元素
        for (int i = n - 1; i > 0; i--) {
            // 将当前根节点移动到末尾
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            // 在堆中调整
            heapify(arr, i, 0);
        }
    }

    // 通过索引i对数组arr的前n个元素进行堆调整
    private static void heapify(int[] arr, int n, int i) {
        int largest = i; // 初始化最大值索引
        int left = 2 * i + 1; // 左孩子节点
        int right = 2 * i + 2; // 右孩子节点

        // 如果左孩子大于根节点
        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }

        // 如果右孩子大于当前最大值
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        // 如果最大值不是根节点
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // 递归调整受影响的子树
            heapify(arr, n, largest);
        }
    }
}
