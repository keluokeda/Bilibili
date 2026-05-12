# CLAUDE.md

本文件为 Claude Code（claude.ai/code）在此仓库中工作时提供指引。

## 项目概述

这是一个 Bilibili 视频流 Android 应用，包含 TV（`tv`）和手机（`app`）两个独立模块。**TV 模块是主应用**
（v1.2.7），面向 Android TV 设备，使用 Compose TV Material Design。手机模块是辅助变体，使用标准 Material
3。

## 构建命令

```bash
# 构建 TV 应用（主应用）
./gradlew :tv:assembleDebug

# 构建手机应用
./gradlew :app:assembleDebug

# 构建所有模块
./gradlew build

# 运行单元测试
./gradlew test

# 运行指定模块的测试
./gradlew :tv:test
./gradlew :viewModel:test
```

关键构建配置：AGP 9.1.0、Kotlin 2.2.10、Compile SDK 35、JVM 目标 Java 11。

## 模块架构

| 模块           | 职责                                                                                       |
|--------------|------------------------------------------------------------------------------------------|
| `api`        | Retrofit 接口（`BilibiliApi.kt`）及所有响应数据类                                                    |
| `common`     | OkHttp/Retrofit 配置、`BilibiliRepository` 接口、`Screen` 导航路由、`BilibiliStorage` 设置接口          |
| `db`         | Room 数据库——内存数据库（`BilibiliDatabase`）和持久化数据库（`BilibiliPersistentDatabase`）及 DAO            |
| `repository` | `BilibiliRepositoryImpl` 及 Paging 3 数据源（`SearchPagingSource`、`CommentsRemoteMediator` 等） |
| `viewModel`  | 共享的 `BaseViewModel<State, Action, Event>` 基类，以及 tv 和 app 共用的所有 ViewModel                 |
| `tv`         | TV 端 UI 页面（Compose）、`MainActivity`、TV 专属 ViewModel                                       |
| `app`        | 手机端 UI 页面（Compose）及手机端入口                                                                 |

## 架构模式

整体采用 **MVVM + Repository + MVI**：

- **ViewModel 上的 MVI**：所有 ViewModel 继承 `BaseViewModel<State, Action, Event>`。UI 状态通过
  `MutableStateFlow` 流转，一次性事件通过 Kotlin Channel 传递。ViewModel 均为 `@HiltViewModel`。
- **Repository 接口在 `common` 中定义**：`BilibiliRepository` 声明所有数据操作，实现位于 `repository`
  模块。这种依赖倒置使 tv/app 模块只需依赖 `common`。
- **导航**：类型安全路由定义为 `common/src/main/java/com/ke/biliblli/common/Screen.kt` 中的密封接口
  `Screen`。`NavHost` 在 `MainActivity` 中配置，路由为：Splash → Login → Main → VideoDetail →
  Comments → UserDetail → Search。
- **依赖注入**：全链路使用 Hilt。`MainApplication` 标注 `@HiltAndroidApp`，Activity/Fragment 标注
  `@AndroidEntryPoint`。
- **分页**：所有分页列表使用 Paging 3。`CommentsRemoteMediator` 将评论缓存至 Room，其他分页源为纯网络请求。

## HTTP / API 层

- OkHttp 客户端配置了 `PersistentCookieJar`（会话 Cookie 持久化）、`BilibiliHttpInterceptor`（添加
  Bilibili 所需请求头：env、app-key、Aurora zone、User-Agent）以及 HTTP 日志拦截器。
- 两个 Retrofit 实例：一个用于 JSON（`KotlinxSerializationConverterFactory`），一个用于 Protobuf（弹幕
  API，通过 `BilibiliProtoApi`）。
- **WBI 签名**：`BilibiliRepositoryImpl` 实现了 Bilibili 的 WBI 请求签名算法，所有需要鉴权的 API
  调用都必须经过此签名流程。

## 关键约定

- 页面以 `*Route` 命名 Compose 入口（如 `VideoDetailRoute`），以 `*Screen` 命名无状态 Composable。
- 共享的 `viewModel` 模块包含 tv 和 app **共用**的 ViewModel；TV 专属 ViewModel 位于
  `tv/src/main/java/com/ke/bilibili/tv/viewmodel/`。
- `BilibiliStorage`（接口定义在 `common`）管理用户偏好设置（弹幕速度/密度/字号、视频分辨率、直接播放设置），实现通过
  Hilt 注入。
- 主 Tab 切换的跨组件通信使用 EventBus。

## 主要依赖

- **UI**：Jetpack Compose + `androidx.tv.material3`（TV 模块）/ `androidx.compose.material3`（手机模块）
- **视频**：Media3/ExoPlayer 1.8.0（tv）、1.6.0（app）
- **图片**：Coil（`io.coil-kt:coil-compose`）
- **序列化**：KotlinX Serialization（JSON）+ Protobuf（弹幕）
- **鉴权**：ZXing-Lite 扫码登录；PersistentCookieJar 管理 Cookie 会话
- **监控**：Firebase Crashlytics + Analytics

## API 参考

本项目参考的 Bilibili 非官方 API 文档：https://github.com/SocialSisterYi/bilibili-API-collect