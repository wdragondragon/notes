[问题解决方式](https://stackoverflow.com/questions/22150417/hadoop-mapreduce-java-lang-unsatisfiedlinkerror-org-apache-hadoop-util-nativec)

[编译hadoop参考](https://blog.csdn.net/qq_36835255/article/details/124733599)

需要对应hadoop依赖版本的native，

此时解决方法：需要在Linux服务器上对使用依赖版本的hadoop进行编译，后获取native传到引擎服务器上，并启动作业时指定-Djava.lib.path。

详细步骤：

1. 安装编译hadoop源码所需要的系统依赖：

   1. 安装gcc环境 ：yum install -y gcc gcc-c++ automake autoconf libtool yasm nasm

   2. 安装zlib,bzip2,lz4：yum install -y bzip2 bzip2-devel zlib zlib-devel lz4 lz4-devel

   3. 安装openssl：yum install -y openssl-devel

   4. 安装cmake 3.1.0版本以上：

      ```bash
      curl -O https://cmake.org/files/v3.1/cmake-3.1.0-Linux-x86_64.tar.gz
      tar xzvf cmake-3.1.0-Linux-x86_64.tar
      cd cmake-3.1.0-Linux-x86_64
      cp -r share/* /usr/share/ 
      cp bin/* /usr/bin/*
      cmake -version
      ```

   5. 安装protobuf：需要版本2.5.0

      ```bash
      curl -O https://github.com/protocolbuffers/protobuf/releases/download/v2.5.0/protobuf-2.5.0.tar.gz
      tar xzvf protobuf-2.5.0.tar.gz
      cd protobuf-2.5.0
      ./autogen.sh
      ./configure
      make
      make install
      protoc --version
      ```

   6. 安装snappy

      ```bash
      curl -O https://github.com/google/snappy/archive/refs/tags/1.1.7.tar.gz
      tar xf 1.1.7.tar.gz
      cd snappy-1.1.7
      mkdir -p build
      cd build
      cmake ..
      make install
      cd ..
      cp build/libsnappy.a /usr/lib
      cp build/snappy-stubs-public.h /usr/include/
      cp snappy-c.h /usr/include/
      cp snappy.h /usr/include/
      ls -lh /usr/lib64/ | grep snappy
      ```

   7. 安装zstd

      ```bash
      curl -O https://codeload.github.com/facebook/zstd/tar.gz/refs/tags/v1.4.10
      tar xzvf zstd-1.4.10.tar.gz
      cd zstd-1.4.10
      make && make install
      ls /usr/local/lib | grep zstd
      ```

   8. 安装isa-l

      ```bash
      curl -O https://codeload.github.com/intel/isa-l/tar.gz/refs/tags/v2.30.0
      tar xzvf isa-l-2.30.0.tar.gz
      cd isa-l-2.30.0
      ./autogen.sh
      ./configure --prefix=/usr --libdir=/usr/lib64
      make
      make install
      ls /usr/lib64/ | grep isal
      ```

2. 安装jdk8，mvn3（已有则跳过）

   1. 安装aws-DynamoDBlocal包

      ```bash
      curl -O https://s3-us-west-2.amazonaws.com/dynamodb-local/release/com/amazonaws/DynamoDBLocal/1.11.86/DynamoDBLocal-1.11.86.jar
      curl -O https://s3-us-west-2.amazonaws.com/dynamodb-local/release/com/amazonaws/DynamoDBLocal/1.11.86/DynamoDBLocal-1.11.86.pom
      # 移动到mvn仓库
      mkdir -p ~/.m2/repository/com/amazonaws/DynamoDBLocal/1.11.86
      mv DynamoDBLocal-1.11.86.jar ~/.m2/repository/com/amazonaws/DynamoDBLocal/1.11.86
      mv DynamoDBLocal-1.11.86.pom ~/.m2/repository/com/amazonaws/DynamoDBLocal/1.11.86
      ```

   2. 安装aws-java-sdk-bundle包

      ```bash
      curl -O https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-bundle/1.11.901/aws-java-sdk-bundle-1.11.901.jar
      # 手动Install到mvn
      mvn install:install-file -Dfile=aws-java-sdk-bundle-1.11.901.jar -DgroupId=com.amazonaws -DartifactId=aws-java-sdk-bundle -Dversion=1.11.901 -Dpackaging=jar
      ```

3. clone hadoop，并切换到对应分支，进行编译

   ```bash
   git clone https://github.com/apache/hadoop.git
   cd hadoop
   git checkout branch-3.1.1
   
   mvn clean package -DskipTests -Pdist,native  -Dtar -Dbundle.snappy=true -Drequire.snappy=true  -Dsnappy.prefix=/usr/lib64  -Dsnappy.lib=/usr/lib64  -Drequire.zstd=true -Dbundle.zstd=true -Dzstd.lib=/usr/local/lib -Dbundle.isal=true -Drequire.isal=true -Disal.prefix=/usr/lib64 -Disal.lib=/usr/lib64
   ```

   解释下上面的参数
   Pdist,native ：编译生成 hadoop 动态库
   DskipTests ：跳过测试
   Dtar ：以tar打包
   Dbundle.snappy: 添加 snappy 压缩支持
   Dsnappy.lib: snappy 库路径

4. 验证打包

   1. ll hadoop-dist/target，里面会有一个hadoop-3.1.1.tar.gz的压缩包。解压。

      ```bash
      cp hadoop-3.1.1.tar.gz ~/
      tar xzvf hadoop-3.1.1.tar.gz
      cd hadoop-3.1.1
      bin/hadoop checknative
      # 执行完出现以下结果，native包贼绑定完毕。
      ```

      ```bash
      2022-12-01 13:07:58,938 INFO bzip2.Bzip2Factory: Successfully loaded & initialized native-bzip2 library system-native
      2022-12-01 13:07:58,942 INFO zlib.ZlibFactory: Successfully loaded & initialized native-zlib library
      Native library checking:
      hadoop:  true /root/hadoop-3.1.1/lib/native/libhadoop.so.1.0.0
      zlib:    true /lib64/libz.so.1
      zstd  :  true /root/hadoop-3.1.1/lib/native/libzstd.so.1
      snappy:  true /root/hadoop-3.1.1/lib/native/libsnappy.so.1
      lz4:     true revision:10301
      bzip2:   true /lib64/libbz2.so.1
      openssl: true /lib64/libcrypto.so
      ISA-L:   true /root/hadoop-3.1.1/lib/native/libisal.so.2
      ```

      

   2. 将hadoop目录下的`lib/native`，全部拷贝出来。迁移到引擎目录`/bmdata/software/hadoop/native`，并在datax.py启动脚本内的jvm参数中加入`-Djava.library.path=``/bmdata/software/hadoop/native`

 

