# Share Movie

> 2019企业实训课程项目筛影网 - ShareMovie
>
> 项目成员：
>
> * [@NicerWang](https://github.com/NicerWang)
> * [@Tumbledore](https://github.com/TumbledoreAlalalala)
> * [@QingFeng](https://github.com/lihangyu1913092)
> * [@吕尤](https://github.com/lvyou2000)

首页效果：

![Homepage](./homepage.png)

项目共包含6个模块，以下分别介绍：

## Backend

> 基于`servlet`

* 用户管理
  * 注册、登陆、注销(`Session`认证)
  * 个人信息更新
* 权限管理
* 电影数据管理
  * 获取电影列表
  * 获取电影详细信息
    * 详细信息包含：评分变动、评分分布、评论词云、每日评分量
* 提供管理员功能

## Frontend

> 基于`vue3`+`vuex`+`vue-router`

* 基本的用户管理功能

* 基于`echart`的数据展示

  * 评分变动、评分分布、评论词云、每日评分量

* 较为克制的动画效果

* 管理员功能(需要使用管理员用户登陆，并手动访问`/admin`)

  用户管理、电影管理、**自动化**、推荐算法结果展示

## MapReduce

> 需要使用一个`Hadoop`集群，`MapReduce`在集群中运行
>
> 本项目配置了`NameNode` + 2 x `DataNode`

* `PurifyData`

  处理所有评分数据，得到评分分布及评分随时间的变化，并写入数据库。

* `WordCloud `

  处理所有影评内容，使用HanLP进行分词，过滤无效词，并写入数据库为生成词云做准备。

* 支持数据更新。

## Automate Adapter

> 用于上述自动化功能。

`Backend`从`Frontend`接受请求，添加`Task`，并告知`TaskManager`向`Automate Adapter`传递信息。

`Automate Adapter`从后端接受请求(含有电影ID信息、数据库信息等)，启动爬虫，爬取所有数据。

爬取结束后，自动将图片上传到腾讯云COS，向后端返回影片信息而后自动执行`MapReduce`，写入数据库后更新任务信息。

## Spider

> 使用`python` + [`proxy pool`](https://github.com/jhao104/proxy_pool)实现
>
> 爬取目标为：`douban.com`

* `comment.py`

  爬取评论内容。

* `movieInfo.py`

  爬取电影信息。

* 二者均需要命令行参数：

  * 参数1：电影`ID`
  * 参数2：开始时的时间戳（毫秒级，用于数据更新，**可选**）

* **配置时，需首先设定`util/proxy_util.py`中的`url`为代理池地址**

## Recommend Algorithm(未完成)

1. 数据预处理

   1. 爬取的豆瓣电影`Top60`的影评信息。
   2. 此时用户-电影的评分矩阵过于稀疏，所以筛选了发布影评较活跃的用户(共1026位)，构建新的用户-电影评分矩阵。
   3. 根据电影的信息，使用jieba进行分词，去除无效信息，根据分词结果的相似度，确定电影的相似度(文本相似度分析)。

2. 推荐算法

   基于**随机游走**算法，针对每位用户推荐10部电影(排除该用户已评分电影)。

**时间仓促，本部分未完善，仅作为一个额外的`Feature`出现。**

## 关于部署

* `Frontend`

  无需处理。

* `Backend`

  使用`database`中的`sql`文件建立`bigdata`数据库，并在`resources/db.properties`填写正确的信息。

* `Automate Adapter`

  * **需要`Hadoop`集群，部署在`master`上（`jar`包）。**

  * 需要在`automate/UploadUtil.java`中设置正确的腾讯云COS密钥。

  * 需要保证`5901`端口可用。

  * 需要在`automate/UpdateTaskInfo.java`中设置正确的数据库信息。

* `MapReduce`

  需要打成`jar`包，在`master`上形成如下结构：

  ```
  automate.jar
  purifydata.jar
  wordcloud.jar
  python(Spider中的内容)
  ->comment.py
  ->movieInfo.py
  ->...
  ```

  启动`Hadoop`，而后使用`nohup`启动`automate.jar`。
