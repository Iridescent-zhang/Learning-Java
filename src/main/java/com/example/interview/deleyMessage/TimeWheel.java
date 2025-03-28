package com.example.interview.deleyMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.deleyMessage
 * @ClassName : .java
 * @createTime : 2025/3/19 21:48
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * 定时轮算法（时间轮）:
 * 时间轮是一种高效管理定时任务的数据结构，它可以将大范围的时间精度划分到一个环形数组中，通过少量的定时器实现大量定时任务的管理。
 *
 * 时间轮的核心思想是引入一个环形缓冲区 (Ring BufferExample)，简称 "时间轮"，每个槽位 (slot) 对应一个时间窗口。
 * 时间轮总共包含N个槽位，每个槽位对应一段时间，时间轮不断旋转，当时间轮旋转到某个槽位时，会执行这个槽位的所有定时任务。
 *
 * 似乎 mq 内部使用了
 *
 * ---
 * 二级时间轮（层次化时间轮）
 * 一级时间轮管理粗粒度时间，二级时间轮管理细粒度时间，实现多层时间轮嵌套，提高延时精度。
 * 优点：具有较高的性能，且能应对大量定时任务。
 */
class TimeWheel {
    private final long tickDuration; // 时间槽的时长
    private final int wheelSize; // 时间槽的数量
    private final long startTime; // 时间轮的启动时间

    private final List<Set<TimerTask>> wheel; // 存储定时任务的时间槽

    public TimeWheel(long tickDuration, int wheelSize) {
        this.tickDuration = tickDuration;
        this.wheelSize = wheelSize;
        this.startTime = System.currentTimeMillis();
        this.wheel = new ArrayList<>(wheelSize);
        for (int i = 0; i < wheelSize; i++) {
            wheel.add(new HashSet<>());
        }
    }

    public void addTask(TimerTask task, long delay) {
        long executeTime = System.currentTimeMillis() + delay;
        int ticks = (int) ((executeTime - startTime) / tickDuration);
        int slotIndex = ticks % wheelSize;
        wheel.get(slotIndex).add(task);
    }

    public void advanceClock() {
        long currentTime = System.currentTimeMillis();
        int ticks = (int) ((currentTime - startTime) / tickDuration);
        int slotIndex = ticks % wheelSize;
        Set<TimerTask> tasks = wheel.get(slotIndex);
        for (TimerTask task : tasks) {
            if (task.getExecuteTime() <= currentTime) {
                task.run();
                tasks.remove(task);
            }
        }
    }
}

class TimerTask {
    private final long executeTime; // 任务执行时间

    public TimerTask(long executeTime) {
        this.executeTime = executeTime;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public void run() {
        // 执行任务
    }
}
