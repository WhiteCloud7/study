# 格式：
```css
元素1,
元素2,
元素3等等
.类名1,
.类名2,
.类名3等等{
    属性1:属性值1;
    属性2:属性值2;
    ...
}
```
**另外，以上将多个元素和类名写在一起组成一个选择器列表的形式，必须是这些元素和类型是相同的css样式。**不一样的样式需要写多个选择器即分开写。
# 选择器的种类：
1. 标签选择器：指向标签的选择器，即上面的元素1，元素2，元素3等等
2. 类选择器：指向类的选择器，即上面的.类名1，.类名2，.类名3等等，*注意类名前面的点*
3. id选择器：指向id的选择器，即上面的#id1，#id2，#id3等等，*注意id前面的#*
4. 标签属性选择器：根据一个元素上的某个标签的属性的存在以选择元素的不同方式，如`a[title] `，根据a标签上是否有title属性来选择a标签。或者根据一个有特定值的标签属性是否存在来选择，如`a[href="https://example.com"]`,根据链接是否存在来选择a标签。
5. 伪类选择器：根据元素的状态来选择元素
    - 链接伪类：
      - `a:hover`：鼠标悬停在链接上时选择链接
      - `a:active`：用于选择被激活（通常是鼠标按下但还未释放）链接
      - `a:visited`：已经访问过的链接
      - `a:focus`：鼠标点击链接时选择链接
      - `a:link`：用于选择未被访问过的链接
    - 结构伪类：
      - `li:last-child`：选择该元素的最后一个元素,如`li:last-child`选择最后一个li元素
      - `li:nth-child(n)`: 选择该元素的第n个元素,如`li:nth-child(2)`选择第二个li元素
      - `:only-child`: 选择是该元素唯一元素,如`div:only-child`选择唯一的div元素，示例：
     ```css
    <div>
    <div>我是唯一子元素。</div>
        </div>

        <div>
        <div>我是第一个兄弟元素。</div>
        <div>我是第二个兄弟元素。</div>
        <div>
            我是第三个兄弟元素。
            <div>但这又是唯一的子元素。</div>
        </div>
    </div>
     ```
      上述代码中，第一个div元素是唯一的子元素，因此它将被选中。而第二个div元素包含多个子元素，因此它将不会被选中。
      - `::first-line`: 选择元素的第一行文本,如`p::first-line`选择p元素的第一行文本,其他的类似略，另外还有first-letter、first-word等
      - before、after：
        - `::before`：在元素内容之前插入内容,如`p::before`在p元素内容之前插入内容,对应的属性是content
        - `::after`：在元素内容之后插入内容,如`p::after`在p元素内容之后插入内容，对应同上
    - 表单伪类：
      - `input:checked`：选择被选中的表单元素，如单选按钮或复选框
      - `input:disabled`：选择禁用的表单元素
      - `input:focus`：选择获得焦点的表单元素,其他的表单元素类似略
      - `ipuut:invalid`：选择无效的表单元素,即不符合要求的表单元素
    - 目标伪类：
      - `:target`：选择当前活动的目标元素，通常用于创建单页面应用程序（SPA），如：
      ```css
      #section1:target {
        display: block;
      }
      ```
      当用户点击链接`#section1`时，该选择器将匹配对应的元素并显示出来。 
6. 运算符：选择器可以将其他选择器组合起来，更复杂的选择元素。
   - 后代选择器：如`div p`，选择div元素的所有后代元素p 
   - 子代选择器：如`div > p`，选择div元素的所有**直接**子元素p
   - 邻接兄弟选择器：如`div + p`，选择div元素后面的第一个兄弟元素p
   - 通用兄弟选择器：如`div ~ p`，选择div元素后面的所有兄弟元素p

# 盒子模型：
以下均用用display属性来控制
1. 外部显示类型：
   - block：块级元素
     - 盒子会产生换行。
     - width 和 height 属性可以发挥作用。
     - 内边距、外边距和边框会将其他元素从当前盒子周围“推开”。
     - 如果未指定 width，方框将沿行向扩展，以填充其容器中的可用空间。在大多数情况下，盒子会变得与其容器一样宽，占据可用空间的 100%。 
     - 某些 HTML 元素，如`<h1>`和`<p>` ，默认使用 block 作为外部显示类型。 
   - inline：行内元素
     - 盒子不会产生换行。
     - width 和 height 属性将不起作用。
     - 垂直方向的内边距、外边距以及边框会被应用但是不会把其他处于 inline 状态的盒子推开。
     - 水平方向的内边距、外边距以及边框会被应用且会把其他处于 inline 状态的盒子推开。
     - 某些HTML元素，如`<a>`、`<span>`、`<em>`以及`<strong>`，默认使用 inline 作为外部显示类型。
   - inline-block：行内块元素：提供了介于 inline 和 block 之间的中间位置。如果不希望项目换行，但又希望它使用 width 和 height 值并避免出现上述重叠现象。
2. 内部显示类型：
   - 元素的内部显示类型决定了元素的子元素如何排列和显示。  
3. 盒模型的各个部分：
   - 内容盒子：显示内容的区域；使用 inline-size 和 block-size 或 width 和 height 等属性确定其大小。
   - 内边距盒子：填充位于内容周围的空白处；使用 padding 和相关属性确定其大小，有padding-top、padding-right、padding-bottom、padding-left，以及类似padding-color、padding-right-color等等，下面的边框和外边距。
   - 边框盒子：边框盒子包住内容和任何填充；使用 border 和相关属性确定其大小。
   - 外边距盒子：外边距是最外层，其包裹内容、内边距和边框，作为该盒子与其他元素之间的空白；使用 margin和相关属性确定其大小。
     - 外边距合并：
        - 两个正外边距将合并为一个外边距。其大小等于最大的单个外边距。
        - 两个负外边距会折叠，并使用最小（离零最远）的值。
        - 如果其中一个外边距为负值，其值将从总值中减去。
4. 替代盒模型：兼容IE或者想用固定大小的盒子的时候使用
   - 替代盒模型是一种特殊的盒模型，它允许开发者在不改变盒模型的情况下调整元素的大小。
   - 替代盒模型的大小是由 border-box 和 content-box 两个值决定的。
   - border-box：元素的大小包括内容、内边距和边框，即元素的大小等于 width 和 height 的总和。
   - content-box：元素的大小只包括内容，即元素的大小等于 width 和 height 的总和减去内边距和边框的大小。
   - 替代盒模型的大小可以通过 box-sizing 属性来设置。
   - 替代盒模型的大小可以用于调整元素的大小，以适应不同的布局需求。 
   - 要在所有元素中使用替代方框模型（这是开发人员的常见选择），请在`<html>`元素上设置 box-sizing 属性，并将所有其他元素设置为继承该值：
   ```css
   html {
    box-sizing: border-box;
    }

    *,
    *::before,
    *::after {
    box-sizing: inherit;
    }
   ```
5.**弹性盒模型：**
   - 弹性盒模型是一种布局模型，它被专门设计出来用于创建横向或是纵向的一维页面布局。要使用 flexbox，你只需要在想要进行flex布局的父元素上应用`display: flex` ，所有直接子元素都将会按照flex进行布局。
   - 
   - flex属性：
     - `flex: initial`:元素不会主动扩展以填充剩余空间，但会根据需要缩小以适应容器。弹性项目的初始大小由其内容决定。 
     - `flex: auto`:元素会主动扩展以填充剩余空间，并且在必要时缩小以适应容器。弹性项目的初始大小由其内容决定。
     - `flex: none`:略
     - `flex: 扩展比例`:元素会根据指定的比例扩展以填充剩余空间，并且在必要时缩小以适应容器。如果有两个弹性项目，一个`flex:1`，另一个`flex:2`，那么后者会占用两倍于前者的剩余空间。
     - `flex:扩展比例 缩小比列 初始大小`:如`flex: 1 2 300px`，1表示元素会按照这个比例扩展以填充剩余空间。2 表示在容器空间不足时，该元素会以这个比例缩小。300px 表示元素的初始大小为 300 像素。
   - 对于盒子内部其他属性：
     - `flex-direction`:决定主轴的方向（即项目的排列方向）。
       - `row`（默认值）：主轴为水平方向，起点在左端。
       - `row-reverse`：主轴为水平方向，起点在右端。
       - `column`：主轴为垂直方向，起点在上沿。
     - `flex-wrap`:决定如果一条轴线排不下，如何换行。
       - `nowrap`（默认）：不换行。
       - `wrap`：换行，第一行在上方。
       - `wrap-reverse`：换行，第一行在下方。
     - `flex-flow`:是 flex-direction 属性和 flex-wrap 属性的简写形式，默认值为 row nowrap。
     - `justify-content`:定义了项目在主轴上的对齐方式。
       - `flex-start`（默认值）：左对齐
       - `flex-end`：右对齐
       - `center`： 居中
     - `align-items`:定义项目在交叉轴上如何对齐。
       - `flex-start`：交叉轴的起点对齐。
       - `flex-end`：交叉轴的终点对齐。
       - `center`：交叉轴的中点对齐。
     - `align-content`:定义了多根轴线的对齐方式。如果项目只有一根轴线，该属性不起作用。值同justify-content
   - 要在所有元素中使用弹性盒模型（这是开发人员的常见选择），请在`<html>` 元素上设置 display 属性，并将所有其他元素设置为。
   ```css
   html {
    display: flex;
    }
    * {
    display: inherit;
    }
   ```  
# 层叠、优先级与继承：
1. 继承：
   - 继承的基本规则，一下面例子说明：
   ```css
   body{
    color:blue;
    background: blue;
   }
   p{
    background: blanchedalmond;
   }
   ```
   上述代码中，由于p元素在body内部，故p的属性会继承body的属性即color和background，但background被p重写了，故p的background为blanchedalmond，这里注意以上是自动继承的情况，一般颜色、背景、字体等属性都是自动继承的。
   -  继承的值（即给属性直接赋以下值）：
      - inherit：继承父元素的属性值
      - initial：将属性设置为其初始值。
      - unset：将属性设置为其默认值。
      - revert：将属性设置为其默认值。 
2. 层叠：
- 三个点：资源顺序<优先级<重要程度
  - 资源顺序：如果你有超过一条规则，而且都是相同的权重，那么最后面的规则会应用。
  - 优先级：我们知道优先级id>类>元素，那么计算规则如下：
    - 一个选择器的优先级可以说是由三个不同的值（或分量）相加，可以认为是百（ID）十（类）个（元素）——三位数的三个位数：
      - ID：选择器中包含 ID 选择器则百位得一分。
      - 类：选择器中包含类选择器、属性选择器或者伪类则十位得一分。
      - 元素：选择器中包含元素、伪元素选择器则个位得一分。
  - 内联样式：!important，如`color: red !important;`，最优先级，但一般不使用，否则会导致样式很难调整。
# 布局：
1. 正常流：即block和inline，上面有介绍。
2. 在正常流用一些属性改变布局:
   - float：浮动，如`float: left;`，将元素向左浮动，即元素会向左移动，直到它的左边没有其他元素。
   - position：定位（范围是整个页面，不是元素之间）
      - static：默认值，元素不会被定位。
      - relative：元素会被定位，但不会影响其他元素的位置。
      - absolute：元素会被定位，但会影响其他元素的位置。
      - fixed：元素会被定位，但会固定在屏幕上，即使页面滚动也不会移动。
      - 对应的定位属性top、bottom、left、right，分别表示元素距离顶部、底部、左边、右边的距离。 
   - display：显示，用于盒型模型
3. flex布局：在盒模型有详细介绍
4. 网格布局grid：
   - 设置网格容器：
      - display: grid; 
      - grid-template-columns: 定义每一列的宽度
      - grid-template-rows: 定义每一行的高度
        - 对于网格的每一行和每一列，都可以使用以下单位：
          - px：像素
          - %：相对于整个网格的百分比
          - fr：相对于可用空间的比例
        - auto：自动计算
        - 对于很多行或列，可以用repeat()函数来重复指定的值
      - grid-gap: 定义网格之间的间距
    - 往网格中添加元素：
      - grid-column: 定义元素在网格中的列
      - grid-row: 定义元素在网格中的行
5. 表格布局：
- tablelayout：
  - auto：自动布局，即根据内容自动调整表格的宽度
  - fixed：表格的列宽由`table`和`col`元素的`width`属性决定，或者在没有设置这些属性时，平均分配列宽。一旦列宽确定，表格内容的多少不会影响列宽，如果内容过多，可能会溢出单元格,但性能较好。
- border-collapse：
  - collapse：相邻单元格之间的边框会合并成一个单一的边框
  - separate：相邻单元格之间的边框会被分开  
一个简单的表格布局示例：
```css
  /* 表格样式 */
  table{
      justify-content: center;
      table-layout: fixed;
      margin: 0 auto; /* 这里是为了配合使表格居中 */
      width: 80%;
      border-collapse: collapse;
      border: 1px solid #ddd;
  }
  /* 表格行样式 */
  th, td{
      padding: 10px;
      text-align: left;
      border: 1px solid #ddd;
  }
  /* 表格列宽设置 */
  th:nth-child(1) {
      width: 10px;
  }

  td:first-child,
  th:first-child {
      border: none;
  }
```
6. 多列布局：我们可以使用`column-count`属性来告诉浏览器我们需要多少列，也可以使用`column-width`来告诉浏览器以至少某个宽度的尽可能多的列来填充容器。
7. 响应式布局：
# 文本样式： 
1. 字体：
   - font-family：字体，可写多个字体，浏览器从左往右依次寻找，直到找到可用的字体。用于保证字体的可用性。
   - font-size：字体大小
   - font-weight：字体粗细
   - font-style：字体样式
   - font-transform：字体变形
     - none：默认值，无变形
     - capitalize：将每个单词的首字母转换为大写
     - uppercase：将所有字母转换为大写
     - lowercase：将所有字母转换为小写
     - full-width：将所有字母转换为全宽字符
     - full-size-kana：将所有假名转换为全宽字符
   - text-decoration：文本装饰
     - underline：下划线
     - overline：上划线
     - line-through：删除线
     - blink：闪烁
   - text-shadow：文本阴影
2. 文本布局：
   - text-align：文本对齐
     - left：左对齐
     - right：右对齐
     - center：居中对齐 
     - justify：两端对齐
   - 行高：
     - line-height：行高
     - vertical-align：垂直对齐  
   - 字母和单词间距：
     - letter-spacing：字母间距
     - word-spacing：单词间距
3. 列表样式：
   - 基本的盒型模型，略
   - 行高，略
   - 对齐，略
   - list-style-type：列表样式类型
     - disc：实心圆
     - circle：空心圆
     - square：实心方块
     - decimal：数字
     - decimal-leading-zero：数字，前面有0
     - lower-roman：小写罗马数字
     - upper-roman：大写罗马数字
    - list-type-position：列表样式位置
      - inside：列表样式在文本内部
      - outside：列表样式在文本外部
    - list-style-image：列表样式图像(一般不用这个用background-image)
   - 管理列表计数：
4. 链接样式：
   - 基本的盒型模型，略
   - 行高，略
   - 伪类选择器，略
# 常用属性(上面提到的就不写了)：
- all: 所有属性
- color: 颜色
  - background-color: 背景颜色
  - color: 文本颜色
- background: 背景
- display: 显示，用于盒型模型
- content: 内容
- atuoflow: 自动换行
- box-shadow:定义了盒子的阴影
  - `box-shadow: 水平偏移 垂直偏移 模糊距离 阴影尺寸 阴影颜色`
  - 如`box-shadow: 0 0 10px 5px #000;`
   - 其中，水平偏移和垂直偏移是阴影的位置，模糊距离是阴影的模糊程度，阴影尺寸是阴影的尺寸，阴影颜色是阴影的颜色 
# 单位：
- 长度单位：
  - 绝对长度最常用的是px，即像素，其他略
  - 相对长度：
    - em：相对于当前元素的字体大小。如果当前元素没有设置字体大小，那么它会继承父元素的字体大小。如1em等于当前元素的字体大小。
    - rem：相对于根元素的字体大小，用法同上。
    - vw：相对于视口宽度的百分比，如1vw=视口宽度的1%
    - vh：相对于视口高度的百分比，如1vh=视口高度的1%
    - vmin：相对于视口宽度和高度中较小的那个百分比，如1vmin=视口宽度和高度中较小的那个的1%
    - vmax：相对于视口宽度和高度中较大的那个百分比，如1vmax=视口宽度和高度中较大的那个的1%  
    - %：相对于父元素的百分比 
    - 等等 