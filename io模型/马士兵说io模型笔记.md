# 马士兵说io模型笔记



## BIO

阻塞io-Blocking IO(Input - Output) 

![image-20200521163139133](C:\Users\10619\AppData\Roaming\Typora\typora-user-images\image-20200521163139133.png)

在Server端中，创建ServerSocket，并使用ServerSocket.accept等待客户创建连接，这会造成阻塞，在等到客户连接时使用Socket创建Thread后使用inputStream.read()和outputStream.write()也是阻塞的。



## NIO

非阻塞io-Non-Blocking IO



single Thread模式

![NIO单线程模型](C:\Users\10619\AppData\Roaming\Typora\typora-user-images\image-20200521163624513.png)

在NIO单线程模型中，selector负责查看是否有client连接，并将client连接到server，又负责client与server的通信





reactor模式

![reactor](C:\Users\10619\AppData\Roaming\Typora\typora-user-images\image-20200521170243857.png)



## AIO

AIO-不再需要轮询(asynchronous IO)



![AIO模型](C:\Users\10619\AppData\Roaming\Typora\typora-user-images\image-20200521171408407.png)





## 	Netty

实现对于NIO BIO封装成AIO的样子/

