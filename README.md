# 网页版GitHub基本使用：网页上在线改代码略
 # 说明
 该仓库是学习过程中重要的例子，以下是一些无关仓库但很重要知识
 
 ## 网页版GitHub基本使用：网页上在线改代码略
 1. 本地上传文件到仓库，先要在GitGub建立一个远程仓库
 2. 然后本地建立一个文件夹，cd到该文件夹，将github远程仓库克隆到该文件夹来作为本地仓库：git clone “ssh地址或http地址”
 3. 然后添加这个远程仓库：`git remote add 远程仓库名（为好记可以设置和github一样）`   查看git已经加入的远程仓库名：`git remote `改名：`git remote rename 原本的名 新名字`    `删除：git remote remove 仓库名`   等等
 4. 然后将本地文件拖进来，指令添加该文件到缓存区：git add 文件名（这里写.表示所有文件）,如果想将某个文件从缓存区删除：`git rm --cached 文件名` 删除或`git restore --staged`文件名恢复到未存状态
 5. 提交申请并注释说明：git commit -m "提交说明内容" ,如果想撤销修改，可以使用`git reset --hard HEAD^` ，`HEAD^`表示上一个版本，`HEAD^^`表示上上个版本，以此类推，也可以使用`git reset --hard` 版本号，或者指定版本号，版本号可通过`git log`查看。
 6. 然后就可以上传到远程仓库了，但这里注意如果是多人协作需要先拉取远程仓库最新内容，修改是否有冲突的地方再进行上传，如果是自己的个人仓库，则直接上传：`git push 远程仓库名 分支名`
 7. 如果是多人协作，先拉取仓库最新内容：`git pull 远程仓库名 分支名`
 2. 然后本地建立一个文件夹，cd到该文件夹，将github远程仓库克隆到该文件夹来作为本地仓库：**git clone “ssh地址或http地址”**
 3. 然后添加这个远程仓库：**git remote add 远程仓库名（为好记可以设置和github一样）**   
    查看git已经加入的远程仓库名：**git remote** 
    改名：**git remote rename 原本的名 新名字**   
    删除：**git remote remove 仓库名**   等等
 4. 然后将本地文件拖进来，指令添加该文件到缓存区：**git add 文件名（这里写.表示所有文件）**
 5. 提交申请并注释说明：**git commit -m "提交说明内容"** 
 6. 然后就可以上传到远程仓库了，但这里注意如果是多人协作需要先拉取远程仓库最新内容，修改是否有冲突的地方再进行上传，如果是自己的个人仓库，则直接上传：**git push 远程仓库名 分支名**
 7. 如果是多人协作，先拉取仓库最新内容：**git pull 远程仓库名 分支名**
 8. 这里的分支可在github创建，所有分支是main节点的拓展延伸，也就是比main节点内容多，待分支写完，需要合并到main节点（即最初main节点和分支合并为新main节点）就需要在GitHub发起i一个拉取请求，个人仓库自己确认拉取请求是否确认合并，多人就是管理者确认。（合并无明显冲突，git都会自动合并，否则你只能解决冲突或对于个人仓库可在本地自行合并在直接上传至main节点）
 
 
 # 对于用ssh密匙绑定git和类似github的仓库（以github为例）：
 ssh-keygen -t rsa -C “git账号”
 
 # 关于markdown基本语言：
 1. ”#“代表标题，几个”#“代表几级标题
 ## 对于用ssh密匙绑定git和类似github的仓库（以github为例）：
 1. **ssh-keygen -t rsa -C "git账号"**  
    这里如果电脑用户文件夹有中文，可能会出错，可以手动在用户文件夹下新建一个.ssh文件夹，然后再输入命令
 2. 然后打开命令生成的文件，复制公匙内容（带pub的是公钥，不带的是私钥，私钥不能给别人看，这里复制的公钥），然后在github上新建一个ssh密匙，将公匙内容粘贴进去，然后输入一个名字，保存即可
 3. **ssh -T git@github.com** (这里可与加-p指定端口号 默认22端口)
    这里如果出现提示输入yes/no，输入yes即可，出现successfully，说明绑定成功
    但ssh可能会连接失败，这和电脑是否有代理有关，需要将电脑的代理关闭即可，如果还没解决，可以把端口改为443端口即可，但这样会导致速度变慢。如果想指定端口为443，可在.ssh下建立一个config文件，内容为：
    ```
    Host github.com
     Hostname ssh.github.com
     User git
     Port 443
 4. 然后可以设置git的默认用户名和邮箱：
    **git config --global user.name "用户名"**
    **git config --global user.email "邮箱"**
    最好和github账号绑定的邮箱一致，邮箱是必须一致
 5. 具体可以看这边博客的ssh密匙部分：[CSDN](https://blog.csdn.net/m0_57787115/article/details/130296388)
    
 ## 关于markdown基本语言：
 - #代表标题，几个#代表几级标题
 - 无序列表：使用 -、+ 或 * 开头，后面紧跟一个空格
 - 有序列表：使用数字加 . 开头，后面紧跟一个空格
 - 斜体：使用一个 * 或 _ 包裹文本，示例：*这是斜体文本* 或 _这是斜体文本_。
   粗体：使用两个 * 或 _ 包裹文本，示例：**这是粗体文本** 或 __这是粗体文本__。
   粗斜体：使用三个 * 或 _ 包裹文本，示例：***这是粗斜体文本*** 或 ___这是粗斜体文本___
 - 链接：使用 [链接文本](链接地址) 的格式创建链接，示例：[百度](https://www.baidu.com)
 - 使用 ![图片描述](图片地址) 的格式插入图片，示例：![GitHub Logo](https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png)
 - 行内代码：使用单个反引号包裹代码，示例： `print('Hello, World!')`  
   代码块：使用三个反引号 ``` 包裹代码块，还可以指定代码的语言，示例：
   ```python
     def add(a, b):
     return a + b
 
 # 该仓库是学习过程中重要的例子
     result = add(3, 5)
     print(result)
 - 使用三个或以上的 *、- 或 _ 来创建分割线