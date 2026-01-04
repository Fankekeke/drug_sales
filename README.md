### 基于SpringBoot + Vue的药品销售系统

电子处方、药品进销存、药店管理平台，药品销售系统

### 管理员
公告管理，药品管理，库存统计，物流信息，订单详情，订单评价，订单信息，缴费记录，药店管理，药店库存，员工管理，用户管理，销售排行，库存预警，电子处方管理，供应商管理，药品采购->供应商，药品订单物品，销售统计

### 用户
个人信息，我的订单，缴费记录，订单评价，药品购买，添加电子处方

##### 管理员功能模块介绍：
###### 公告管理：发布、编辑和删除系统公告，及时传达重要信息。
###### 药品管理：维护药品基本信息，包括名称、规格、价格等数据。
###### 库存统计：汇总并分析当前药品库存数量与分布情况。
###### 物流信息：跟踪和管理药品配送的物流状态与运输记录。
###### 订单详情：查看每笔订单的具体商品、数量、金额及状态。
###### 订单评价：浏览用户对药品及服务的评价内容与评分。
###### 订单信息：统一管理所有用户提交的购药订单记录。
###### 缴费记录：查询用户支付订单的详细缴费时间与方式。
###### 药店管理：维护合作药店的基本资料与营业状态信息。
###### 药店库存：监控各药店实时药品库存，支持调拨建议。
###### 员工管理：管理后台员工账号、权限及工作分配情况。
###### 用户管理：查看和维护注册用户账户信息与使用状态。
###### 销售排行：统计热销药品或药店的销售排名数据。
###### 库存预警：自动提示库存低于安全阈值的药品信息。
###### 电子处方管理：审核、存储和管理用户上传的电子处方。
###### 供应商管理：维护药品供应商资料及合作状态信息。
###### 药品采购->供应商：向指定供应商发起药品采购申请流程。
###### 药品订单物品：管理采购或销售订单中具体的药品明细。
###### 销售统计：生成按日、月、年维度的销售数据分析报表。

##### 用户功能模块介绍：
###### 个人信息：查看和编辑个人资料、联系方式等账户信息。
###### 我的订单：查询历史购药订单状态及物流进度详情。
###### 缴费记录：查看已完成支付的订单费用与支付凭证。
###### 订单评价：对已收货的药品和服务进行评分与留言。
###### 药品购买：浏览药品目录，在线选购所需药品并下单。
###### 添加电子处方：上传医生开具的电子处方用于合规购药。

#### 安装环境

JAVA 环境 

Node.js环境 [https://nodejs.org/en/] 选择14.17

Yarn 打开cmd， 输入npm install -g yarn !!!必须安装完毕nodejs 

Mysql 数据库 [https://blog.csdn.net/qq_40303031/article/details/88935262] 一定要把账户和密码记住

redis 

Idea 编译器 [https://blog.csdn.net/weixin_44505194/article/details/104452880]

WebStorm OR VScode 编译器 [https://www.jianshu.com/p/d63b5bae9dff]

#### 采用技术及功能

后端：SpringBoot、MybatisPlus、MySQL、Redis、
前端：Vue、Apex、Antd、Axios、baidu.js


平台后台：springboot(框架) + redis(缓存中间件) + shiro(权限中间件) + mybatisplus(orm) + restful风格接口 + mysql(数据库)

开发环境：windows10 or windows7 ， vscode or webstorm ， idea + lambok

支付使用阿里沙盒支付

#### 前台启动方式

安装所需文件 yarn install 
运行 yarn run dev

#### 后端启动方式

1.首先启动redis，进入redis目录终端。输入redis-server回车
2.导入sql文件，修改数据库与redis连接配置
3.idea中启动后端项目

#### 默认后台账户密码

[管理员]
admin
1234qwer

[用户]
fank
1234qwer

#### 项目截图

|  |  |
|---------------------|---------------------|
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1683864138195.jpg) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1683864008853.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1683864108403.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1683863994707.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1683864092021.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1683863982591.jpg) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1683864074951.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1683864244337.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1683864061554.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1683864226438.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1683864052387.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1683864205060.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1683864035131.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1683864162642.png) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1683864020608.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/work/936e9baf53eb9a217af4f89c616dc19.png) |

#### 演示视频

暂无

#### 获取方式

Email: fan1ke2ke@gmail.com

WeChat: `Storm_Berserker`

`附带部署与讲解服务，因为要恰饭资源非免费，伸手党勿扰，谢谢理解😭`

> 1.项目纯原创，不做二手贩子 2.一次购买终身有效 3.项目讲解持续到答辩结束 4.非常负责的答辩指导 5.**黑奴价格**

> 项目部署调试不好包退！功能逻辑没讲明白包退！

#### 其它资源

[2025年-答辩顺利通过-客户评价🍜](https://berserker287.github.io/2025/06/18/2025%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2024年-答辩顺利通过-客户评价👻](https://berserker287.github.io/2024/06/06/2024%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2023年-答辩顺利通过-客户评价🐢](https://berserker287.github.io/2023/06/14/2023%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2022年-答辩通过率100%-客户评价🐣](https://berserker287.github.io/2022/05/25/%E9%A1%B9%E7%9B%AE%E4%BA%A4%E6%98%93%E8%AE%B0%E5%BD%95/)

[毕业答辩导师提问的高频问题](https://berserker287.github.io/2023/06/13/%E6%AF%95%E4%B8%9A%E7%AD%94%E8%BE%A9%E5%AF%BC%E5%B8%88%E6%8F%90%E9%97%AE%E7%9A%84%E9%AB%98%E9%A2%91%E9%97%AE%E9%A2%98/)

[50个高频答辩问题-技术篇](https://berserker287.github.io/2023/06/13/50%E4%B8%AA%E9%AB%98%E9%A2%91%E7%AD%94%E8%BE%A9%E9%97%AE%E9%A2%98-%E6%8A%80%E6%9C%AF%E7%AF%87/)

[计算机毕设答辩时都会问到哪些问题？](https://www.zhihu.com/question/31020988)

[计算机专业毕业答辩小tips](https://zhuanlan.zhihu.com/p/145911029)


#### 接JAVAWEB毕设，纯原创，价格公道，诚信第一

`网站建设、小程序、H5、APP、各种系统 选题+开题报告+任务书+程序定制+安装调试+项目讲解+论文+答辩PPT`

More info: [悲伤的橘子树](https://berserker287.github.io/)

<p><img align="center" src="https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/%E5%90%88%E4%BD%9C%E7%89%A9%E6%96%99%E6%A0%B7%E5%BC%8F%20(3).png" alt="fankekeke" /></p>
