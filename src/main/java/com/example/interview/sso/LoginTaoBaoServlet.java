package com.example.interview.sso;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Date;
import io.jsonwebtoken.Claims;

import javax.servlet.FilterChain;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.sso
 * @ClassName : .java
 * @createTime : 2025/3/30 18:05
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * key  先登录淘宝，单点登录天猫
 *
 * 1. 登录流程
 *   用户访问 taobao.com 的登录页面并输入凭证。
 *   login.taobao.com 验证用户凭证，如果通过则创建 JWT 令牌。
 *   key  将 JWT 令牌作为响应的一部分返回给浏览器，浏览器将其存储（通常存储在 localStorage 或 sessionStorage 中）。

 ---------------------------
 // 假设这是登录请求的响应处理代码
 fetch('https://login.taobao.com/login', {
     method: 'POST',
     headers: {
         'Content-Type': 'application/json'
     },
     body: JSON.stringify({ username: 'user', password: 'password' })
 })
 .then(response => response.json())
 .then(data => {
     // 存储JWT令牌
     window.localStorage.setItem('jwtToken', data.token);
 })
 .catch(error => console.error('Error:', error));
 ---------------------------

 *
 * 2. 访问天猫
 *   用户访问 tmall.com。
 *   浏览器通过发送 JWT 令牌作为请求头的一部分来请求 tmall.com 的受保护资源。
 * 3. 将 JWT 令牌附加到请求
 *   以下是如何在客户端 （例如：JavaScript 中）将 JWT 令牌附加到请求头部去请求 天猫服务器

---------------------------
 // 假设我们已有JWT令牌，在向其他域（例如天猫）发起请求时，读取并使用存储的 JWT 令牌。
 const jwtToken = window.localStorage.getItem('jwtToken');

 fetch('https://tmall.com/some-protected-resource', {
     method: 'GET',
     headers: {
         'Authorization': 'Bearer ' + jwtToken
     }
 })
 .then(response => response.json())
 .then(data => console.log(data))
 .catch(error => console.error('Error:', error));
 ---------------------------

 *
 * 4. 解析 JWT 令牌
 * 在天猫的服务器端，验证和解析 JWT 令牌
 *
 * --------------------------
 * 总结
 * 登录淘宝：用户通过提供凭证登录淘宝 (login.taobao.com) 后，服务器验证成功后生成 JWT 令牌并返回给客户端，同时设置 CORS 头允许跨域请求。
 * 跨域请求与令牌存储：客户端将收到的 JWT 令牌存储在本地（localStorage 或 sessionStorage），以便在访问其他受保护资源如天猫时使用。
 * 访问天猫：当用户访问天猫 (tmall.com)，客户端通过将 JWT 令牌附加在请求头（Authorization）中发送至天猫服务器。
 * 验证 JWT 令牌：天猫服务器解析并验证 JWT 令牌，以确认用户的身份，从而实现单点登录效果。
 *
 * --------------------------
 * 补充：
 * 1. 'Bearer ' 前缀的意义
 * 'Authorization': 'Bearer ' + jwtToken
 * HTTP 授权头部中的Bearer关键字表示令牌认证 (Token Authentication) 类型。它告诉服务器，客户端使用了 “持票人令牌（Bearer Token）” 来进行认证。
 *      Bearer 后面紧跟的就是我们的 JWT 令牌。服务器知道它需要从Authorization头部中提取并验证令牌。
 * key  'Bearer '：是 OAuth 2.0 标准中用来标识 Token 类型的字符串前缀，服务器通过它知道客户端附加了 JWT 令牌。
 *
 * 2. 为什么浏览器在向天猫服务器发请求时带上 JWT?
 * 浏览器不知道自动带上 JWT，这需要在你的前端代码中手动添加。例如，当你的用户在淘宝登录后，你会在前端保存这个 JWT 令牌（通常是 localStorage 或 sessionStorage）。
 *      在用户访问天猫时，你的代码需要主动读取并将 JWT 添加到请求头中。
 * key  浏览器带上 JWT：需要在客户端代码中明确实现，通过在请求头中附加 Authorization。
 *
 * 3. CORS 设置的作用
 * CORS (跨域资源共享) 的设置主要是为了允许浏览器发起跨域请求而不会被浏览器拒绝。CORS 不会自动将 JWT 带到跨域请求中；你需要在前端代码中明确地将 JWT 附加到 HTTP 请求头中。
 *      CORS 的作用是允许浏览器将这个请求发送出去，并接受来自跨域的响应。
 * key  CORS 作用：仅允许浏览器进行跨域请求，并不是带上 JWT；代码中的 Access-Control-Allow-Origin 配置的是允许哪些源可以访问资源。
 *
 *
 */

/**
 * key  突然发现连什么是跨域都不知道，真牛
 * 跨域请求 (Cross-Origin Request) 是指从一个域名的页面向不同域名的服务器发送的 HTTP 请求。这种请求在现代 Web 应用中非常常见。
 * 比如，当你在一个网页（如 http://example.com）上通过 AJAX 请求数据，而这个数据由另一个域（如 http://api.example.com）提供，就产生了跨域请求。
 *
 * 浏览器有同源策略 (Same-Origin Policy)，这是出于安全原因设计的，key  限制一个源（域名、协议、端口）上的文档或脚本如何与另一个源上的资源进行交互。同源策略防止恶意网站读取另一个网站的数据。
 * 同源策略的具体限制如下：
 * 协议：必须相同 (如 http 和 https 被视作不同源)。
 * 域名：必须完全相同 (如 sub.example.com 与 example.com 视作不同源)。
 * 端口：必须相同 (如 80 和 8080 被视作不同源)。
 *
 * CORS (跨源资源共享) 的作用
 * CORS 是一种机制，它允许服务器告诉浏览器允许来自不同源的请求。这是通过设置 HTTP 头来实现的。如果服务器没有适当的 CORS 配置，那么浏览器会拦截并阻止跨域请求。
 * 【淘宝返回的响应设置了允许从这个页面 跨源通过 POST 访问天猫页面】
 *
 * ---------------------
 * 区分两种情况：
 * 1. 在浏览器的新标签页中直接访问
 * 你在浏览器的新标签页中直接访问 http://tmall.com/api/resource，这是一个直接访问的行为
 * 浏览器动作：浏览器发起普通的 HTTP 请求。
 * 这种行为不涉及同源策略的限制，因为这是用户直接发起的请求，key  而非通过 JavaScript 代码在一个域中访问另一个域的数据。
 *
 * 2. 在 JavaScript 代码中进行跨域请求
 * 跨域请求通常发生在你使用 JavaScript 来通过 AJAX 或其他手段从一个域请求另外一个域的数据时。这种情况下会涉及跨域问题，也就是前面提到的同源策略限制。
 * 例如，在 http://taobao.com 网站的 JS 代码中使用 fetch 或 XMLHttpRequest 去请求 http://tmall.com/api/resource，这就会被认为是跨域请求。
 *
 * 跨域请求主要用于以下场景：
 * AJAX 请求：在一个网页中通过 JavaScript 动态请求其他域的数据。
 * 微服务架构：前端和后端分离，前端和多个后端服务进行通信。
 * 第三方 API 调用：例如从你的网站调用另一个网站上的 API。
 *
 * 示例流程
 * 假设你已经在 taobao.com 登录成功，并且获取到了一个 JWT 令牌。你希望在不重新登录的情况下访问 tmall.com 的受保护资源，在这种情况下，你可以通过 JavaScript 代码来实现。
 * 登录淘宝：用户在 taobao.com 登录，服务器返回 JWT 令牌。
 * 存储 JWT 令牌：浏览器存储这个 JWT 令牌，比如使用 localStorage。
 * 跨域请求：在访问 tmall.com 时，使用 JavaScript 代码带上 JWT 令牌进行请求。
 *
 *
 * key key key key key key key key key key key key key key key key key key key key key  这里应该有问题，设置跨域的应该是 天猫？改天再看
 * 【CORS 头设置：由目标域（tmall.com）的服务器设置，以允许来自源域（taobao.com）的跨域请求。】
 *
 * 为什么是目标域设置 CORS
 * CORS 配置总是由目标服务器管理，原因是：
 * 安全考虑：目标服务器能够控制哪些域允许跨域请求。
 * 防止恶意伪造：只有目标服务器才能正确判断并设置允许的跨域来源。
 *
 * 目标服务器设置 CORS：在这个例子中，天猫 (tmall.com) 的服务器需要设置允许跨域请求的 CORS 头，以允许来自淘宝 (taobao.com) 的请求。
 *
 * 总结
 * 什么是跨域请求：当网页（如 taobao.com）试图请求另一个源的数据（如 tmall.com）时，就形成了跨域请求。由于浏览器的同源策略，这些请求会被默认拒绝，key  除非目标服务器明确允许。
 * CORS 的作用：key  目标服务器可以设置 CORS 头，告诉浏览器允许来自指定源的请求，例子中的代码示例展示了如何设置这些头。
 * 浏览器如何知道带上 JWT：浏览器不会自动带上 JWT。这需要在前端代码中手动添加，在上面的 fetch 示例中明确地将 JWT 附加到请求头中。
 */

// key  淘宝登录服务器
@WebServlet("/login")
public class LoginTaoBaoServlet extends HttpServlet {
    private static final String SECRET_KEY = "secret-key";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 假设认证通过
        if ("user".equals(username) && "password".equals(password)) {
            // key  生成JWT令牌：JWT 设置用户名、有效期、签名算法
            String jwtToken = Jwts.builder()
                    .setSubject(username)
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1小时有效期
                    .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                    .compact();

            // 设置CORS头
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");

            // 返回 JWT 令牌到客户端进行保存
            response.setContentType("application/json");
            response.getWriter().write("{\"token\": \"" + jwtToken + "\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

// key  天猫的服务器端，验证和解析 JWT 令牌
class JwtAuthenticationFilter implements Filter {
    private static final String SECRET_KEY = "secret-key";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(jwtToken)
                        .getBody();

                httpRequest.setAttribute("claims", claims);
            } catch (Exception e) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
