[官方文档](https://cn.vuejs.org/guide/)  
[参考文档1 全面文档 但有些是vue2的旧特性要区分](https://blog.csdn.net/weixin_45735355/article/details/118931768?ops_request_misc=%257B%2522request%255Fid%2522%253A%252271c81f6b5dee99ead33de0fd9499523d%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=71c81f6b5dee99ead33de0fd9499523d&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-118931768-null-null.142^v102^pc_search_result_base8&utm_term=vue&spm=1018.2226.3001.4187)  
[参考文档2 介绍vue3新特性](https://blog.csdn.net/2302_76329106/article/details/139447910?ops_request_misc=%257B%2522request%255Fid%2522%253A%25221a1a6401493d6d80267919a3189819e2%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=1a1a6401493d6d80267919a3189819e2&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-139447910-null-null.142^v102^pc_search_result_base8&utm_term=vue3&spm=1018.2226.3001.4187)  
# 创建项目、启动
[具体安装步骤看这篇博客](https://blog.csdn.net/m0_57545353/article/details/124366678)
所有下载、配置好了之后，在你想要创建vue项目的文件夹下用`vue create 项目名`创建项目，然后`npm install`下载依赖，最后`npm run serve`启动项目。
# 导入
```html
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<!-- 生产环境版本，优化了尺寸和速度 -->
<script src="https://cdn.jsdelivr.net/npm/vue"></script>
<!-- 本地导入 -->
 <script src="本地路径"></script>
```
# 基本语法
## createApp和普通组件创建
### createApp
createApp是vue3的新特性，用于创建vue应用的根组件。它是整个 Vue 应用的起点，负责管理和协调应用中的所有组件、状态、路由等。从这个根实例开始，Vue 会根据组件的嵌套关系和路由配置，逐步渲染出整个应用的各个页面和组件，包括首页以及其他所有相关的页面和功能模块。以下是一个例子：
### 普通组件创建
#### defineComponent函数（选项api）：
语法为`export default defineComponent({组件配置})` 其中会有以下选项（这里这时理出，具体我们可以再例子看道，例子没有可能在单独的一个点）：
- data()：data 是一个函数，其返回值是一个对象，该对象包含了组件的数据 ***将其存储并赋初值***。这些数据是响应式的，当数据发生变化时，Vue 会自动更新与之绑定的 DOM 元素。而 ***进行动态更新的逻辑通常在方法里面实现。***
- methods：methods 是一个对象，其中的每个属性都是一个函数，这些函数用于定义组件的方法。可以在模板中通过事件绑定（如 @click）来调用这些方法，也可以在组件的其他方法中调用。方法用于处理用户交互、执行逻辑操作等。
- computed 用于定义计算属性。计算属性是基于已有数据进行计算得出的属性，其值会根据依赖的数据自动更新。与方法不同，计算属性是有缓存的，只有当依赖的数据发生变化时，才会重新计算。
- watch 用于监听数据的变化，并在数据变化时执行相应的操作。当需要在数据变化时执行异步操作或者复杂的逻辑时，使用 watch 比较合适。
- props：props 用于在组件之间传递数据。父组件可以通过 props 向子组件传递数据，子组件可以接收并使用这些数据。
- components：components 用于注册子组件。在父组件中，可以通过 components 选项来注册子组件，然后在模板中使用这些子组件(用自定标签，标签名为组件名)
- mounted：mounted 是一个生命周期钩子函数，在组件实例挂载到 DOM 后调用。可以在 mounted 中执行一些初始化操作，如发送网络请求、初始化第三方库等。即在组件初始化时执行的操作（***选项api直接要this.函数名挂载，语法糖用mounted() 的匿名内部类实现***）
## 插值表达式
`{{ 变量名/对象.属性名 }}` 用于简单的展示数据及动态更新数据。
## v-text和v-html
v-text(纯文本数据)和v-html(遇到html标签会正确解析)专门用来展示数据, 其作用和插值表达式类似。v-text和v-html可以避免插值闪烁问题。如v-text：`<span v-text="msg"></span>	<!-- 相当于<span>{{msg}}</span> -->` v-html：`<span v-html="msg"></span>	<!-- 相当于<span>{{msg}}</span> -->`  
​当网速比较慢时, 使用{{}}来展示数据, 有可能会产生插值闪烁问题。 
​插值闪烁: 在数据未加载完成时，页面会显示出原始的{{}}, 过一会才会展示正常数据。  
## v-model（数据双向绑定数据）  
Vue的双向绑定可以实现: 数据变化的时候, 页面会自动刷新, 页面变化的时候，数据也会自动变化,另外注意：
1. 双向绑定, 只能绑定**文本框,单选按钮,复选框,文本域,下拉列表**等
2. 文本框/单选按钮/textarea, 绑定的数据是字符串类型
3. 单个复选框, 绑定的是boolean类型,即true为选中, false为未选中
4. 多个复选框, 绑定的是数组
5. select单选对应字符串，多选对应也是数组  
以下是插值表达式、v-text\v-html和v-model的例子：
```html
<template>
  <div class="div">
    <p>我是你爹:{{name}}</p> <!-- 插值表达式 -->
    <p>我：<span v-text="age"></span></p> <!-- v-text -->
    <span v-html="html"></span><br> <!-- v-html -->
    <input type="text" v-model="username"><br> <!-- 绑定文本框 -->
    <input type="checkbox" v-model="agree">哈哈 <!-- 绑定单个复选框 -->
    <input type="checkbox" value="math" v-model="subject">数学 <!-- 绑定多个复选框,value传递值 -->
    <input type="checkbox" value="Chinese" v-model="subject">语文
    <input type="checkbox" value="English" v-model="subject">英语<br>
    <select v-model="selectOption"> <!-- 静态下拉框，动态在v-for介绍 -->
      <option value="A">我是天才</option>
      <option value="B">你是蠢材</option>
      <option value="C">我说得对</option>
    </select>
  </div>
</template>
<script>
import { defineComponent } from 'vue';
export default defineComponent({
  data() {  //这里是给了初始值，至于怎么动态更新我们后面在说
    return { 
        name: "刘文杰",
        age: "21岁",
        html: '<a href="https://blog.csdn.net/">点击访问 CSDN</a>',
        username:"云白",
        agree: true,
        subject:["math","English"],
        selectOption:"A"
    };
  }
});
```
## v-bind
`v-bind`和插值表达式或双向绑定用法差不多，只不过`v-bind`是用于绑定标签属性的后面便于动态，而插值表达式是用于展示数据的。格式为`v-bind:属性名="数据"`，也可以简写为`:属性名="数据"`
## 事件绑定
`v-on:事件名="函数名/vue表达式"`，简写为`@事件名="函数名/vue表达式"`，vue事件支持html标签的所有事件。  
另外vue还可以有事件修饰符，格式为`事件名.事件修饰符`，事件修饰符有以下几种：
- .stop ：阻止事件冒泡, 也就是当前元素发生事件,但当前元素的父元素不发生该事件
- .prevent ：阻止默认事件发生
- .capture ：使用事件捕获模式, 主动获取子元素发生事件, 把获取到的事件当自己的事件执行
- .self ：只有元素自身触发事件才执行。（冒泡或捕获的都不执行）
- .once ：只执行一次，这个时vue内置的，要想重置只能刷新页面或自定义条件刷新
写了这些，事件函数里就不用写对于的相关函数了
## v-if和v-show
- v-if与v-show可以根据条件的结果,来决定是否显示指定内容.
  - v-if: 条件不满足时, 元素不会存在.
  - v-show: 条件不满足时, 元素不会显示(但仍然存在).
以下是一个例子，包含了动态更新、v-if、ref（响应式）、全局变量注册的用法：
```js
//mian.js中
const app = createApp(App);
const clickCount = ref(0);  //创建一个响应式变量clickCount
app.config.globalProperties.$clickCount=clickCount;  //将clickCount注册为全局变量
```
```html
<template>
  <button @click="testIf()">点我</button><br>
  <input v-if='test=="1"'>{{test}}  <!-- v-if，满足要求显示元素input -->
<template>
<script>
import { defineComponent} from 'vue';

export default defineComponent({
  data() {
    return {
      test:""   //存储的数据，初值为空
    };},
    methods:{
    testIf(){
      this.$clickCount.value++;  //点击按钮，响应式变量clickCount加1，注意要访问到响应式变量的值需要.value。这里由于defineComponent函数不能在方法外定义变量(即类似java的实例变量)，所以用了全局变量。实际上setup函数可以在方法外定义变量，这个后面再说。
      console.log(this.$clickCount.value);
      if(this.$clickCount.value%2==0)
        this.test = "2";
      else
        this.test = "1";
    }
  }
});
</script>
```
## v-for
- 遍历非对象，格式为`v-for="(item,index) in iteams"`，也可以指只遍历iteam。   
- 遍历对象为`v-for="(value,key,index) in iteams"`，也可以指只遍历`(value,key)`或只遍历value。  
- `v-bind:key`或`:key`是遍历对象的唯一标识符，用于告诉Vue如何跟踪每个节点。如果不是对象，那可以是索引，如果数据又唯一性，也可以是数据本身（iteam）。如果是对象，同非对象，只不过对象一般会设置唯一标识符id，所以一般是id。  
以下是一个动态下拉框的例子：
```html
<template>
  <select>
      <option v-for="name in names" v-bind:key="name">{{name}}</option> <!-- 注意for在哪就在那遍历，我们要又多个option所以应该放在option而不是select -->
    </select><br>
</template> 
<script>
import { defineComponent} from 'vue';
export default defineComponent({
  data() {
    return {
      names:["刘文杰","冯杰伟","张俊伟"]
    };
  }})
```
## main.js
main.js是Vue应用的入口文件(根js文件)，用于创建Vue应用实例并挂载到指定的DOM元素上，vue3用的是createApp，格式为`const app = createApp(App);app.mount('#app')`，对于这个对象有以下常用方法：
- component：全局注册或获取组件。当传入两个参数时，用于全局注册组件；仅传入一个参数时，用于获取已注册的组件。
- use: 注册插件。用于注册Vue插件，例如Vue Router、Vuex等。
- provide：提供依赖注入。用于在组件树中提供依赖，使得子组件可以注入依赖。有两个参数，第一个参数是提供的依赖的键名，第二个参数是提供的依赖的值。然后在子组件中使用inject对象来注入依赖，可以通过键名来获取注入的值。
- config：全局配置。用于配置Vue应用的全局选项，例如全局指令、全局过滤器等,有以下常用配置
  - errorHandler：全局错误处理函数。用于捕获Vue应用中的错误，并进行相应的处理，有三个参数，第一个参数是错误对象，第二个参数是Vue实例，第三个参数是一个包含错误信息的字符串。可以用匿名函数来处理逻辑，**以下配置都可以**。
  - warnHandler：全局警告处理函数。参数同上
  - globalProperties：全局属性。用于添加全局属性，全局属性用要加上$前缀，然后可以在任何组件中用this.$全局属性名来访问。
  - isCustomElement：自定义元素检测函数。用于检测是否为自定义元素，有一个参数，即元素的标签名。
## app.vue:
app.vue是Vue应用的根组件，用于定义整个应用的结构和逻辑。它是整个应用的入口点，也是整个应用的根实例。一般可以在这里放首页模板，来管理整个应用的路由和页面跳转。
## computed、watch、props
### computed
computed是Vue3中新增的一个计算属性，用于根据已有数据计算出一个新的值。计算属性是基于已有数据进行计算得出的属性，其值会根据依赖的数据自动更新。与方法不同，计算属性是有缓存的，只有当依赖的数据发生变化时，才会重新计算。（用的vue2的例子，语法糖里其实就是一个方法）
```js
computed: {
    birth() {
      const date = new Date();
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    }
  },
```  
其实和data里的变量差不多，只是不能在data里定义，因为data里定义的变量是响应式的，而计算属性是根据已有数据计算得出的属性，其值会根据依赖的数据自动更新。 
### watch
watch是Vue3中新增的一个监听函数，用于监听数据的变化，并在数据变化时执行相应的操作。当需要在数据变化时执行异步操作或者复杂的逻辑时，使用watch比较合适。简单例子（语法糖里是`watch(对象名或数据名,(newValue, oldValue)=>{方法体})`来简化的）
```js
watch:{
      监听的数据名或对象名(newValue, oldValue) {
        console.log('对象发生变化', newValue, oldValue);
      },
  },
```
这里对象可以用`deep：true`来开启深度监听，*深度监听监听对象所有属性的变化，而浅度监听只监听对象本身的变化。*
### props
props是Vue3中新增的一个属性，用于在组件之间传递数据。父组件可以通过props向子组件传递数据，子组件可以接收并使用这些数据。（**数据包括属性和方法，下同**）
如下：
```html
<!-- 父组件 -->
<template>
  <MyTest2 :message="message"></MyTest2>  <!-- 这里用传递的数据就变成了自定义标签属性，这里只有在这里绑定了传递的数据子组件才能进行更改，否则只能获取值 -->
</template>
<script>
import { defineComponent } from 'vue';
export default defineComponent({
  data() {
    message:"nini"   //初始化数据，将其传递给子组件
    };
  },
  components:{
    MyTest2
  });
</script>
<!-- 子组件 -->
<template>
  <p>我是你爹:{{message}}</p>
</template>
<script>
export default defineComponent({
  props:{   //接收父组件的数据，用props来接收
    message:{
      type:String,  //类型
      required:true //是否一定需要
    }
  }
});
</script>
```
语法糖里是这样的：
```js
import { defineProps } from 'vue';
const props = defineProps({
  message:{
    type:String,
    required:true
  }
})//这样就可以了 但会提示未使用props，其实不用管，因为语法糖会自动使用props。可以用控制台输出一下去掉提示
```
这里能看出这是**父组件传递数据给子组件**。
## 自定义事件来传递数据
例如下：
```js
//子组件自定义事件并点击
export default defineComponent({
  emits: ['sentData'],   //定义事件名，参数可以是数组亦可以是对象，这里是数组
  //data里初始化chlidMessage，略
  methods: {   
    sendDataEvent() {   //让子组件绑定该事件，子组件触发
      this.$emit('sentData', this.childMessage);   //触发事件，参数1为事件名，参数2为传递的数据
    },}})
//然后是父组件接收
//首先在父组件的component（懒加载一般用）里或自定义子组件标签（直接显示的常用）里绑定自定义的事件，组件标签会在下面的路由配置一栏说明，用组件表前绑定事件如：<component :is="Form" v-if="Form" @sentData="handleSentData"></component>
//然后父组件是指函数接收
function handleSentData(data) { //接收数据，对应emit里的参数2
  childMessage = data;  
}
```
语法糖里的emits是由defineEmits代替，其他流程一样。
## defineExpose将子组件的方法暴露给父组件
```js
//这是语法糖的新概念
import { defineExpose } from 'vue';
// 子组件的方法
const childMethod = () => {
  console.log('子组件方法被调用');
};
// 暴露方法给父组件
defineExpose({
  childMethod
});  
//父组件接收
//先在自定义标签或组件标签里绑定ref属性，如`<ChildComponent ref="childComponentRef" />`
const childComponentRef = ref(null); // 创建对子组件的引用
childComponentRef.value.childMethod( // 父组件调用子组件的方法,这里是一个简单判断使用
 if (childRef.value) {
    childRef.value.childMethod();
  }
)
```
选项api如下
```js
//这时vue2里选项api的方法
//子组件只需定义方法即可
//同样先在自定义标签或组件标签里绑定ref属性，如`<ChildComponent ref="childRef" />`
//然后通过$refs访问子组件实例访问其属性和方法
callChildMethod() {
      // 通过 $refs 访问子组件实例并调用其方法
      this.$refs.childRef.childMethod();
    }
```
## provide 和 inject
provide和inject是Vue3中新增的两个API，用于在组件之间（***它可以跨层级传输，但注意只能向下传递***）传递数据。前面只是在createApp介绍了一下全局的，这里详细说明。这里为方便，就举一个父子组件的例子：
```js
//父组件提供数据
 provide() {
    return {
      //提供的数据，任意类型，类似data，注意要使用响应数据记得放data里
    }}
//子组件接收
inject: ['providedData'],  //接收/注入数据
```
语法糖里的provide是个函数，参数是数据名和值。inject也是函数，参数是接受的数据名。
# Composition API
## setup函数
setup函数是Vue3中新增的一个函数，组件中用到的数据，方法计算属性，事件方法，监听函数，都可以配置在setup中。即defineComponent的data、methods、computed、watch、生命周期钩子函数都可以配置在setup中，**但注意尽量不要和data、methods、computed、watch、生命周期钩子函数混用，因为setup无法访问data、methods等**。  
setup可以定义数据（这里直接let或其他）、方法（function）、return（返回对象），一般用于初始化数据，即在组件创建之前调用，故页 ***无法使用this***，需要用。另外对于简单的组件，可以只用setup，不用defineComponent。但复杂的组件，还是用defineComponent（因为提到不能混用）。
## setup语法糖
setup语法糖是Vue3中新增的一个语法糖，用于简化代码，直接定义属性、方法、计算属性、监听函数等。可以 ***更方便写setup函数或defineComponent。***
## 响应式
先说一下前端的响应式，**响应式是指当数据发生变化时，视图会自动更新。这个作用是为了让用户在不刷新页面的情况下，看到数据的变化，同时不刷新，也无需从数据库中获取数据，而是从缓存中获取数据，从而提高了用户体验。**
### ref
ref是Vue3中新增的一个函数，用于创建一个响应式的引用类型数据。ref函数接受一个参数，即初始值，返回一个包含value属性的对象。value属性是响应式的，当value属性发生变化时，视图会自动更新。它一般用于将基本类型数据处理成响应式数据。**注意需要要value对数据操作**（*注意那个是响应式数据，如`const nn = { name: 'num', value: 0 };const n = reactive(nn);`，这里nn不是，n才是，所以注意显示数据用n而不是nn*）
### reactive
和ref类似，只不过一班用于处理对象。另外，这个不是用value操作数据，而是用对象的key操作数据。
### toRef和toRefs
也一样，它是处理对象的单个属性或多个属性，toref接收对象(或reactive)和属性，而torefs接收多个属性组成的对象（或reactive）。它返回其实就是一个ref对象。
# 页面跳转（vue-router路由配置）
vue的跳转主要是通过路由实现的，它能在不刷新页面的情况下，实现页面的跳转。这时由于vue在页面加载时就已经将所有的组件加载到内存中了。
要使用vue-router，需要先安装vue-router（npm install @你的vue-router及版本），然后再进行以下：  
首先看怎么在mian.js中引入vue-router：
```js
import {createApp, ref} from 'vue'
import App from './App.vue'
import router from './router'; //写你放置路由的文件路径  
const app = createApp(App);
app.use(router)  //注册路由
app.mount('#app')
```
再看看路由配置：
```js
import { createRouter, createWebHistory } from 'vue-router';
import MyTest2 from '../components/MyTest2';
// 定义路由规则
const routes = [//路由对象
    {
        path: '/about', //路由路径  当路径的值前面有一个':'时，它表示这是一个动态路由参数，它会匹配任意值。如:id,它会匹配id为任意值的路由。当前路径最后有一个？时，它表示这是一个可选的路由参数，即可以匹配也可以不匹配。如:id?，它会匹配id为任意值的路由，也可以不匹配。这两个用于构建动态路由。

        name: 'About', //路由名称
        component: MyTest2 //引入的组件
        params:{ } //传递参数,如果需要的话
        redirect：'/about' //重定向，将一个路由重定向到另一个路由，可以是一个字符串或一个对象（即路由对象）。
        children：[ ] //子路由，用于定义嵌套的路由。子路由会被作为父路由的子组件进行渲染。
        meta：{ } //元信息，用于存储路由的额外信息，如标题、权限等。
        beforeEnter：(to, from, next) => { } //这是一个函数，路由守卫，用于在进入路由之前进行一些操作，如权限验证、数据加载等。to是目标路由对象，from是当前路由对象，next是一个函数，用于控制路由的导航行为，即判断是否继续导航。
        props：true //这是一个布尔值或函数，用于将路由参数传递给组件。如果为true，则会将路由参数作为组件的props进行传递。如果为函数，则可以自定义传递的参数。这用于动态路由参数的传递。
    }
];
// 创建路由实例
const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),  //创建一个基于浏览器历史记录的路由实例。process.env.BASE_URL是一个环境变量，用于指定应用的基础路径，这里是如果你用vue-cli创建的项目，会自动生成的。
    routes
});
export default router;
//vue.config.js中配置
//默认生成的略
publicPath: process.env.NODE_ENV === 'production'
      ? '/PersonalBlog/' // 生产环境下的基础路径，可按需修改
      : '/' // 开发环境下的基础路径
``` 
但注意最终只能在mian.js里引入一个路由，那么分开文件管理路由呢？**我们通过一个主路由文件来管理所有的子路由**，如下：
```js
//主路由文件
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'  //只有主路由文件需要引入这些
import index from '@/views/BlogIndex.vue' //引入组件

import indexRouter from '@/router/index'  //这里就是子路由文件的引入
import projectRouter from '@/router/project'

const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'home',
        component: index
    },
    ...indexRouter,    //这里就是子路由文件，及用...加上引入的子路由文件名
    ...projectRouter
]

const router = createRouter({   //这里也只有主路由文件有
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
//子路由文件
import myProjects from "@/components/project/MyProjects.vue"  //子路由只需要引入组件

export default [   //子路由只需要定义路由对象即可，然后导出
    {
        path: '/project',
        name: 'project',
        component: myProjects
    },
    {
        path: '/project/:first?/:second?/:third?/:fourth?/:fifth?/:sixth?/:seventh?',
        name: 'projectPath',
        component: myProjects
    }
]
```
最后可以再模板页面里使用，比如再app.vue里：
```html
<template>
  <div>
    <router-view></router-view>
  </div>
</template>
```
用`<router-view>`来渲染路由组件，当然也可以不用路由直接用`<组件名/>`标签来渲染组件，一般是二者混合使用。  
然后就可以在页面中使用`<router-link>`标签来页面跳转到路由（即router-view的内容）了，其又以下常用属性：
- :to：这时**必填**的属性，指定要跳转到的路由路径。可以是一个字符串或一个对象（即路由对象）。
- replace：是否使用替换模式进行导航。如果为true，则不会在浏览器历史记录中添加新的记录，而是替换当前记录。
- active-class：指定激活时的类名。当路由匹配成功时，对应的`<router-link>`元素会添加该类名。
- exact-active-class：指定精确匹配时的类名。当路由路径完全匹配成功时，对应的`<router-link>`元素会添加该类名。
还有很多属性再router4.x已经被弃用了，而vue3则对应引入了useRouter()对象，在方法里获取，从而在组件内部进行路由导航、访问路由信息等操作。而useRouter()有一些常用方法：(注意**userouter只能在setup里且在方法外定义才有效**)
- push：用于导航到指定的路由路径，可以是一个字符串或一个对象（即路由对象）。
- replace：用于替换当前路由路径，对应上面的repalce属性，属性里决定是否启用替换模式，这个指定替换的路由路径，启**用后替换了当前路由路径同时用户也无法通过浏览器的后退按钮返回上一个页面**。
- go：用于在浏览器历史记录中前进或后退指定的步数。  
但这里注意，**在methods里定义是无法通过useRouter()获取到的，它用this.$router来直接获取当前路由**。  
那其实就有个问题，当项目很大时就要加载所有组件就会导致性能问题，vue提供了懒加载来解决，即当路由被访问时才加载懒加载组件。实现就是在路由配置的组件属性：`component: () => import('组件位置')`，然后js里通过异步方法来加载组件，如下：
```html
<template>
  <button @click="loadDynamicComponent">加载动态组件</button>
  <!-- 使用动态组件渲染,用:is来绑定组件来更换到不同组件，v-if只是辅助来判断是否显示组件 -->
  <!-- 这里的componet标签是用来动态切换组建的，不止辅助懒加载，通过:is来实现 -->
  <component :is="dynamicComponent" v-if="dynamicComponent" />
</template>
<script setup> 
import { ref } from 'vue';
const dynamicComponent = ref(null); // 用于存储动态加载的组件,选项式不需要因为用data
const loadDynamicComponent = async () => {   // 异步加载组件的方法
//省略了data
  try {
    const { default: 组件名 } = await import('组件位置');// 动态加载组件
    dynamicComponent.value = 组件名;
  } catch (error) {
    console.error('加载组件出错:', error);
  }
};
</script>  
```
如果只是**跳转到单独页面**，无需异步方法，直接用上述的push或router-link标签即可。
# axios异步请求
axios异步请求是Vue3中常用的一个库，用于发送HTTP请求。它可以用于发送GET、POST、PUT、DELETE等请求，并且可以设置请求头、请求参数、响应拦截器等。vue2后作者就不再更新ajax了，因为作者推荐使用axios。axios是promise的封装。  
1. get请求
```js
axios.get(url,{
  Params{
    参数名：参数值
  },
  Headers{
    参数名：参数值
  }
  }).then(respone => { 
    //操作接收到的数据
  }).catch(err => {
    //操作错误信息
  });
```
这里处理操作数据有以下常用方法：
- data：获取服务器返回的响应数据。数据的格式取决于服务器的设置和请求的类型，可能是 JSON 数据、字符串、Blob 等。例如：const data = response.data;
- status：获取响应的 HTTP 状态码，如 200 表示成功，404 表示未找到，500 表示服务器内部错误等。例如：const statusCode = response.status;
- statusText：获取与状态码对应的状态文本，如 OK、Not Found、Internal Server Error 等。例如：const statusText = response.statusText;
- headers：获取响应头信息，是一个包含所有响应头字段的对象。例如：const headers = response.headers;
- config：获取用于生成此响应的请求配置对象，其中包含了请求的各种参数，如 url、method、headers、data 等。例如：const config = response.config;
2. post请求: 和get一样只不过传参不一样，post三个参数，就是把get的参数单独设置到了的第二个参数。
## 表单提交
例子：
```js
//template已经用v-model绑定了数据，这里略
import {reactive} from "vue";
import axios from "axios";
let formData = reactive({
  userId: 0,
  nikename: "",
  sex: "",
  phone: "",
  email: ""
});
function updateForm() {
  let form = new FormData();  //用表单数据来提交
  for(let key in formData){   
    form.append(key,formData[key]);
  }
  axios.post("http://localhost:8081/form/updateUserInfo", form, {
  })
//还可以用json提交
const form = JSON.stringify(formData);
axios.post("http://localhost:8081/form/updateUserInfo", form, {
  headers: {
    'Content-Type': 'application/json'   //这里注意，一定要设置请求头，因为默认是表单提交，所以要设置为json提交，同时后端也别忘了设置接收json数据@RequestBody，不然也默认接收表单数据
  }})
```
# 弹窗
实现弹窗可以用我上面懒加载组件方法，同时vue提供了一个 <teleport> 标签，使用很简单用该标签包围懒加载或其他形式实现弹窗的组件即可，还可搭配 <transition>标签（包围在teleport内弹窗组件外）实现弹窗的淡入淡出的动画效果。它和直接用组件标签的区别是，它不会影响页面的布局，而是将组件渲染到指定的位置。  
而实现渲染到指定位置用的是to属性，它的值可以是一个字符串或一个对象，就是渲染的位置，如：`<teleport to="body">`，这样就会将组件渲染到body标签内。  
使用<teleport>的好处是：
- 避免了组件的布局影响：使用<teleport>可以将组件渲染到指定的位置，而不会影响页面的布局。这在创建弹窗时非常实用，因为弹窗往往需要脱离当前组件的 DOM 结构，避免受到父元素样式（如 overflow: hidden）的限制，确保弹窗能在页面任意位置正常显示。
- 提高了组件的可维护性：使用<teleport>可以将组件的逻辑和样式与页面的其他部分解耦，使得组件的维护更加方便。
- 支持动态渲染：使用<teleport>可以动态地渲染组件到不同的位置，而不需要在每个位置都重新编写组件的代码。