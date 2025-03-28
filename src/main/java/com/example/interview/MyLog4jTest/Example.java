package com.example.interview.MyLog4jTest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.MyLog4jTest
 * @ClassName : .java
 * @createTime : 2025/2/13 14:33
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import org.apache.logging.log4j.ThreadContext;

import java.util.UUID;

public class Example {
    public void someMethod() {
        String traceId = UUID.randomUUID().toString();
        // TODO ThreadContext 是和 SLF4J MDC 类似的工具，用于在日志上下文中存储键值对。可以将 traceId 存储到当前线程的 MDC 中，从而可以在日志中记录和打印这个 traceId。
        ThreadContext.put("traceId", traceId);

        // 进行日志输出或者其他操作...

        ThreadContext.remove("traceId"); // 使用完后移除traceId避免污染其他线程
    }
}
