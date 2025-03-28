package com.example.interview.MyLog4jTest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.MyLog4jTest
 * @ClassName : .java
 * @createTime : 2025/2/13 13:51
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * SLF4J（Simple Logging Facade for Java） 不是一个日志的具体实现，它只是一个日志的抽象层，提供了一套通用的接口。
 * 底层可以集成多种具体的日志框架（例如 Logback, Log4j, java.util.logging (JUL) 等等）。
 * 使用 SLF4J 可以让你在代码中使用统一的日志接口，而无需关注具体的日志实现。
 * TODO SLF4J 的一个重要优势是可以在运行时动态替换底层日志实现。
 *
 * Log4j 的优势：
 *   灵活的配置方式，可以通过 XML、JSON、YAML 等多种格式进行定制日志输出。
 *   多种日志级别（如 DEBUG、INFO、WARN、ERROR 等）和多种日志输出方式（如文件、控制台、远程服务器等）。
 *   Log4j 2 相较于 Log4j 1 进行了大量性能优化，采用了异步日志记录机制，显著提升了日志记录的效率。（Log4j 1 版本已经被弃用，且曾有过安全漏洞问题，因此在选择 Log4j 时需要注意使用最新的 Log4j 2 版本。）
 *   Log4j 2 采用了插件化架构，所有的组件（如 Appender、Layout 等）都可以通过插件的形式动态加载和使用。
 *
 * Logback
 *   由 Log4j 的创始人设计，是一个功能强大且性能优越的日志框架。Logback 是 SLF4J 的原生实现，拥有更佳的性能和丰富的功能，可以直接与 SLF4J 配合使用。
 *
 * java.util.logging (JUL)
 *   是 JDK 自带的日志框架，具有轻量级和无依赖的优势。它适用于对日志功能要求不高的项目。
 */
public class LoggerExample {
    // 使用 LogManager.getLogger 方法来获取一个名为 LoggerExample.class 全限定名的日志记录器，便于在日志输出时识别日志来源。
    private static final Logger logger = LogManager.getLogger(LoggerExample.class);

    public void run() {
        logger.debug("This is a debug message");
        logger.info("This is an info message");
        logger.warn("This is a warn message");
        logger.error("This is an error message");

        try {
            throw new Exception("An example exception");
        } catch (Exception e) {
            logger.error("Caught an exception", e);
        }
    }
}
