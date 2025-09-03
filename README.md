### 基于SpringBoot + Vue的药品销售系统

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

### 管理员
公告管理，药品管理，库存统计，物流信息，订单详情，订单评价，订单信息，缴费记录，药店管理，药店库存，员工管理，用户管理，销售排行，库存预警，电子处方管理，供应商管理，药品采购->供应商，药品订单物品，销售统计

### 用户
个人信息，我的订单，缴费记录，订单评价，药品购买，添加电子处方

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

> 1.项目纯原创，不做二手贩子 2.一次购买终身有效 3.项目讲解持续到答辩结束 4.非常负责的答辩指导 5.黑奴价格

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
