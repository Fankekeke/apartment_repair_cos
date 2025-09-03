### 基于SpringBoot + Vue的公寓报修系统.

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
前端：Vue、Apex、Antd、Axios

平台前端：vue(框架) + vuex(全局缓存) + rue-router(路由) + axios(请求插件) + apex(图表)  + antd-ui(ui组件)

平台后台：springboot(框架) + redis(缓存中间件) + shiro(权限中间件) + mybatisplus(orm) + restful风格接口 + mysql(数据库)

开发环境：windows10 or windows7 ， vscode or webstorm ， idea + lambok


#### 前台启动方式
安装所需文件 yarn install 
运行 yarn run dev

#### 默认后台账户密码
[管理员]
admin
1234qwer

###### 管理员：
楼宇管理 、公告管理 、出入库记录 、设备管理 、申请记录 、房屋管理 、维修工单 、设施检查 、业主管理 、采购申请 、库房管理 、出库记录 、入库记录 、物品类型 、员工管理 、投诉管理 、工单评价 、库房采购、采购入库、出库审核、数据统计。

###### 业主：
账户注册、账户登录、密码修改、我的房屋 、维修工单 、工单评价 、投诉记录 、在线支付 、业主信息、房屋看板。

###### 维修工：
账户登录、密码修改、个人信息 、投诉记录 、工单评价 、我的工单 、设施巡检。

#### 项目截图
暂无

|  |  |
|---------------------|---------------------|
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/0fb3c2d1-64bd-411f-828f-2ef4c68b47a1.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/977a8012-6661-4f3f-82a9-e335b0af4f12.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/0d09c3a7-1663-4c23-9ac3-cb32a8a69d36.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/959c86bb-b449-48fa-820f-6a58fd16084f.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/fef5e18d-bd9c-4511-aff7-26eafaac089a.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/612a1385-dd7f-4210-82b6-8cc4c21da2bd.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/f0ac229f-20e9-41e8-b026-250563629f63.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/134b7fb0-f0d7-4fdc-864e-9110b7a79610.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/e46273e6-6186-4bec-a750-9d7631f8a17a.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/78b9c655-18a2-471b-9539-cba1815e212a.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/c1969147-e65b-40b0-8b0a-9067064322b6.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/71ab101a-5f9b-4a7b-a154-de33055d9a1d.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/c7e4ac60-26f5-4b5c-ba05-0e82734479fe.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/20a8687f-8ae6-4f7e-acc9-4210f385eac2.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/c7c2a707-9382-496e-a4e1-2a171ce4d035.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/6a55c94f-d6e4-47d8-a1c9-9f7e18795aea.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/b6245db9-19e4-4fb7-8274-bab5d3db72b1.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/4fed4860-a533-4f4c-a6bd-c24aab0d6d9a.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/59339489-ecfe-4c44-ac23-aeee142642e1.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/3ca50b51-b3cd-4cce-a294-9bda18b5337b.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/170115f9-b1a0-4e30-9af4-4e9cb4435883.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/3ac07d01-9c42-446c-8ebb-4354509cae4e.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/13303f71-b81d-49a9-8d3d-d15852634b89.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/3ac2de77-7f45-405b-bac6-988bf2db62fc.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/03794a54-6f93-4890-9855-515e19d30fcd.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/work/936e9baf53eb9a217af4f89c616dc19.png) |

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
