package com.example.intesting;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.intesting
 * @ClassName : .java
 * @createTime : 2025/2/15 21:06
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

// TODO 解析和分析 HTTP 报文
/**
 * HTTP 请求报文通常由三部分组成：每部分之间应该也有 \r\n
 * 请求行（Request Line）：请求方法（GET、POST、PUT 等）、请求 URI、HTTP 版本   GET /path/to/resource HTTP/1.1
 * 请求头（Request Headers）：由若干个键值对组成，每个键值对之间用 CRLF（回车换行，\r\n）分隔： Host: example.com
 * 请求体（Request Body，通常只有在 POST 请求或其他带有数据的请求中存在）：可选部分，用于提交数据（如表单数据），通常在 POST 请求中使用。
 */
public class HttpRequestParser {

    public static void main(String[] args) {
        String httpRequest = "GET /path/to/resource HTTP/1.1\r\n" +
                "Host: example.com\r\n" +
                "User-Agent: Mozilla/5.0\r\n" +
                "Accept: text/html\r\n" +
                "\r\n";

        try {
            ParsedHttpRequest parsedRequest = parseHttpRequest(httpRequest);
            System.out.println("Method: " + parsedRequest.method);
            System.out.println("URI: " + parsedRequest.uri);
            System.out.println("HTTP Version: " + parsedRequest.httpVersion);
            System.out.println("Headers: " + parsedRequest.headers);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ParsedHttpRequest parseHttpRequest(String httpRequest) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(httpRequest));
        String line = reader.readLine();
        if (line == null) {
            throw new IOException("Invalid HTTP request");
        }
        // 解析请求行
        String[] requestLineParts = line.split(" ");
        String method = requestLineParts[0];
        String uri = requestLineParts[1];
        String httpVersion = requestLineParts[2];

        // 解析请求头
        Map<String, String> headers = new HashMap<>();
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(": ");
            String headerName = headerParts[0];
            String headerValue = headerParts[1];
            headers.put(headerName, headerValue);
        }

        // 请求体解析省略（如果有需要可自行添加）
        return new ParsedHttpRequest(method, uri, httpVersion, headers);
    }
}

class ParsedHttpRequest {
    String method;
    String uri;
    String httpVersion;
    Map<String, String> headers;
    String requestBody;

    ParsedHttpRequest(String method, String uri, String httpVersion, Map<String, String> headers) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.headers = headers;
    }
}
