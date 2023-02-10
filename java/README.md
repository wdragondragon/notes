# java class

[参考](https://www.cnblogs.com/hvicen/p/6261878.html)

- 判断

  - isPrimitive():boolean
    查看是否基本数据类型。
  - isArray():boolean
    查看是否数组类型。
  - isInterface():boolean
    查看是否接口类型。
  - isAnnotation():boolean
    查看是否注解类型。
  - isEnum():boolean
    查看是否枚举类型。

  - isPrimitive():boolean 

    是否为八个基础类型

  - isMemberClass():boolean
    查看是否成员内部类。

  - isLocalClass():boolean
    查看是否局部内部类。

  - isAnonymousClass():boolean
    查看是否匿名内部类。

  - desiredAssertionStatus():boolean
    测试该类的断言功能是否已打开。

- 获取类，类部类，类方法，类字段

  - getClasses():Class<?>[]

    获取public的内部类

  - getDeclaredClasses():Class<?>[]

    获取除父类的所有内部类

  - getConstructor(Class<?>...):Constructor<T>

    根据参数获取public构造方法

  - getConstructors():Constructor<?>[]

    获取所有public构造方法

  - getDeclaredConstructor(Class<?>...):Constructor<T>

    根据参数获取构造方法

  - getDeclaredConstructors():Constructor<?>[]

    获取所有构造方法

  - getMethod(String, Class<?>...):Method

    根据方法名和参数获取public方法

  - getMethods():Method[]

    获取所有的public方法

  - getDeclaredMethod(String, Class<?>...):Method

    根据方法名和参数获取方法

  - getDeclaredMethods():Method[]

    获取所有方法

  - getField(String):Field

    根据字段名获取public字段

  - getFields():Field[]

    获取所有public字段

  - getDeclaredField(String):Field

    根据字段名获取字段

  - getDeclaredFields():Field[]

    获取所有字段

  - getComponentType():Class<?>
    该类为数组类型时，可通过此方法获取其组件类型。

  - getEnumConstants():T[]
    该类为枚举类型时，可通过此方法获取其所有枚举常量。

  - getDeclaringClass():Class<?>
    获取成员内部类在定义时所在的类。

  - getEnclosingClass():Class<?>
    获取内部类在定义时所在的类。

  - getEnclosingConstructor():Constructor
    获取局部或匿名内部类在定义时所在的构造器。

  - getEnclosingMethod():Method
    获取局部或匿名内部类在定义时所在的方法。

  - asSubclass(Class<U>):Class<? extends U>
    把该类型(子类)转换为目标类型(父类)。

  - isAssignableFrom(Class<?>):boolean
    测试该类型(父类)是否为目标类型(子类)的父类。

- 实例

  - newInstance():T
    使用该类的无参构造器创建实例。
  - isInstance(Object):boolean
    测试该对象实例是否为该类的实例。
  - cast(Object):T
    把对象实例转为该类的实例。

- 获取该类继承父类，实现接口相关

  - getSuperclass():Class<? super T>
    获取直接继承的父类。（无泛型参数）

  - getGenericSuperclass():Type

    返回直接继承父类。（包含泛型参数）

  - getAnnotatedSuperclass():AnnotatedType

    返回直接继承父类时使用的注解

  - getInterfaces():Class<?>[]
    获取实现的接口集。（无泛型参数）

  - getGenericInterfaces():Type[]

    获取实现的接口集。（有泛型参数）

  - getAnnotatedInterfaces():AnnotatedType[]

    返回直接实现接口时使用的注解

- 类相关包和资源

  - getPackage():Package
    获取类在定义时所在的包。
  - getResource(String):URL
    获取与该类所在目录下的路径资源。
  - getResourceAsStream(String):InputStream
    以流的方式获取该类所在目录下的路径资源



# 集合类初始化赋值




```java
// 代码1
List<String> list1 = Arrays.asList("aa", "bb", "cc");
list1.add("dd");    // UnsupportedOperationException

// 代码2
String[] str = {"a","b","c"};
List<String> list = Arrays.asList(str);
str[0] = "e";   // list中的0号位置也一同改变
```

上面有两段代码，看似没有问题，但是运行结果却和大家想象的有些不同。首先*代码1*，使用asList方法返回的创建的List对象，***不允许进行修改操作***，否则将会抛出一个**UnsupportedOperationException**；再来看*代码2*，我们将一个数组作为asList的参数，得到一个List对象，但是***此时我们改变这个数组中元素的值，list对象的值也会发生改变***，因为这个List对象底层引用的就是这个数组，并且和代码1一样，这个list也不能修改。

  但是，若我们将返回的List对象作为参数传入ArrayList的构造器中，这个问题就不会发生，因为ArrayList的构造器将会把传入的list中所有的元素***复制一份***，因此不会影响到原数组，且可以随意改变。



正确创建赋值方式：

```java
int[] num = {1,2,3};
String strs = {"a", "b", "c"}

ArrayList<String> list = new ArrayList<>(Arrays.asList("aa", "bb", "cc"));

List<String> list = new ArrayList<String>(){ {add("a"); add("b"); add("c");} };


HashMap<String, Integer> map = new HashMap<String, Integer>() { 
    		{
                put("a", 1);
                put("b", 2); 	
                put("c", 3);
            }
         };
```

# stream

**map** 方法用于映射每个元素到对应的结果

**filter** 方法用于通过设置的条件过滤出元素。

**limit** 方法用于获取指定数量的流。

**sorted** 方法用于对流进行排序。

**Collectors** 类实现了很多归约操作，例如将流转换成集合和聚合元素。

```java
List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);

List<Integer> squaresList = numbers.stream()
    .map(i -> i*i)//
    .distinct()
    .sorted(Comparator.reverseOrder())
    .limit(3)
    .collect(Collectors.toList());

squaresList.forEach(System.out::println);
//49
//25
//9
```



List转固定key的MapList：{""}

```java
List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
List<HashMap<String,Integer>> numberMapList = numbers.stream()
          .map(i-> new HashMap<String,Integer>(){{put("value",i);}})
          .collect(Collectors.toList());
System.out.println(numberMapList);
//[{value=3}, {value=2}, {value=2}, {value=3}, {value=7}, {value=3}, {value=5}]
```

