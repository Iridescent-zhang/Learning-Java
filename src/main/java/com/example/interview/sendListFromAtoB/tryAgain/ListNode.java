package com.example.interview.sendListFromAtoB.tryAgain;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.sendListFromAtoB.tryAgain
 * @ClassName : .java
 * @createTime : 2024/12/30 16:18
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class ListNode implements Serializable {
    private static final long serialVersionUID = 1L;

    int val;
    ListNode next;
    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
