package com.example.javaBase;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.javaBase
 * @ClassName : .java
 * @createTime : 2025/3/29 23:11
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import java.util.TreeMap;
import java.util.TreeSet;

/**
 * key  TreeSet 这种都是自带去重的，如果比较器返回 0 是不会插入的，所以如果你允许插入相同值【也就是重写比较器】，那你对 headSet 等方法的查询结果也有很大的影响
 * key  理清的关键是比如：相同值我们 return 1 表示大于，排在相等值的后面，那么一个 headSet() 的入参也会排在相等值的后面，然后再考虑 "严格小于"，注意这里的引号的意思，这两个相等值实际上"不相等"
 * key  就比如 treeSet.subSet(2, 5)，虽然里面已经有 2 和 5 了，但是不是我们入参的 2 和 5 实际上不和里面的 2 和 5 相等，则等效为 treeSet.subSet(2.1, 5.1)
 * TreeSet 的常用方法介绍：
 * TreeSet是一个实现了 NavigableSet 接口[继承 SortedSet]的类，可以用来维护一个排序的集合。
 * key  默认递增  那就是：headSet < 当前入参值 <= tailSet，其实就是 [fromElement, toElement)
 * headSet(E toElement): 返回一个所有元素严格小于 toElement 的 NavigableSet。
 * tailSet(E fromElement): 返回一个所有元素大于或等于 fromElement 的 NavigableSet。
 * subSet(E fromElement, E toElement): 返回一个元素范围从 fromElement（包含）到 toElement（不包含）的 NavigableSet。
 *
 * key  默认递增  那就是：floor <= 当前入参值 <= ceiling  【这两个的组合都是 带等于的】
 * ceiling(E e): 返回集合中 大于或等于 e 的最小元素，如果不存在这样的元素，则返回null。
 * floor(E e): 返回集合中   小于或等于 e 的最大元素，如果不存在这样的元素，则返回null。
 *
 * key  默认递增  那就是：lower < 当前入参值 < higher     【这两个的组合都 不带等于】
 * higher(E e): 返回集合中严格大于 e 的最小元素，如果不存在这样的元素，则返回null。
 * lower(E e): 返回集合中严格小于 e 的最大元素，如果不存在这样的元素，则返回null。
 */
public class TreeSetAndMapExample{}
class TreeSetExample {
    public static void main(String[] args) {
        TreeSet<Integer> treeSet = new TreeSet<>((o1, o2)->{
            if(o1 != o2) return o1-o2;
            else return 1;
        });

        treeSet.add(1);
        treeSet.add(2);
        treeSet.add(2);
        treeSet.add(2);
        treeSet.add(3);
        treeSet.add(3);
        treeSet.add(4);
        treeSet.add(5);

        System.out.println("Original TreeSet: " + treeSet);

        // headSet example
        TreeSet<Integer> headSet = (TreeSet<Integer>) treeSet.headSet(3);
        System.out.println("HeadSet (< 3): " + headSet);

        // tailSet example
        TreeSet<Integer> tailSet = (TreeSet<Integer>) treeSet.tailSet(3);
        System.out.println("TailSet (>= 3): " + tailSet);

        // subSet example
        TreeSet<Integer> subSet = (TreeSet<Integer>) treeSet.subSet(2, 5);
        System.out.println("SubSet (2 to 5): " + subSet);

        // ceiling example
        System.out.println("Ceiling (2): " + treeSet.ceiling(2));

        // floor example
        System.out.println("Floor (2): " + treeSet.floor(2));

        // higher example
        System.out.println("Higher (3): " + treeSet.higher(3));

        // lower example
        System.out.println("Lower (3): " + treeSet.lower(3));
    }
}

/**
 * TreeMap是一个实现了 NavigableMap 接口的类，可以用来维护一个排序的键值对映射。
 * TreeMap 的常用方法介绍：  key  和 TreeSet 是一样的, 其实就是 [fromKey, toKey)
 *
 * headMap(K toKey): 返回一个所有键严格小于 toKey 的映射集。
 * tailMap(K fromKey): 返回一个所有键大于或等于 fromKey 的映射集。
 * subMap(K fromKey, K toKey): 返回一个键范围从 fromKey（包含）到 toKey（不包含）的映射集。
 *
 * ceilingKey(K key): 返回映射中大于或等于 key 的最小键，如果不存在这样的键，则返回null。
 * floorKey(K key): 返回映射中小于或等于 key 的最大键，如果不存在这样的键，则返回null。
 *
 * higherKey(K key): 返回映射中严格大于 key 的最小键，如果不存在这样的键，则返回null。
 * lowerKey(K key): 返回映射中严格小于 key 的最大键，如果不存在这样的键，则返回null。
 */
class TreeMapExample {
    public static void main(String[] args) {
        TreeMap<Integer, String> treeMap = new TreeMap<>();

        treeMap.put(1, "one");
        treeMap.put(2, "two");
        treeMap.put(3, "three");
        treeMap.put(4, "four");
        treeMap.put(5, "five");

        System.out.println("Original TreeMap: " + treeMap);

        // headMap example
        TreeMap<Integer, String> headMap = new TreeMap<>(treeMap.headMap(3));
        System.out.println("HeadMap (< 3): " + headMap);

        // tailMap example
        TreeMap<Integer, String> tailMap = new TreeMap<>(treeMap.tailMap(3));
        System.out.println("TailMap (>= 3): " + tailMap);

        // subMap example
        TreeMap<Integer, String> subMap = new TreeMap<>(treeMap.subMap(2, 5));
        System.out.println("SubMap (2 to 5): " + subMap);

        // ceilingKey example
        System.out.println("CeilingKey (2): " + treeMap.ceilingKey(2));

        // floorKey example
        System.out.println("FloorKey (2): " + treeMap.floorKey(2));

        // higherKey example
        System.out.println("HigherKey (3): " + treeMap.higherKey(3));

        // lowerKey example
        System.out.println("LowerKey (3): " + treeMap.lowerKey(3));
    }
}

/**
 * key  TreeSet和TreeMap是 Java 中用于维护有序集合和有序映射的类，基于红黑树实现。它们提供了如下方法：
 * headSet / headMap：返回所有小于指定元素 / 键的集合 / 映射。
 * tailSet / tailMap：返回所有大于或等于指定元素 / 键的集合 / 映射。
 * subSet / subMap：返回指定范围内的集合 / 映射。
 * ceiling / ceilingKey：返回大于或等于指定元素 / 键的最小元素 / 键。
 * floor / floorKey：返回小于或等于指定元素 / 键的最大元素 / 键。
 * higher / higherKey：返回严格大于指定元素 / 键的最小元素 / 键。
 * lower / lowerKey：返回严格小于指定元素 / 键的最大元素 / 键。
 */
