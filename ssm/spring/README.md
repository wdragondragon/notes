# Spring



## 一、注册组件



**@Configuration**：设置为配置类

**@Bean**：在配置类下将某方法的返回值注册到ioc容器中





**@ComponentScan**自动扫描组件

搭配**@Filter**定义扫描组件规则

```java
@ComponentScans(value = {
    @ComponentScan(value = "org.example",includeFilters = {
//            @Filter(type = FilterType.ANNOTATION,classes = {Controller.class }),//包扫描排除规则
//            @Filter(type= FilterType.ASSIGNABLE_TYPE,classes = {BookService.class}),
            @Filter(type= FilterType.CUSTOM,classes = {MyTypeFilter.class})
    },useDefaultFilters = false)
})
//@ComponentScan value:指定要扫描的包
//@ComponentScans value:指定多个要扫描的包
//excludeFilters = Filter[]：指定扫描的时候按照什么规则排除那些组建
//includeFilters = Filter[]：指定扫描的时候只需要包含哪些组件
    //使用这个时用禁用掉默认的Filters扫描全部useDefaultFilters = false
//FilterType.ANNOTATION按照注解
//FilterType.ASSIGNABLE_TYPE按照给定类型
//FilterType.REGEX使用正则指定
//FilterType.CUSTOM:使用自定义规则
```

实现**TypeFilter**接口指定过滤规则，

```java
public class MyTypeFilter implements TypeFilter {
    /**
     *
     * @param metadataReader:读取到的当前正在扫描类的信息
     * @param metadataReaderFactory:可以获取到其他任何类信息的
     * @return
     * @throws IOException
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        //获取当前类注解的信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        //获取当前正在扫描的类的类信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        //获取当前类资源（类的路径）
        Resource resource = metadataReader.getResource();

        String className = classMetadata.getClassName();
        System.out.println("MyTypeFilter----->"+className);
        return className.contains("er");
    }
}
```

**@Scope**设置组件作用域

**@Lazy**设置懒加载

```java
@Scope("prototype")
//默认是单例的
/**
 *      * @see #SCOPE_PROTOTYPE原型
 *         在getBean的时候才会创建（懒加载）
 *      * @see #SCOPE_SINGLETON单例
 *         在容器启动时就会创建（饿加载）
 *      * @see #SCOPE_REQUEST一次请求创建一个
 *      * @see #SCOPE_SESSION一个session创建一个实例
 */
@Lazy
/**
 *  单例懒加载：容器启动不创建对象。第一次使用（获取）Bean创建对象，并初始化。
 */
@Bean("person")
public Person person(){
    return new Person("张三",25);
}
```

**@Conditional**：按条件注册组件，需要实现**Condition**接口



@Import：给容器中快速加入一个组件，可在import中放bean，**ImportSelector**和**ImportBeanDefinitionRegistrar**。

**ImportSelector**：实现ImportSelector接口，返回String[]，里面放入全类名，就会去寻找并实例化注册到容器中。

```java
public class MyImportSelector implements ImportSelector {
    //返回值， 就是导入到容器中的组件全类名
    /**
     * @param importingClassMetadata：当前标注@Import注解类的所有注解信息
     * @return
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //不能返回Null
        //填入全类名，
        return new String[]{"org.example.bean.Blue","org.example.bean.Yellow"};
    }
}
```

**ImportBeanDefinitionRegistrar**：实现这个接口，选择一个class并赋名，手动注册。

```java
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     *
     * @param importingClassMetadata:当前标注@ImportBeanDefinitionRegistrar注解类的所有注解信息
     * @param registry:BeanDefinition注册类：
     *                 把所有需要添加到容器中的bean：调用
     *                 BeanDefinitionRegistry.registerBeanDefinition手动注册进来
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        boolean red = registry.containsBeanDefinition("org.example.bean.Red");
        boolean blue = registry.containsBeanDefinition("org.example.bean.Blue");
        if(red && blue){
            //指定bean
            RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(RainBow.class);
            //注册一个Bean
            registry.registerBeanDefinition("rainbow",rootBeanDefinition);
        }

    }
}
```



几种组件注册方式和区别：

```java
/**
 * 给容器中注册组件：
 * 1）包扫描+组件注注解
 * 2）@Bean[导入的第三方包里面的组件]
 * 3）@Import[快速给容器中导入一个组件]
 *      1)@Import(要导入到容器中的组件）：容器中就会自动注册这个组件，id默认是全类名
 *      2)@ImportSelector：返回需要导入 的组件的全类名数组：
 *      3)ImportBeanDefinitionRegistrar:
 * 4）使用FactoryBean（工厂bean）
 *      1)默认获取到的是工厂bean调用getObject创建的对象
 *      2）要获取工厂Bean本身，我们需要给id前面加一个&符号
 */
```



## 二、生命周期

### 1.初始化和销毁方法

**方法一：**

@Bean中使用**initMethod**和**destroyMethod**指定bean中的销毁方法名称，

**在对多实例Bean不会进行调用销毁方法。**

```java
public class Car {
    public Car(){
        System.out.println("car constructor...");
    }
    public void init(){
        System.out.println("car init");
    }
    public void destroy(){
        System.out.println("car destroy");
    }
}

@Configuration
public class MainConfigOfLifeCycle {
    @Bean(initMethod = "init",destroyMethod = "destroy")
    public Car car(){
        return new Car();
    }
}
```



**方法二：**

- Bean实现InitializingBean接口，其中的afterPropertiesSet方法，在Bean属性设置完成后调用，相当于初始化方法。

- Bean实现DisposableBean接口，其中的destory会在单例Bean销毁后调用。

```java
@Component
public class Cat implements InitializingBean, DisposableBean {
    public Cat(){
        System.out.println("cat constructor......");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("cat.....destroy....");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("cat.....init");
    }
}
```



**方法三：**

使用JSR250：

- 创建@PostConstruct：在bean创建完成并且属性赋值完成：来执行初始化方法。

*                    销毁@PreDestroy：在容器销毁bean之前通知我们进行清理工作。

```java
@Component
public class Dog {
    public Dog(){
        System.out.println("dog constructor...");
    }

    //对象创建并赋值之后调用
    @PostConstruct
    public void init(){
        System.out.println("dog......PostConstruct...");
    }
    //容器移除对象之前
    @PreDestroy
    public void destroy(){
        System.out.println("dog......PreDestroy...");
    }
}
```



**方法四：**

后置处理器：实现BeanPostProcessor接口

- postProcessBeforeInitialization：对所有的bean的所有的初始化操作之前操作（前三个方法之前）
- postProcessAfterInitialization：对所有的bean的所有的初始化操作之后操作（前三个方法之后）

```java
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization..."+beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization..."+beanName);
        return bean;
    }
}
```



原理：

```
* 遍历得到容器中的BeanPostProcessor：挨个执行beforeInitialization，
* 一旦返回null跳出for循环，不会执行后面的BeanPostProcessor.postProcessBeforeInitialization
* populateBean(beanName, mbd, instanceWrapper) 给Bean进行属性赋值
* {
*     applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
*     invokeInitMethods(beanName, wrappedBean, mbd);执行自定义初始化
*     applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
* }
```

spring底层使用BeanPostProcessor的各种实现类来实现前三种方法

例如：InitDestroyAnnotationBeanPostProcessor：JSR250中的@PostConstruct和@PreDestroy处理

## 三、配置

@PropertySource(value = "classpath:/person.properties")



## 四、spring底层组件的注入

aware注入spring底层组件和原理

自定义组件想要使用Spring容器底层的一些组件（ApplicationContext,BeanFactory等）：

自定义组件实现xxxAware：在创建对象的时候，会调用接口规定的方法注入相关组件：Aware：

把spring底层一些组件注入到自定义的Bean中



xxxAware：XXXAwareProcessor中回调xxxAware中的方法

```java
//获取容器，自身Bean名字，字符串解析器
@Component
public class Red implements ApplicationContextAware, BeanNameAware, EmbeddedValueResolverAware {

    private ApplicationContext applicationContext;

    private String beanName;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        System.out.println("传入的ioc:" + applicationContext);
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = beanName;
        System.out.println("当前的Bean名字" + name);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        String resolveStringValue = resolver.resolveStringValue("你好${os.name} 我是#{20*18}");
        System.out.println("解析的字符串：" + resolveStringValue);
    }
}
```

## 五、AOP

指在程序运行期间动态的将某段代码切入到指定方法指定位置进行运行的编程方式

### 使用方法

- 导入aop模块：Spring AOP:(spring-aspects)
- 定义一个业务逻辑类（MathCalculator）业务逻辑运行的时候将日志进行打印（方法之前、方法运行结果，方法出现异常）
- 定义一个日志切面类（LogAspects）：切面类里面的方法需要动态感知MathCalculator运行到哪里
  - 前置通知(@Before)：logStart：在目标方法运行之前运行
  - 后置通知(@After)：logEnd：在目标方法运行之后运行(无论方法是否异常)
  - 返回通知(@AfterReturning)：logReturn：在目标方法正常返回之后运行
  - 异常通知(@AfterThrowing)：logException：在目标方法运行异常之后运行
  - 环绕通知(@Around)：动态代理，手动推进目标方法运行(joinPoint.procced())
- 给切面类的目标方法标注何时何地运行（通知注解）
- 将切面类和业务逻辑类（目标方法所在类）都加入到容器中
- 必须告诉Spring哪个类是切面类（给切面类上加一个注解）
- 给配置类中加@EnableAspectJAutoProxy，开启基于注解的aop模式



```java
@EnableAspectJAutoProxy
@Configuration
public class MainConfigOfAOP {
    //业务逻辑类加入容器中
    @Bean
    public MathCalculator calculator(){
        return new MathCalculator();
    }

    //切面类加入到容器中
    @Bean
    public LogAspects logAspects(){
        return new LogAspects();
    }
}
```

```java
@Aspect
public class LogAspects {
    //抽取公共的切入表达式
    //1、本类引用:pointCut()
    //2、其他的切面引用:org.example.aop.LogAspects.pointCut()
    @Pointcut("execution(public int org.example.aop.MathCalculator.*(..))")
    public void pointCut() {
    }

    //@Before在目标方法之前切入:切入点表达式
    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        System.out.println(joinPoint.getSignature().getName() + "运行……@Before:参数列表是：{" + Arrays.toString(args) + "}");
    }

    @After("pointCut()")
    public void logEnd(JoinPoint joinPoint) {
        System.out.println(joinPoint.getSignature().getName() + "结束……@After");
    }

    @AfterReturning(value = "pointCut()", returning = "result")
    public void logReturn(JoinPoint joinPoint, Object result) {
        System.out.println(joinPoint.getSignature().getName() + "正常返回……@AfterReturning:运行结果：{" + result + "}");
    }

    @AfterThrowing(value = "pointCut()", throwing = "e")
    public void logException(JoinPoint joinPoint, Exception e) {
        System.out.println(joinPoint.getSignature().getName() + "异常……@AfterThrowing:异常信息：{" + e.getMessage() + "}");
    }
}
```

### 原理

@EnableAspectJAutoProxy：@Import(AspectJAutoProxyRegistrar.class)给容器导入AspectJAutoProxyRegistrar。