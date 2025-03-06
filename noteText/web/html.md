参考博客：(html)[https://blog.csdn.net/zong596568821xp/article/details/83277729]
# html网页基本结构:
```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>ZONGXP</title>
</head>
<body>
    <h1>我的第一个标题</h1>
    <p>我的第一个段落。</p>
</body>
</html>
```
- `<!DOCTYPE html>` 声明为 HTML5 文档
- `<html>`元素是 HTML 页面的根元素
  - `<head>` 元素包含了文档的元（meta）数据，如 <meta charset="utf-8"> 定义网页编码格式为 utf-8（由于在大部分浏览器中直接输出中文会出现乱码，所以要在头部将字符声明为UTF-8）
    - `<title>` 元素描述了文档的标题，即浏览器上显示的标题
  - `<body>` 元素包含了可见的页面内容
    - `<h1>` 元素定义一个大标题
    - `<p>` 元素定义一个段落
## 常用标签：
- meta：
  - `<meta charset="utf-8">` 定义网页编码格式为 utf-8
  - `<meta name="viewport" content="width=device-width, initial-scale=1.0">` 定义视口，用于控制页面在不同设备上的显示方式
    - minimum-scale：  
     含义：设置页面允许的最小缩放比例。例如，minimum-scale=0.5 表示页面最小只能缩小到原始大小的 50%。
    - maximum-scale：  
    含义：设置页面允许的最大缩放比例。例如，maximum-scale=2.0 表示页面最大只能放大到原始大小的 200%。
    - user-scalable：  
    含义：设置用户是否可以手动缩放页面。取值为 yes 或 no，user-scalable=no 表示禁止用户手动缩放页面。
  - `<meta name="description" content="这是一个描述">` 定义页面的描述，用于搜索引擎优化
  - `<meta http-equiv="refresh" content="5;url=https://www.example.com">` 定义页面自动刷新，5秒后跳转到指定页面
  - `<meta http-equiv="X-UA-Compatible" content="IE=edge">` 定义页面使用的浏览器模式，IE=edge 表示使用最新的浏览器模式
  - `<meta name="author" content="张三">` `<meta name="copyright" content="版权所有 &copy; 2024 示例公司">` 定义页面的作者和版权信息
- 标题：HTML 标题（Heading）是通过`<h1> - <h6>`标签来定义的
- 段落：HTML 段落（Paragraph）是通过<p> 标签来定义的
- 文档分块（除了以上网页基本结构的进一步分块，其中段落和标题可以包含在以下每个标签里面）：
  - 页眉：`<header>` 于定义文档、页面或章节的头部区域，通常包含网站的标题、标志、导航菜单、搜索框等内容。一个页面可以有多个 `<header>` 元素，例如每个文章或章节都可以有自己的头部。
  - 导航栏：`<nav>` 用于定义导航链接的集合，通常包含指向其他页面或当前页面内其他部分的链接。常见的导航菜单、面包屑导航等都可以使用`<nav>` 标签，如：
  ```html
  <nav>
    <ul>
      <li><a href="#">首页</a></li>
      <li><a href="#">关于我们</a></li>
      <li><a href="#">联系我们</a></li>
    </ul>
  </nav>
  ```
  - 主体：`<main>` 一个页面只能有一个main
  - 侧边栏：`<aside>` 标签用于定义与页面主要内容间接相关的部分，通常显示为侧边栏、侧注、广告等。一个页面可以有多个 `<aside>` 元素。
  - 页脚：`<footer>` 标签用于定义文档、页面或章节的底部区域，通常包含版权信息、联系方式、相关链接、站点地图等内容。和 `<header>` 一样，一个页面也可以有多个`<footer>` 元素。
  - 文章：`<article>` 表示一个独立的、完整的、可以独自被外部引用的内容块，如博客文章、论坛帖子、新闻报道等。它可以嵌套其他标签，甚至可以包含 `<header>`、`<footer>` 等。
  - 部分：`<section>` 用于对文档进行分节，通常包含一个主题相关的内容块，有自己的标题。它和 `<article>` 的区别在于，`<article>` 更强调独立性，而 `<section>` 更侧重于内容的分段。
  - `<figure>` 和 `<figcaption>`  `<figure>` 用于表示一段独立的可视化内容，如图片、图表、代码块等；`<figcaption>` 用于为 `<figure>` 元素添加标题或说明。如：
  ```html
  <figure>
    <img src="example.jpg" alt="示例图片">
    <figcaption>这是一个示例图片</figcaption>
  </figure>
  ```
  - `<div>` 块级元素，用于对页面进行分块，通常用于布局和样式控制，让内部元素独占一行。
  - `<span>` 是一个内联元素，它不会换行，只会占据内容所需的宽度。通常用于对文本或其他内联元素进行分组，以便为它们应用特定的样式或进行 JavaScript 操作。
- 链接：HTML 链接（Link）是通过`<a>`标签来定义的，如
  - 普通的链接：`<a href="http://www.example.com/">链接文本</a>`
  - 图像链接： `<a href="http://www.example.com/"><img src="URL" alt="替换文本"></a>`
  - 邮件链接： `<a href="mailto:webmaster@example.com">发送e-mail</a>`
  - 书签：
    - `<a id="tips">提示部分</a>`
    - `<a href="#tips">跳到提示部分</a>`
- 图像：HTML 图像是通过标签 <img> 来定义的。如`<img src="URL" alt="替换文本" height="42" width="42">`
- 引入外部资源：
  - 引入外部样式表：`<link rel="stylesheet" type="text/css" href="mystyle.css">`
  - 引入外部脚本：`<script src="myscript.js"></script>`
  - 引入外部图片：`<img src="URL" alt="替换文本">`
- 样式：
```html
<!-- 行内样式,和css有关 -->
<style type="text/css">
  h1 {color:red;}
  p {color:blue;}
</style>
```
- 表格：HTML 表格是通过标签 <table> 来定义的。如：
```html
<table border="1">
  <tr>
    <th>表格标题</th>
    <th>表格标题</th>
  </tr>
  <!-- 标题就相当于字段名，数据就是数据，每一行数据就是一个tr，当然可以用js循环 -->
  <tr>
    <td>表格数据</td>
    <td>表格数据</td>
  </tr>
</table>
```
- 列表(记得包含在`<p>`)：
  - 无序列表：HTML 无序列表是通过标签 <ul> 来定义的。如：
  ```html
  <ul>
    <li>列表项</li>
    <li>列表项</li>
  </ul>
  ```
  - 有序列表：HTML 有序列表是通过标签 <ol> 来定义的。如：
  ```html
  <ol>
    <li>列表项</li>
    <li>列表项</li>
  </ol>
  ```
  - 定义列表：HTML 定义列表是通过标签 <dl> 来定义的。如：
  ```html
  <dl>
    <dt>定义列表项</dt>
    <dd>定义列表项</dd>
  </dl>
  ```
- 框架：HTML 框架是通过标签 <frame> 来定义的。如：`<iframe src="demo_iframe.htm"></iframe>`
- 实体：
  - 字符实体：常见的字符实体有：
    - `&lt;` 小于号 less than 在 HTML 中，如果直接写<，浏览器可能会将其误认为是标签的开始，使用&lt;可以正确显示小于号。
    - `&gt;` 大于号 greater than 
    - `&amp;` 与号 and 和号在 HTML 中有特殊用途，用于表示实体的开始，所以要显示和号本身，需要使用&amp;。
    - `&qout` 引号 quotation mark 在 HTML 标签的属性中，如果属性值包含引号，为了避免与属性值的界定引号冲突，可使用&quot;。
    - 空格：&nbsp; HTML 中，多个连续的空格会被浏览器合并为一个空格，使用&nbsp;可以实现不被合并的空格效果，常用于在文本中插入特定数量的空白间隔。
  - 符号实体：常见的符号实体有：
    - 版权符号：&copy;
    - 注册商标符号：&reg;
    - 商标符号：&trade;
- 注释：HTML 注释是通过标签 <!-- 注释文本 --> 来定义的。
## 表单：
```html
<form action="demo_form.php" method="post/get">
  <!-- 基本文本框，他的属性其他都有 -->
  <input type="text" name="email" size="40" maxlength="50">
  <!-- 密码输入框，你看不见输入 -->
  <input type="password">
  <!-- 复选框，checked表示默认被选中 -->
  <input type="checkbox" checked="checked">
  <!-- 单选框，必须要给需要单选的输入用相同的name分为一组才能实现单选 -->
  <input type="radio" checked="checked">
  <!-- 提交按钮，value只按钮文本 -->
  <input type="submit" value="Send">
  <!-- 重置按钮，当用户点击该按钮时，表单中的所有输入字段会被重置为它们的默认值 -->
  <input type="reset" value="reset">
  <!-- 隐藏按钮 -->
  <input type="hidden" value="hidden">
  <!-- 输入数字的输入框，浏览器会提供一些数字输入的交互，比如可以通过上下箭头来增减数值，还可以设置输入的最小值、最大值和步长等 -->
  <input type="number" min="0" max="100" step="1">
  <!-- 用于输入日期的输入框，还有time为时间输入框，datetime为日期时间 -->
  <input type="date">
  <!-- 文件选择框，允许用户从本地文件系统中选择一个或多个文件进行上传 -->
  <input type="file" multiple>
  <!-- 下创建搜索输入框 -->
  <input type="search" placeholder="Search...">
</form>
```
- input除了以上type还有：
  - email：用于输入电子邮件地址
  - url：用于输入网址
  - tel：用于输入电话号码
  - slider：用于输入滑块
  - range：用于输入范围
  - color：用于输入颜色
  - range：用于输入范围
  - 等等
- 下拉框：
```html 
<select id="simple" name="simple">
  <option>Banana</option>
  <option selected>Cherry</option>
  <option>Lemon</option>
</select>
<!-- 用input和datalist可组成自动补全框，即可以下拉可以输入 -->
<label for="myFruit">What's your favorite fruit?</label>
<input type="text" name="myFruit" id="myFruit" list="mySuggestion" />
<datalist id="mySuggestion">
  <option>Peach</option>
  <option>Pear</option>
</datalist>
```
- textarea：可以多行输入
- flieldset：给进行相同操作的表单包围起来，那么这样就可以给不同的表单进行分组，分组的元素name应该相同
  - legend：定义flieldset的标题
- `<botton>`:
  - submit：提交按钮
  - reset：重置按钮
  - anonymous：匿名按钮
- `<label>`:用于定义表单控件的标签、视频、音频、图片等，以表单控件为例：
```html
<label for="email">电子邮件：</label>
<input type="text" id="email"> 
```
- html5的内置表单检验：
  - required：必填
  - pattern：正则表达式
  - min：最小值
  - max：最大值 
  - maxlength：最大长度
  - minlength：最小长度
  - step：步长
  - multiple：多选
其中用for属性指定了标签的指向
以上只是基础，更详细的（表单）[https://developer.mozilla.org/zh-CN/docs/Learn_web_development/Extensions/Forms]
## 一些参数：
- value：
  - 用于定义输入框、下拉框、复选框等元素的默认值
  - 用于定义按钮的文本
- id: 用于定义全局元素的唯一标识符，可以通过 JavaScript 或 CSS 来引用该元素  
- name: 用于定义表单元素的名称，在表单提交时，会将该名称（可以看做键）和对应的值（value）一起提交，另外对于本质上相同的操作，name应该相同方便管理
- size: 用于定义输入框的宽度、下拉框的高度选项数
- class: 用于定义元素的样式类，可以通过 CSS 来引用该类
- rel: 用于定义链接的关系，如：
  - `rel="stylesheet` 定义外部样式表
  - `icon`: 定义网站图标
  - `prev` 和 `next`：定义上一页和下一页,用于定义链接的关系，如：
  ```html
  <a href="https://example.com/page-1" rel="prev">上一页</a>
  <a href="https://example.com/page+1" rel="next">下一页</a>
  ``` 
  - `first` 和 `last` : 定义首页和末页，参考prev和next
  - `preload`: 预加载资源，需要配合as指定资源类型
  - `prefetch`:提示浏览器在空闲时预先获取资源，需要配合as指定资源类型
  - `canonical`：定义网站的标准URL
  - `author`、`publisher`、`copyright`、`license`：定义作者、发布者、版权信息、许可证
  - `readOnly`、`disabled`：定义只读和禁用状态
  - `placeholder`：定义输入框的占位文本,用来简略描述输入框的目的。
## 关于文本：
- `<br>`: 换行
- `<pre>`: 保留文本格式
- `<hr>`: 水平线
- `<sup>`: 上标
- `<strong>`: 加粗
- `<em>`: 斜体
- `<del>`: 删除线
- `<ins>`: 下划线
- `<sub>`: 下标 
- `<mark>`: 高亮
- `<q>`: 引用
- `<abbr>`: 缩写
- `<cite>`: 引用 