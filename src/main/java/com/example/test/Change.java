package com.example.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.test
 * @ClassName : .java
 * @createTime : 2025/4/11 13:51
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class Change {

    int encode(String ipv4) {
        String[] split = ipv4.split("\\.");
        int code = 0;
        for(String s : split) {
            code <<= 8;
            code |= Integer.parseInt(s);
        }
        return code;
    }

    String decode(int ipv4){
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>();
        list.add(String.valueOf((ipv4 & (255<<24))>>>24));
        list.add(String.valueOf((ipv4 & (255<<16))>>>16));
        list.add(String.valueOf((ipv4 & (255<<8))>>8));
        list.add(String.valueOf(ipv4 & 255));
        return String.join(".", list);
    }

    public static void main(String[] args) {
        Change change = new Change();
        String ret = change.decode(change.encode("10.0.0.9"));
        System.out.println("ret = " + ret);
    }
}
