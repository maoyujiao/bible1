mob 的 **分享** 和 **短信** sdk

```
// 在主gradle文件添加
allprojects {
    repositories {
        flatDir {
            dirs '../mob/libs'
        }
    }
}
```


短信需要依赖下面四个
- MobCommons-2017.0914.1125.jar
- MobTools-2017.0914.1125.jar
- SMSSDK-3.0.0.aar
- SMSSDKGUI-3.0.0.aar   （可选）