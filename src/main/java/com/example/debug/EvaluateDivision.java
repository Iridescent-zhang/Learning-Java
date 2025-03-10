package com.example.debug;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.debug
 * @ClassName : .java
 * @createTime : 2025/1/3 23:08
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class EvaluateDivision{
    public static void main(String[] args) {
        Solution solution = new EvaluateDivision().new Solution();
    }
    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
            HashMap<String, Integer> indexMap = new HashMap<>();
            int idx = 0;
            for (List<String> equation : equations) {
                if (!indexMap.containsKey(equation.get(0))) {
                    indexMap.put(equation.get(0), idx++);
                }
                if (!indexMap.containsKey(equation.get(1))) {
                    indexMap.put(equation.get(1), idx++);
                }
            }
            List<String>[] strAdj = new List[idx];
            List<Double>[] valAdj = new List[idx];
            for (int i = 0; i < idx; i++) {
                strAdj[i] = new ArrayList<>();
                valAdj[i] = new ArrayList<>();
            }
            for (int i = 0; i < equations.size(); i++) {
                String start = equations.get(i).get(0);
                String end = equations.get(i).get(1);
                strAdj[indexMap.get(start)].add(end);
                valAdj[indexMap.get(start)].add(values[i]);

                strAdj[indexMap.get(end)].add(start);
                valAdj[indexMap.get(end)].add(1.0/values[i]);
            }

            ArrayList<Double> ans = new ArrayList<>();
            for (List<String> query : queries) {
                String start = query.get(0);
                String end = query.get(1);
                if (!indexMap.containsKey(start) || !indexMap.containsKey(end)) {
                    ans.add(-1.0);
                    continue;
                }
                if (start.equals(end)) {
                    ans.add(1.0);
                    continue;
                }
                HashSet<String> hset = new HashSet<>();
                hset.add(start);
                ArrayDeque<String> strQue = new ArrayDeque<>();
                strQue.add(start);
                ArrayDeque<Double> valQue = new ArrayDeque<>();
                valQue.add(1.0);
                boolean flag = false;
                while (!flag && !strQue.isEmpty()) {
                    String poll = strQue.poll();
                    Double value = valQue.poll();
                    List<String> list = strAdj[indexMap.get(poll)];
                    for (int i = 0; i < list.size(); i++) {
                        if (!hset.contains(list.get(i))) {
                            if (list.get(i).equals(end)) {
                                ans.add(value * valAdj[indexMap.get(poll)].get(i));
                                flag = true;
                                break;
                            }
                            hset.add(list.get(i));
                            strQue.add(list.get(i));
                            valQue.push(value * valAdj[indexMap.get(poll)].get(i));
                        }
                    }
                }
                if (!flag) {
                    ans.add(-1.0);
                }
            }
            double[] doubles = new double[ans.size()];
            for (int i = 0; i < doubles.length; i++) {
                doubles[i] = ans.get(i);
            }
            return doubles;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}
