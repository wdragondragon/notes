

[下载graalvm](https://www.oracle.com/downloads/graalvm-downloads.html)，解压并配置graalvm_home环境变量，若原有java_home需要将其指向graalvm_home：

```
GRAALVM_HOME=C:\graalvm-ee-java11-21.3.0

path=path;%GRAALVM_HOME%\bin
JAVA_HOME=%GRAALVM_HOME%
```

使用gu安装native-image：`gu install native-image`



创建编译测试代码HelloWorld.java：

```java
public class HelloWorld { 
    public static void main(String[] args) { 
        System.out.println("Hello World"); 
    }
}
```



编译HelloWorld

```shell
javac HelloWorld.java
native-image HelloWorld
```



问题一：

在进行native-image编译过程中，可能会出现cl.exe缺失的问题。

```shell
Error: Default native-compiler executable 'cl.exe' not found via environment variable PATH
```



由[官网介绍](https://docs.oracle.com/en/graalvm/enterprise/21/docs/getting-started/installation-windows/)可知，在windows中使用native-image需要安装msvc2017-15.9或以上版本，可使用vs安装工具安装所需组件，[vs下载地址](https://visualstudio.microsoft.com/zh-hans/downloads/)

![image-20211118005017063](https://gitee.com/wdragondragon/picture-bed/raw/master/imgs/202111180050311.png)



经实践，graalvm-ee-21.3.0在vs installer中下载如下组件。

![img](https://gitee.com/wdragondragon/picture-bed/raw/master/imgs/202111180048180.png)



安装完毕后，需要配置msvc的环境变量（大约在这个路径下，根据下载路径来改变）

`MSVC=C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Tools\MSVC\14.29.30133`



cmd中执行`cl`指令后出现以下提示，msvc则配置成功了

![image-20211118202234642](https://gitee.com/wdragondragon/picture-bed/raw/master/imgs/202111182022807.png)



问题二：

```shell
fatal error C1034: stdio.h: 不包括路径集
```

需要添加`window include`，`window lib`的环境变量

```shell
WK10_INCLUDE=C:\Program Files (x86)\Windows Kits\10\Include\10.0.20348.0
WK10_LIB=C:\Program Files (x86)\Windows Kits\10\Lib\10.0.20348.0
## 变量值必须为INCLUDE和LIB
INCLUDE=%WK10_INCLUDE%\ucrt;%WK10_INCLUDE%\um;%WK10_INCLUDE%\shared;%MSVC%\include
LIB=%WK10_LIB%\um\x64;%WK10_LIB%\ucrt\x64;%MSVC%\lib\x64

```

[GraalVM Native Image介绍](https://aijishu.com/a/1060000000090571)

[java-graalvm-start](https://gitee.com/westinyang/java-graalvm-start#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

[Visual Studio 2019 配置 MSVC 环境变量，使用命令行编译](https://www.jianshu.com/p/7fab25165f4b)

[使用msvc的cl工具编译程序，以及 “fatal error C1034: iostream: 不包括路径集”等问题解决](https://blog.csdn.net/weixin_41115751/article/details/89817123)

[cl.exe missing when building native app using GraalVM](https://stackoverflow.com/questions/64197329/cl-exe-missing-when-building-native-app-using-graalvm)

