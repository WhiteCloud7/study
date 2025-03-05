# 关于概念：
参考博客：(CSDN)[https://blog.csdn.net/zt15732625878/article/details/79951933?ops_request_misc=%257B%2522request%255Fid%2522%253A%25220c2ca8134146780b45a4d9eb5b57665e%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=0c2ca8134146780b45a4d9eb5b57665e&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-79951933-null-null.142^v101^pc_search_result_base8&utm_term=jsp%2Bservlet&spm=1018.2226.3001.4187]
(更详细，且包含使用)[https://blog.csdn.net/QAZJOU/article/details/135417998?ops_request_misc=%257B%2522request%255Fid%2522%253A%25220c2ca8134146780b45a4d9eb5b57665e%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=0c2ca8134146780b45a4d9eb5b57665e&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduend~default-2-135417998-null-null.142^v101^pc_search_result_base8&utm_term=jsp%2Bservlet&spm=1018.2226.3001.4187]
# servlet使用
参考第二篇博客，重点学习servlet
## 五个servlet的生命周期
1. 初始化：init 方法是 Servlet 生命周期中的初始化方法，它在 Servlet 实例被创建后、处理第一个请求之前被调用，且只会被调用一次。该方法主要用于执行一些初始化操作，例如加载配置文件、建立数据库连接池、初始化资源等。
2. 服务：service 方法是 Servlet 处理客户端请求的核心调度方法。当客户端发送请求到 Servlet 时，容器会调用 service 方法，该方法会根据请求的 HTTP 方法（如 GET、POST、PUT、DELETE 等）来决定调用相应的 doXxx 方法（如 doGet、doPost、doPut、doDelete 等）。
3. doGet(): doGet 方法用于处理客户端发送的 HTTP GET 请求。GET 请求通常用于从服务器获取资源，例如请求网页、图片、数据等。在 doGet 方法中，可以根据请求参数进行相应的处理，并将处理结果返回给客户端。
4. doPost(): doPost 方法用于处理客户端发送的 HTTP POST 请求。POST 请求通常用于向服务器提交数据，例如提交表单、上传文件等。在 doPost 方法中，可以获取客户端提交的数据，并进行相应的处理。
5. 销毁：当Servlet实例不再需要时，它会调用`destroy()`方法进行销毁。在这个方法中，你可以进行一些清理操作，例如关闭数据库连接、释放资源等。
## 几个重要对象
1. ServletConfig: ServletConfig对象包含了Servlet的配置信息，例如Servlet的名称、初始化参数等。  
    常用方法
    - 获取初始化参数
      - String getInitParameter(String name)：根据参数名获取 Servlet 的初始化参数。
      - Enumeration<String> getInitParameterNames()：获取所有 Servlet 初始化参数的名称。
    - 获取 Servlet 上下文
      - ServletContext getServletContext()：获取当前 Servlet 所属的 ServletContext 对象。
2. **ServletContext**（Aplication）: 代表整个 Web 应用程序，在服务器启动时创建，在服务器关闭时销毁。它是一个全局对象，整个 Web 应用中的所有 Servlet 都可以访问同一个 ServletContext 实例，因此可用于在不同的 Servlet 之间共享数据、获取应用程序的初始化参数、访问资源文件等。
    常用方法：
    - 获取初始化参数
      - String getInitParameter(String name)：根据参数名获取 Web 应用的初始化参数。
      - Enumeration<String> getInitParameterNames()：获取所有初始化参数的名称。
    - 获取资源信息
      - String getRealPath(String path)：获取指定资源的真实文件系统路径。
      - URL getResource(String path)：获取指定资源的 URL。
    - **共享数据（或类似session存储，只不过这个的生命周期是整个web程序，而seeion是当前会话）**
      - void setAttribute(String name, Object object)：在应用程序范围内存储一个属性。
      - Object getAttribute(String name)：根据属性名获取应用程序范围内的属性值。
      - void removeAttribute(String name)：从应用程序范围内移除指定的属性。
3. **HttpServletRequest**: HttpServletRequest对象表示了客户端发送的HTTP请求。  
   常用方法：
   - 获取请求参数
     - String getParameter(String name)：根据参数名获取单个参数值。
     - String[] getParameterValues(String name)：根据参数名获取多个参数值（用于复选框等场景）。
     - Map<String, String[]> getParameterMap()：获取所有请求参数的映射。
   - 获取请求头信息
     - String getHeader(String name)：根据请求头名称获取请求头的值。
     - Enumeration<String> getHeaderNames()：获取所有请求头的名称。
   - 获取请求相关信息
     - String getMethod()：获取请求的 HTTP 方法（如 GET、POST 等）。
     - String getRequestURI()：获取请求的 URI。
     - String getContextPath()：获取当前 Web 应用的上下文路径。
   - 获取会话对象
     - HttpSession getSession()：获取当前会话，如果不存在则创建一个新的会话。
     - HttpSession getSession(boolean create)：根据参数决定是否创建新会话。
   - 共享数据
     - void setAttribute(String name, Object object)：在请求范围内存储一个属性。
     - Object getAttribute(String name)：根据属性名获取请求范围内的属性值。
     - void removeAttribute(String name)：从请求范围内移除指定的属性。
     - Object getRequestDispatcher(String path)：获取请求转发对象。
       - ***和response.sendRedirect的区别***：
          - 实现机制
            - getRequestDispatcher().forward：是服务器端的行为，服务器内部将请求转发到另一个资源，客户端并不知道请求被转发到了哪里。
            - sendRedirect：是客户端的行为，服务器通过 HTTP 响应头告诉客户端需要重新发送请求到另一个 URL，客户端会根据收到的响应中的新 URL 再次发起请求。
          - URL 变化
            - getRequestDispatcher().forward：浏览器地址栏中的 URL 不会改变，仍然显示最初请求的 URL。
            - sendRedirect：浏览器地址栏会显示重定向后的 URL。
          - 数据传递
            - getRequestDispatcher().forward：可以使用request.setAttribute方法在转发前设置请求属性，在目标资源中可以通过request.getAttribute获取这些属性，实现数据传递。
            - sendRedirect：默认情况下，重定向后原来请求中的属性会丢失。如果需要传递数据，通常需要将数据放在 URL 参数中或者使用HttpSession等方式。
          - 应用场景
            - getRequestDispatcher().forward：适用于服务器端内部的页面跳转，比如在处理一个请求时，根据不同的条件将请求转发到不同的 JSP 页面进行显示，不希望客户端知道具体的内部处理流程和跳转情况。
            - sendRedirect：常用于需要改变客户端访问的 URL，或者在用户登录成功后将用户重定向到登录后的首页等场景，让用户明确知道页面发生了跳转，并且可以跳转到当前 Web 应用之外的 URL。`
4. **HttpServletResponse**: HttpServletResponse对象表示了服务器对客户端的响应。  
    常用方法：
    - 设置响应状态码
      - void setStatus(int sc)：设置响应的状态码（如 200、404、500 等）。
    - 设置响应头信息
      - void setHeader(String name, String value)：设置响应头的名称和值。
      - void setContentType(String type)：设置响应的内容类型（如 text/html、application/json 等）。
      - void setCharacterEncoding(String charset)：设置响应的字符编码。
    - 输出响应内容  
      - PrintWriter getWriter()：获取一个字符输出流，用于输出文本内容。
      - ServletOutputStream getOutputStream()：获取一个字节输出流，用于输出二进制内容。  
    - 重定向
      - void sendRedirect(String location)：将客户端重定向到指定的 URL。
5. **HttpSession**: HttpSession对象表示了客户端与服务器之间的会话。
    - 常用方法：
      - void setAttribute(String name, Object value)：在会话中存储一个属性。
      - Object getAttribute(String name)：根据属性名获取会话中的属性值。
      - void removeAttribute(String name)：从会话中移除指定的属性。
    - 获取会话信息
      - String getId()：获取会话的唯一标识符。
      - long getCreationTime()：获取会话的创建时间。
      - long getLastAccessedTime()：获取会话的最后访问时间。
    - 管理会话生命周期
      - void invalidate()：使会话无效，销毁会话及其所有属性。
      - void setMaxInactiveInterval(int interval)：设置会话的最大空闲时间（以秒为单位）。。
6. Filter: Filter对象是Servlet的过滤器，它可以用于对请求和响应进行拦截和处理。它可以通过`doFilter()`方法获取。可以写到web.xml中。
7. Listener: Listener对象是Servlet的监听器，它可以用于监听Servlet的生命周期事件。
    常见的监听器：
    - ServletContextListener：监听 ServletContext 的生命周期事件，即 ServletContext 的创建和销毁。常用于在 Web 应用启动时进行初始化操作（如加载配置文件、初始化数据库连接池等），在应用关闭时进行资源释放操作。
    - HttpSessionListener：监听 HttpSession 的生命周期事件，即 HttpSession 的创建和销毁。可用于统计在线用户数量等功能。
    - ServletRequestListener：监听 ServletRequest 的生命周期事件，即 ServletRequest 的创建和销毁。可用于记录请求的开始和结束时间等。
    - ServletContextAttributeListener：监听 ServletContext 中属性的添加、删除和修改事件。
    - HttpSessionAttributeListener：监听 HttpSession 中属性的添加、删除和修改事件。
    - ServletRequestAttributeListener：监听 ServletRequest 中属性的添加、删除和修改事件。