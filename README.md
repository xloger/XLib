每个开发者大概都有一个轮子，用于将那些常用的功能封装起来。而这款 `Xlib` 就是我的轮子。

它有什么功能呢？目前它有：

- Android6.0 运行时权限的支持；
- 更好用的 log 工具；
- 更强大的 toast

目前它存在的问题：
- dialog 和多线程是我写的比较失败的两个库，而且被 Anko 完爆；
- test module 是我用来测试功能的，但是之前学习 kotlin 的时候在里面写了些练习代码，将来会清除掉；
- 项目正在逐步更换为 kotlin，因此对 Java 的支持可能不够好，不过将来我的示范代码会 Java、kotlin 写两份。

使用方法：

build project 后，将 `XLib\app\build\outputs\aar` 底下的 aar 文件复制到你项目的 libs 文件夹内（与其他 jar 一起）。

随后 `dependencies` 添加 `implementation(name: 'app-debug', ext: 'aar')` 即可。（旧版 gradle 请使用 compile 关键字）

如果提示找不到该 aar，请在 gradle 中添加：
```
repositories {
    flatDir {
        dirs 'libs'
    }
    google()
}
```

随后即可正常使用。具体介绍我将来会完善，也可参考 test module 下的示范代码。