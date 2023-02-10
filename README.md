# erp_pro

> 基于SpringBoot 2.X框架，为中小企业打造开源好用的ERP软件。主要模块有零售管理、采购管理、销售管理、仓库管理、财务管理、报表查询、系统管理等。支持预付款、收入支出、仓库调拨、组装拆卸、订单，生产等特色功能。拥有库存状况、出入库统计等报表。对权限进行精确划分，同时支持多系统集成方案，可与OA，CRM，知识库等多个系统进行集成使用。同时对角色和权限进行了细致全面控制，精确到每个按钮和菜单。集成Nacos配置中心、服务注册中心。

-  **作者目前想找一份远程办公的工作，有需求的金主可以加我私聊，添加时请备注：远程** 
- [2023年全年更新计划](https://mp.weixin.qq.com/s/deBkHLLeo1JDy6nqhvtWZg)
- [软件更新资讯](https://gitee.com/doc_wei01/skyeye/blob/company_server/HISTORY_UPDATE.md)
- 开源版请下载`master`分支
- 企业版当前版本：202301版
- 作者本人承诺，知识星球人数达到1000人，即开放所有功能模块源代码(仅供星球内部成员使用)
- [开源版项目文档](https://gitee.com/doc_wei01/skyeye/blob/company_server/%E9%A1%B9%E7%9B%AE%E6%96%87%E6%A1%A3.md)
- [企业版项目文档](https://gitee.com/doc_wei01/skyeye/blob/company_server/%E4%BC%81%E4%B8%9A%E7%89%88%E9%A1%B9%E7%9B%AE%E6%96%87%E6%A1%A3.md)
- 基础模块 (源代码) 针对星球内部用户开放，具体包含功能可查看：[https://docs.qq.com/sheet/DYWZWUWZQUkFTcmh2?tab=BB08J2](https://docs.qq.com/sheet/DYWZWUWZQUkFTcmh2?tab=BB08J2)
- 此软件为个人软件，暂不能开发票，需要发票的勿扰，企业版报价：https://docs.qq.com/sheet/DYUh5RUlGSkVPeHVo

### 沟通交流

| 企业部署版 | 作者微信(添加时请备注个人信息：姓名-联系方式)，其余一律不通过 |  QQ群    |  微信公众号(文档类资料) |  微信小程序(操作视频/介绍视频等) |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/mindMap/知识星球.png)  | ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/mindMap/微信.jpg)  | ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/mindMap/Skyeye智能制造云办公官方①群群二维码.png)  | ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/mindMap/微信公众号.jpg)  | ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/mindMap/Skyeye视频微信小程序.jpg) |


#### 环境搭建

> 该项目是maven工程，如果遇到jar包没下载下来的问题，请更换镜像地址.

- 1.搭建apollo配置中心，将`docs/配置中心参数.md`复制到配置中心
- 2.搭建redis（3.2）集群
- 3.MySQL 5.5.X
- 4.最后修改配置中心参数，启动`SkyeyeErpApplication.java`项目

#### 开源版功能介绍

| 功能        | 描述                                    | 功能     | 描述                             |
|-----------|---------------------------------------|--------|--------------------------------|
| 用户管理      | 用户是系统操作者，该功能主要完成系统用户配置                | 部门管理   | 配置系统组织机构（公司、部门、小组），树结构展现支持数据权限 |
| 岗位管理      | 配置系统用户所属担任职务                          | 菜单管理   | 配置系统菜单，操作权限，按钮权限标识等            |
| 角色管理      | 角色菜单权限分配、设置角色按机构进行数据范围权限划分            | 基础设置   | 包含背景图片设置、锁屏图片设置等               |
| 计量单位      | 产品规格单位管理                              | 结算账户   | 自定义账户的结算方式以及结算明细的查看            |
| 收支(科目)项目 | 自定义除了采购/销售等产生的费用外，其他产生费用的项目           | 基本资料   | 包含会员管理、供应商管理,商业版客户已抽离为CRM      |
| 采购管理      | 包含采购单、转入库单、采购入库、采购退货等功能，整改完成          | 销售管理   | 包含销售单、转出库单、销售出库、销售退货等功能，整改完成   |
| 其他入库管理    | 整改完成                                  | 库存盘点   | 对现有仓库的库存进行盘点                   |
| 零售管理      | 包含零售出库、零售退货等功能，整改完成                   | 拆分单    | 整改完成                           |
| 调拨单       | 整改完成                                  | 仓库管理   | 管理用户所拥有的仓库,整改完成                |
| 商品管理      | 管理用户所拥有的产品信息,整改完成                     | 其他单据管理 | 包含其他入库、其他出库、调拨、组装单、拆分单等功能      |
| 报表管理      | 包含入库/出库明细、入库/出库汇总、库存状况、客户/供应商/会员对账等报表 | 财务管理   | 收入单、支出单、收付款单、转账单等              |
| 库存管理      | 对比开源版新增库存管理，可以查看每个商品不同规格的库存数          |    |                   |


# 企业版功能矩阵

系统后台集成了主流的通用功能，如：登录验证、系统配置、角色权限、组织管理、功能菜单、模块管理、数据字典、审批流程、员工管理、消息通知、企业公告、知识文章、办公审批、日常办公、财务管理、API接口等。可以基于当前系统的企业版便捷做二次开发。

![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/mindMap/Skyeye智能制造云办公.png)

#### 技术选型

##### 后端技术:

技术|名称|版本
---|---|---
[SpringBoot](http://spring.io/projects/spring-boot)|核心框架|2.0.3
[MyBatis](http://www.mybatis.org/mybatis-3/zh/index.html)|ORM框架
[Maven](http://maven.apache.org/)|项目构建管理|
[redis](https://redis.io/)|key-value存储系统|3.2集群（不要问我单机的能不能行）
[webSocket](http://www.runoob.com/html/html5-websocket.html)|浏览器与服务器全双工(full-duplex)通信|
xxl-job|定时任务|https://gitee.com/xuxueli0323/xxl-job?_from=gitee_search/
[RocketMQ](https://rocketmq.apache.org/dowloading/releases/)|消息队列|
[Java]()|Java|1.8
[MySQL]()|数据库|5.5.28

##### 前端技术：

|技术|名称| 官网                                       |
|---|---|------------------------------------------|
|layui|模块化前端UI| https://www.layui.com/                   |
|winui|win10风格UI| https://gitee.com/doc_wei01_admin/skyeye |

#### 效果图

| 效果图                                    | 效果图                                |
|----------------------------------------|------------------------------------|
| ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/tradition/show001.png) | ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/win10/show001.png) |
| ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/tradition/show002.png) | ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/win10/show002.png) |
| ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/tradition/show003.png) | ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/win10/show003.png) |
| ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/tradition/show004.png) | ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/win10/show004.png) |
| ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/tradition/show005.png) | ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/win10/show005.png) |
| ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/tradition/show006.png) | ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/win10/show006.png) |
| ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/tradition/show007.png) | ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/win10/show007.png) |
| ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/tradition/show008.png) | ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/win10/show008.png) |
| ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/tradition/show009.png) | ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/win10/show009.png) |
| ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/tradition/show010.png) | ![](https://gitee.com/doc_wei01/skyeye/raw/company_server/images/show/win10/show010.png) |
