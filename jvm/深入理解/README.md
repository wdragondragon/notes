### 类文件结构

u1,u2,u4,u8代表1,2,4,8个字节的无符号数

| 名称                | 大小 | 数量 |            |
| ------------------- | ---- | ---- | ---------- |
| 魔数(magic)         | u4   | 1    | 0xCAFFBABE |
| minor_version       | u2   | 1    | 次版本号   |
| major_version       | u2   | 1    | 主版本号   |
| constant_pool_count | u2   | 1    | 常量池     |
|                     |      |      |            |
|                     |      |      |            |
|                     |      |      |            |
|                     |      |      |            |
|                     |      |      |            |





### 内存分配策略

![img](https://img2018.cnblogs.com/blog/163758/201811/163758-20181101131229284-1189515543.png)

常量池放在堆中的独立空间中，方法区以被存放在本地内存的元空间代替



对象分配：

分配前要进行类加载器加载类对象，逃逸分析，尝试栈上分配，TLAB分配

超大对象判断，eden空间判断，eden空间不足的minorGC后的survicor判断。

![对象分配](http://image.mamicode.com/info/202001/20200111183513707064.jpg)

### 垃圾收集算法

分代收集假说

- 弱分代假说：绝大多数对象都是朝生夕灭的

- 强分代假说：熬过越多次垃圾收集过程的对象就越难以消亡



垃圾收集定义

- 部分收集(Partial GC)
  - 新生代收集(Minor GC)
  - 老年代收集(Major GC)
  - 混合收集(Mixed GC)
- 整堆收集(Full GC)



垃圾回收算法与其缺点：

- 标记-清除算法：标记出所有需要回收的对象，标记完成后，统一回收掉所有被标记的对象，也可以反过来。标记存活的对象，回收所有未被标记的对象。
  - 执行效率不稳定，标记和清除两个过程的执行效率都随对象数量增长而降低
  - 内存空间碎片化，标记、清楚会产生大量不连续的内存碎片。
- 标记-复制算法：将内存划分成相等的两块，只使用其中一块，当这一块内存使用完，就将存活对象复制到另外一块上，然后清理这一块的内存。
  - 半区复制
    - 当大多数对象都是可存活时，产生大量的内存复制开销
    - 内存代价较大。可用内存为原来的一半
  - Appel式回收，分为Eden和Survivor两种空间，HotSpot默认大小比例8：1，两块Survior空间：From Survivor与To Survivor，To Survivor是被浪费掉的空间。垃圾回收时，将Eden中所有存活的对象复制到To区中，from区中的对象去处取决于它们的年龄（年龄阈值，可以通过-XX:MaxTenuringThreshold来设置）超过阀值的去到年老代中。然后交换To区和From区的定义。若当存活对象超过To区大小时，会直接被`担保`，复制到年老代中。
- 标记-整理算法：将存活的对象移动到内存的一端，然后清理掉便捷外的内存。
  - 移动存活对象要更新所有引用这些对象的地方，这种操作需要全程暂停用户应用程序才能进行。



### OopMap，安全点，安全区域

OopMap存放在对象引用的有效范围：在`0x026eb7a9`处有OopMap记录，指明了EBX寄存器中和栈中偏移量为16的内存中各有一个Oop指针，有效范围为从`call`指令`0x026eb7a9`到`0x026eb730+142=0x026eb7be`。



```
[Verified Entry Point]
0x026eb730: mov	%eax,-0x8000(%esp)
…………………………
0x026eb7a9: call	0x026e83e0	; OopMap{ebxOop [16]=Oop off=142}
```



OopMap只存在于某些特定的位置（安全点safepoint）

安全点选定在“是否具有让程序长时间执行的特征”

安全点的停顿具有两种方案。

- 抢先式中断：系统主动中断，判断不在安全点上则恢复线程执行，循环直到跑到安全点上

- 主动式中断：在安全点上设置标志位。当HotSpot需要暂停时将`0x601000`设置不可读，执行到test指令时会产生一个自陷异常信号，用预先注册的异常处理器挂起线程等待。

  ```
  0x01b6d62d:	test	%eax,0x160100 ; {poll}
  ```

  

安全区域：

安全点机制保证了程序执行时，在短时间内就可进入安全点并暂停执行，而无法解决程序处于`sleep`或`blocked`状态时不能自发的走到安全点去中断挂起自己。

在某一断代码片段中，引用关系不会发生变化，属于安全区域。

当用户线程执行到安全区域中，会将自己标识已进入了安全区域。当发起垃圾收集时，就不必去管这些线程。该线程走出安全区域之前垃圾收集完成了。则什么都不会发生，否则线程要挂起等待。





### 编译优化

- 方法内联：

  ```java
  //方法内联前
  static class B {
      int value;
      final int get() {
          return value;
      }
  }
  public void foo() {
      y = b.get();
      z = b.get();
      sum = y + z;
  }
  
  //内联后
  public void foo() {
      y = b.value;
      z = b.value;
      sum = y + z;
  }
  
  //冗余访问消除
  public void foo() {
      y = b.value;
      z = y;
      sum = y + z;
  }
  
  //复写传播
  public void foo() {
      y = b.value;
      y = y;
      sum = y + y;
  }
  
  //无用代码消除
  public void foo() {
      y = b.value;
      sum = y + y;
  }
  ```
  

  
- 逃逸分析：分析对象动态作用域，分为，不逃逸、方法逃逸、线程逃逸。对不同的逃逸程度有采取不同程度的优化

  - 栈上分配：不对线程逃逸的对象可以使用栈上分配来替换堆上分配。减少GC压力

  - 标量替换：逃逸分析出一个对象不会出现方法逃逸，并且对象可以被拆散，程序执行时会创建若干个被这个方法使用的成员变量来代替。

  - 同步消除：逃逸分析出一个变量不会逃逸出线程，会对这个变量实施的同步措施消除。(synchronized消除)

    ```java
    //未优化前代码
    public int test(int x){
    	int xx = x + 2;
        Point p = new Point(xx,42);
        return p.getX();
    }
    
    //1. Point构造方法内联
    public int test(int x) {
        int xx = x + 2;
        Point p = point_memory_alloc();
        p.x = xx;
        p.y = 42;
        return p.x;
    }
    
    //2. 标量替换后
    public int test(int x) {
        int xx = x + 2;
        int px = xx;
        int py = 42;
        return px;
    }
    
    //3. 无效代码消除
    public int test(int x) {
        return x + 2;
    }
    ```
  
- 公共子表达式消除（语言无关）

  ```java
  int d = (c * b) * 12 + a + (a + b * c);
  
  int d = E * 12 + a + (a + E);
  
  int d = E * 13 + a * 2;
  ```

- 数据边界检查消除（语言相关）