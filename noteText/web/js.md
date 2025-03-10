# 变量：
## 定义：
- let: 定义变量，常用
  - 可以直接赋值
  - 或者new一个下面变量类型的对象
- var: 声明变量，可以重复声明，但本身不常用
- const： 定义常量，不可修改
## 变量类型及一些常用方法：
- 基本类型：
  - number: 数字,即不分整型、浮点型了
  - string: 字符串
    - 大部分和java一样
    - slice(start, end) 
    - substr(start, length) 
  - boolean: 布尔值
  - array: 数组 这个就可看做对象列表
    - 用push和pop对最后一个元素增删
    - 而shift和unshift对第一个元素增删
    - slice(start, end) 截取数组，返回一个新数组，原数组不变
    - splice(start, deleteCount, item1, item2, ...) 删除元素，并可以往start位置添加元素，返回一个新数组，原数组改变
    - concat(arr1, arr2,...) 合并数组，返回一个新数组，原数组不变
    - join(separator) 将数组转换为字符串，separator为分隔符，默认为逗号
    - reverse() sort() 
    - 自定义排序，重写sort方法，定义a和b，返回a-b为升序，b-a为降序
  - 对象：
    - 两种定义方式：
      ```js
      // 方式一
      var person = new Object();
      person.name = "孙悟空";
      person.age = 18;
      person.sayHello = function(){
          console.log("Hello");
      };
      // 方式二
      var person = {
          name: "孙悟空",
          age: 18,
          sayHello: function(){
              console.log("Hello");
          }
      };
      ```
    - call方法：
      - 可以用来调用对象的方法，如：
        ```js
        function fun(a, b) {
            console.log("a = " + a);
            console.log("b = " + b);
            console.log("fun = " + this);
        }

        var obj = {
            name: "obj",
            sayName: function () {
                console.log(this.name);
            }
        };

        fun(2, 3);
        console.log("===============");
        fun.call(obj, 2, 3);
        ```
    - **arguments**对象，可以用来获取函数的参数，类型java的mian方法的args一样，我们函数所接受的参数都存在这里面
  - undefined: 未定义
  - null: 空值
  - symbol: 符号  
  - Date：日期
  - Math：数学 无需new 可看作里面都是静态方法
  - 正则表达式：
    - var 变量名 = new RegExp("正则表达式","匹配模式");
    - 匹配模式：
      - i: 忽略大小写
      - g: 全局匹配
      - m: 多行匹配
    - 常用方法：
      - test(str): 测试字符串是否匹配正则表达式，返回true或false
      - exec(str): 执行正则表达式，返回一个数组，数组中包含匹配的字符串
      - match(str): 执行正则表达式，返回一个数组，数组中包含匹配的字符串
      - search(str): 执行正则表达式，返回匹配的位置
    - 详细见正则表达式文件夹 
## 数值转换：
1. 转字符串：
   - toString(): 转换为字符串,但不能转换null和undefined
   - String(): 转换为字符串,可以转换null和undefined
   - 任意类型用+连接: 转换为字符串,可以转换null和undefined
2. 转数字
   - number(): 转换为数字,空串和null转换为0,undefined及其他不合法的转换为NaN  
   - parseInt(): 专门用来字符串转整数,不合法同上
   - parseFloat() ：专门用来字符串转浮点数,不合法同上
3. 转布尔值
   - Boolean(): 转换为布尔值,除了0、NaN、空串、null、undefined、false,其他都为true
   - !: 取反,同上
   - !!: 取反两次,同上
# 关键字（java里没有的）：
- typeof: 返回变量的类型,如typeof 1或 type(1) 返回number 
- debugger: 调试器,在浏览器中打开控制台，在代码中加入debugger，会在控制台中停止执行，方便调试
- delete: 删除变量
- export: 导出变量
- yeld: 生成器，用来生成迭代器
- in: 判断属性是否在对象中,如"name" in person 返回true，，等同hasOwnProperty()方法，形式类似于python
# 运算符：
大部分和java一样，但是有一些不同：
- ==和===: ==是值相等，===是值和类型都相等
- !=和!==: 同上
- 和python一样可以用，隔开来多次定义变量
 
# 条件、循环、函数：
多数和java一样，但是有一些不同：
- 遍历：
  - 可以用for...in来遍历对象，类似python
  - 还可以用forEach来遍历数组，但是不能用break和continue且只支持IE9+
- 函数定义用function定义
- 另外js一般用不到class，而用对象来代替，同样里面写属性和方法，但我们可以不用定义如以上介绍对象的例子，故封装一般用对象来代替，如：
  ```js
  // 使用构造函数来创建对象
    function Person(name, age) {
        // 设置对象的属性
        this.name = name;
        this.age = age;
        // 设置对象的方法，略
    }
  ```
但如果想更灵活，我们可以原型链来实现，原理是获取类原型来设置方法和属性：
```js
// 使用构造函数来创建对象
function Person(name, age) {
    // 设置对象的属性
    this.name = name;
    this.age = age;  
}
// 设置对象的方法
Person.prototype.sayHello = function() {
    console.log("Hello, my name is " + this.name + " and I am " + this.age + " years old.");
};
```
也可以设置属性一样的形式
# 继承：
常用继承形式如下：
```js
function Person(name, age) {
    this.name = name;
    this.age = age;
}

Person.prototype.setName = function (name) {
    this.name = name;
};

function Student(name, age, price) {
    Person.call(this, name, age); // 为了得到父类型的实例属性和方法，this指向子类，固定写法
    this.price = price;
}

Student.prototype = new Person(); // 为了得到父类型的原型属性和方法
Student.prototype.constructor = Student; // 修正constructor属性指向
Student.prototype.setPrice = function (price) { // 添加子类型私有的方法，没什么特别的
    this.price = price;
};
```
# 输出：
# 垃圾回收：
将不用的变量设置为null即可，等待垃圾回收器回收
# ***对于web交互***
## DOM：
## 事件：
  - addEventListener("事件类型", 事件处理函数)
    - 常见事件类型：
      - submit 表单提交
      - click dbclick 点击和双击
      - mousedown mouseup mousemove 鼠标按下、抬起、移动在
      - mouseover mouseout 鼠标移入、移出
      - keydown keyup keypress 键盘按下、抬起、按下并松开
      - 事件处理函数就是监听到事件后要执行的函数，一般直接匿名函数
      - 可设置多个事件监听，监听到事件后会依次执行
  - removeEventListener("事件类型", 事件处理函数) 移除事件监听
  - **事件处理器**，相对addaddEventListener更简洁：
    - on事件类型 = 事件处理函数
    - 只能设置一个事件监听，监听到事件后会依次执行
    - 移除事件监听：on事件类型 = null
  - 事件对象固定一般参数为event、evt 或 e等等，事件处理函数内部可以使用事件对象的属性和方法
  - preventDefault() 阻止表单提交，用于在表单不合法时阻止表单提交
  - **事件冒泡：**
    1. 在父元素上监听事件，子元素也会触发事件
    2. 冒泡顺序：子元素 -> 父元素 -> 祖先元素，不好理解看(例子)[https://developer.mozilla.org/zh-CN/docs/Learn_web_development/Core/Scripting/Event_bubbling]
    3. 由于冒泡顺序，可能会导致一些问题，我们可以用事件对象的stopPropagation()方法来阻止事件冒泡
  - 事件捕获：  
    1. 事件捕获一般是由于很少用一般是禁用的，我们要用capture: true来开启
    2. 事件捕获就是事件冒泡的相反顺序，祖先元素 -> 父元素 -> 子元素
    3. 由于捕获顺序，可能会导致一些问题，我们可以用事件对象的stopPropagation()方法来阻止事件冒泡
  - **事件委托：**
    - 即利用事件冒泡的原理，在父元素上监听事件，子元素也会触发事件，这样就可以避免多个子元素的事件监听，提高性能
## Ajax：
## bom:  