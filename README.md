# DreamTogether
####小小的我有大大的梦想。
###### code by : 潘安(www.mrpann.com)

*****************************************************************************                            

                            _ooOoo_
                           o8888888o
                           88" . "88
                           (| -_- |)
                            O\ = /O
                        ____/`---'\____
                      .   ' \\| |// `.
                       / \\||| : |||// \
                     / _||||| -:- |||||- \
                       | | \\\ - /// | |
                     | \_| ''\---/'' | |
                      \ .-\__ `-` ___/-. /
                   ___`. .' /--.--\ `. . __
                ."" '< `.___\_<|>_/___.' >'"".
               | | : `- \`.;`\ _ /`;.`/ - ` : | |
                 \ \ `-. \_ __\ /__ _/ .-` / /
         ======`-.____`-.___\_____/___.-`____.-'======
                            `=---='

         .............................................
                  佛祖保佑             永无BUG


********************************************************************************

####项目简介

+ 每个人都有自己的梦想，或大或小。
+ 这是一个情怀app。
+ 一群追梦文青的自留地。

**********************************************************

####项目介绍
##### PHP服务端
+ 采用mvc模式设计后段接口,由于暂时只提供接口供安卓端访问,暂时缺少v层。
+ 数据库设计：梦想和用户两张表，以及两张扩展表。自由度增高，方便以后增加信息。
+ 

##### ANDROID应用端
+ 完成了首页显示所有梦想、用户的登录(注册)、发表梦想(暂时只支持文字)、显示用户发表梦想的时间线。
+ 未完待续

###### 功能日志：
+ 基本框架搭建，网络请求模块,缓存模块   3.27
+ 用户注册登录   3.29
+ 发表梦想       3.30
+ 图片上传       4.8
+

**********************************************************
##### 主要技术功能截图
+ 首页采用自定义recyleview与cardview,可以进行下拉刷新与上拉自动加载功能，可以自定义加载动画。本应用所有数据均解析自php服务端，格式为json。图片的加载以及缓存等均采用第三方框架imageloader实现，封装了部分代码后来感觉冗余便删掉了。
<img src="https://github.com/wslongchen/DreamTogether/blob/master/截图/Screenshot_20160612-121443.png" width="260px" height="480px"/>
+ 点击一条梦想进入详情界面。本界面采用了自定义listview，并实现了仿ios下拉水滴动画。评论可以插入表情，具体是用SpannableStringBuilder实现图文混排，也可以使用Html.fromHtml进行。另外实现表情以及gif动画具体可见源代码。
<img src="https://github.com/wslongchen/DreamTogether/blob/master/截图/Screenshot_20160612-155845.png" width="260px" height="480px"/>
+ 点击首页右上角图标进入仿支付宝咻一咻界面，水波纹效果通过自己绘制点击效果imageview控件实现。点击之后通过一个等概率随机算法以某一特定的概率咻到从数据库里随机获取到的梦想。另外点击按钮声音通过SoundPool简单播放放在资源文件raw下的音频资源。
<img src="https://github.com/wslongchen/DreamTogether/blob/master/截图/Screenshot_20160612-155415.png" width="260px" height="480px"/>
+ 咻一咻咻到的梦想通过ViewPager展示，并引用facebook开源动画jar包nineoldandroids-2.4.0.jar进行切换动画效果支持。
<img src="https://github.com/wslongchen/DreamTogether/blob/master/截图/Screenshot_20160612-155703.png" width="260px" height="480px"/>
+ 小记界面仿one is all的小记功能，对精选中的梦想可以自定义内容和图片，然后进行分享，分享功能暂时只支持qq、qq空间、微信、微信朋友圈以及腾讯微博。手动集成了分享模块，并没有用第三方分享平台。另外天气温度接口用的中国天气网的接口，地理位置使用的是百度地图lbs服务。另外此界面涉及了将view界面转换为bitmap以及bitmap转文件的技术，具体可见源代码。
<img src="https://github.com/wslongchen/DreamTogether/blob/master/截图/Screenshot_20160612-155906.png" width="260px" height="480px"/>
+ 个人中心界面菜单采用动态加载，后期可以支持网络添加。个人背景采用高斯模糊效果，由于现有java模糊效果渲染较慢，造成界面长时间卡顿，改为jni模式调用c/c++代码进行模糊效果，但是在图片压缩后在进行模糊，两者时间一样。
<img src="https://github.com/wslongchen/DreamTogether/blob/master/截图/Screenshot_20160612-155940.png" width="260px" height="480px"/>
+ 我的二维码功能，界面仿qq二维码。利用google二维码支持zxing的core包，实现了二维码的生成以及仿微信的扫一扫功能，并实现了特色二维码功能，可以进行二维码样式的更改。
<img src="https://github.com/wslongchen/DreamTogether/blob/master/截图/Screenshot_20160612-141433.png" width="260px" height="480px"/>
<img src="https://github.com/wslongchen/DreamTogether/blob/master/截图/Screenshot_20160612-151958.png" width="260px" height="480px"/>
+ 我的梦想历程。

<img src="https://github.com/wslongchen/DreamTogether/blob/master/截图/Screenshot_20160612-155906.png" width="260px" height="480px"/>
<img src="https://github.com/wslongchen/DreamTogether/blob/master/截图/Screenshot_20160612-155906.png" width="260px" height="480px"/>
<img src="https://github.com/wslongchen/DreamTogether/blob/master/截图/Screenshot_20160612-155906.png" width="260px" height="480px"/>

**********************************************************
######更新于2016/04/10


&copy; 2016 MrPan
