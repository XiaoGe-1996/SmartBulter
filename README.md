# 每日一逛
一款适合新手的简单app，实现了科大讯飞TTS机器人语音聊天，微信文章精选，美女图片，Bmob个人用户，百度地图定位sdk，拍照上传，以及二维码扫描，二维码生成等功能.

## 技术支持
* 使用RxVolley访问网络接口数据，使用Gson解析json数据
* 使用TabLayout和ViewPager实现顶部导航栏
* 使用RecyclerView实现微信文章精选和美女图片的列表显示
* 使用Bmob后端云实现用户管理模块
* 图片加载使用的是picasso，保存图片用的gilde
* 头像使用CircleImgaeView
* 微信精选部分上拉加载，下拉刷新使用的是PtrClassicFrameLayout
* 美图图片部分上拉加载，下拉刷新使用的是TwinklingRefreshLayout
* 接口数据大部分来自聚合数据，美女图片来自天行数据
* 集成了百度地图sdk
* 兼容了Android6.0权限，Android7.0拍照
* 使用zxingLibrary实现了二维码的扫描与生成

## 功能
* 用户可以登录，注册，保存登录状态，修改密码，忘记密码可通过邮箱修改密码
* 可以跟机器人进行聊天，并实现了语音功能
* 监听了手机的短信，来短信时，会弹窗显示短信
* 微信精选文章的列表显示，点击进入文章详情界面
* 美女图片的网格列表显示，点击后全屏展示图片，并实现了保存到本地功能
* 用户可以拍照设置头像或者从图库选择后裁剪设置头像
* 可以实时定位用户的位置
* 支持二维码的扫描和生成

## 部分功能展示
![](https://github.com/AndroidYiku/SmartBulter/blob/master/screenshot/login.gif) 
![](https://github.com/AndroidYiku/SmartBulter/blob/master/screenshot/jiqiren.gif)
![](https://github.com/AndroidYiku/SmartBulter/blob/master/screenshot/weixin.gif)
![](https://github.com/AndroidYiku/SmartBulter/blob/master/screenshot/girl.gif)
![](https://github.com/AndroidYiku/SmartBulter/blob/master/screenshot/user.gif)
![](https://github.com/AndroidYiku/SmartBulter/blob/master/screenshot/setting.gif)
