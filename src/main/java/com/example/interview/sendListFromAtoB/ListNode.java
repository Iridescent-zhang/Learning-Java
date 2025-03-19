package com.example.interview.sendListFromAtoB;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.sendListNode
 * @ClassName : .java
 * @createTime : 2024/12/30 15:44
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.io.Serializable;

class ListNode implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    int value;
    ListNode next;

    ListNode(int value) {
        this.value = value;
        this.next = null;
    }

}
