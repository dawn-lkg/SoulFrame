# Base Admin 后台管理系统

<div align=\
center\>

**基于 Spring Boot 3.x + Vue 3.x 的现代化后台管理系统**

[在线演示](http://120.27.215.0:5888/home)

</div>

##  项目简介

Base Admin 是一个功能完善、架构清晰的企业级后台管理系统。采用前后端分离架构，基于 Spring Boot 3.x、Vue 3.x、MyBatis-Plus 等主流技术栈开发，提供用户管理、角色管理、菜单管理、权限控制等核心功能，适合作为企业级应用的基础框架。

##  核心特性

###  技术特性
- **现代化技术栈**: Spring Boot 3.x + Vue 3.x + JavaScript
- **微服务架构**: 模块化设计，支持独立部署和扩展
- **权限认证**: 基于 Sa-Token 的 RBAC 权限控制
- **接口文档**: 集成 Knife4j，自动生成 API 文档
- **数据持久化**: MyBatis-Plus + MySQL，支持多数据源
- **缓存支持**: Redis 缓存，提升系统性能
- **代码生成**: 支持数据库表自动生成 CRUD 代码

###  功能特性
-  **用户管理**: 用户增删改查、密码重置、状态管理
-  **角色管理**: 角色权限分配、角色继承
-  **菜单管理**: 动态菜单、权限控制、图标管理
-  **系统监控**: 操作日志、登录日志、系统信息
-  **系统配置**: 参数配置、字典管理
-  **文件管理**: 文件上传、下载、预览
-  **定时任务**: 任务调度、执行监控
-  **消息推送**: SSE 实时消息推送

##  技术栈

### 后端技术
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.0 | 基础框架 |
| Spring Security | 6.2.0 | 安全框架 |
| Sa-Token | 1.39.0 | 权限认证 |
| MyBatis-Plus | 3.5.5 | ORM 框架 |
| MySQL | 8.0+ | 数据库 |
| Redis | 6.0+ | 缓存 |
| Knife4j | 4.4.0 | API 文档 |
| Hutool | 5.8.26 | 工具类库 |

### 前端技术
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue.js | 3.5.13 | 前端框架 |
| Ant Design Vue | 4.2.6 | UI 组件库 |
| Vite | 6.3.5 | 构建工具 |
| Pinia | 3.0.2 | 状态管理 |
| Vue Router | 4.5.1 | 路由管理 |
| SCSS | 1.89.0 | CSS 预处理器 |
| ECharts | 5.6.0 | 图表库 |
| Axios | 1.9.0 | HTTP 客户端 |

##  项目结构

### 后端结构
\\\
base_admin/
 base_admin_common/          # 公共模块
    core/                   # 核心功能
       domain/             # 核心领域对象
       web/                # Web 核心配置
       utils/              # 核心工具类
    constants/              # 常量定义
    enums/                  # 枚举类
    exception/              # 异常处理
    security/               # 安全相关
    task/                   # 任务相关
    utils/                  # 工具类
 base_admin_system/          # 系统模块
    domain/                 # 领域对象
       entity/             # 实体类
       dto/                # 数据传输对象
       vo/                 # 视图对象
    service/                # 业务服务
       impl/               # 服务实现
       interface/          # 服务接口
    mapper/                 # 数据访问层
    enums/                  # 系统枚举
    constants/              # 系统常量
 base_admin_framework/       # 框架模块
    config/                 # 配置类
       security/           # 安全配置
       redis/              # Redis 配置
       database/           # 数据库配置
       swagger/            # API 文档配置
    aspect/                 # 切面编程
       log/                # 日志切面
       security/           # 安全切面
    interceptor/            # 拦截器
    handler/                # 处理器
    listener/               # 监听器
    annotation/             # 自定义注解
    exception/              # 异常处理
    web/                    # Web 相关
 base_admin_web/             # Web 模块
    controller/             # 控制器
       system/             # 系统管理控制器
       monitor/            # 监控控制器
       common/             # 通用控制器
    config/                 # Web 配置
       swagger/            # API 文档配置
       security/           # 安全配置
       cors/               # 跨域配置
    timerTask/              # 定时任务
 base_admin_quartz/          # 定时任务模块
    config/                 # 任务配置
       scheduler/          # 调度器配置
       job/                # 任务配置
    domain/                 # 任务领域对象
       entity/             # 任务实体
       dto/                # 任务 DTO
    service/                # 任务服务
       impl/               # 服务实现
       interface/          # 服务接口
    mapper/                 # 任务数据访问
    util/                   # 任务工具类
 base_admin_sse/             # 消息推送模块
    SseTemplate.java        # SSE 模板类
    SseConstant.java        # SSE 常量
    SseAutoConfiguration.java # SSE 自动配置
    SseProperties.java      # SSE 属性配置
 docs/                       # 项目文档
\\\

### 前端结构
\\\
base_admin/                     # 前端项目根目录
 src/                        # 源码目录
    api/                    # API 接口
       modules/            # 模块化接口
       index.js            # 接口配置
    assets/                 # 静态资源
    components/             # 公共组件
       common/             # 通用组件
       charts/             # 图表组件
       DictRadio/          # 字典单选组件
       DictSelect/         # 字典选择组件
       DictTag/            # 字典标签组件
       error/              # 错误页面组件
    config/                 # 配置文件
    layout/                 # 布局组件
       silderLayout/       # 侧边栏布局
       iframe/             # 内嵌页面布局
       common/             # 通用布局
    router/                 # 路由配置
    stores/                 # 状态管理
       auth/               # 认证状态
       app/                # 应用状态
       config/             # 配置状态
       dict/               # 字典状态
       theme/              # 主题状态
    styles/                 # 样式文件
       theme/              # 主题样式
       global.scss         # 全局样式
       variables.scss      # 样式变量
    utils/                  # 工具函数
    views/                  # 页面视图
       dashboard/          # 仪表盘
       login/              # 登录页面
       system/             # 系统管理
       monitor/            # 系统监控
       profile/            # 个人中心
       demo/               # 示例页面
    App.vue                 # 根组件
    main.js                 # 入口文件
 public/                     # 公共资源
 package.json                # 依赖配置
 vite.config.js              # Vite 配置
 index.html                  # HTML 模板
\\\

##  快速开始

### 环境要求
- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Node.js 16+
- Maven 3.6+

### 后端启动
\\\ash
# 1. 克隆项目
git clone https://github.com/dawn-lkg/SoulFrame

# 2. 导入数据库
mysql -u root -p < docs/sql/base_admin.sql

# 3. 修改配置
# 编辑 base_admin_web/src/main/resources/config/dev/mysql.yml
# 编辑 base_admin_web/src/main/resources/config/dev/redis.yml

# 4. 启动后端
mvn clean install
mvn spring-boot:run -pl :base_admin_web
\\\

### 前端启动
\\\ash
# 1. 进入前端目录
cd base_admin

# 2. 安装依赖
npm install

# 3. 启动开发服务器
npm run dev
\\\

### 访问地址
- 前端地址: [http://120.27.215.0:5888/home](http://120.27.215.0:5888/home)
- 后端地址: http://localhost:8080
- API 文档: http://localhost:8080/doc.html
- 默认账号: admin / 123456

