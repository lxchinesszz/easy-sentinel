![easy](https://ws3.sinaimg.cn/large/006tNc79gy1g2e0ejmbv0j312s0bsmxv.jpg)

### EasySentinel

---
![](https://img.shields.io/badge/build-passing-brightgreen.svg) ![](https://img.shields.io/badge/license-Apache%202-blue.svg)

是一款专门为`SpringBoot`项目设计的限流组件，利用`Redis`+`lua`从而来实现高性能和分布式的能力。使用比较简单。通过半嵌入式的开发即可使用分布式注解。引用使用`Redis`作为注册中心,所以需要添加redis依赖





SpringBoot2版本之后官方强烈建议以此来替换`spring-boot-starter-redis`

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### 正在Coding

为了有更好的用户体验,控制台的开发已经提上日常。当前项目只有本人维护,欢迎前端或者后端的同学一起来参与

![](https://ws3.sinaimg.cn/large/006tNc79gy1g2e0wi3bpuj31320ihmz8.jpg)

### 使用案例

只用将需要限制的接口加上`@EasySentinel`即可实现限流能力

通过Qps设置当前接口的`qps`,`fallback`指定限流后的备用方法 。

```java
@RestController
public class WebController {

  @EasySentinel(Qps = 10, fallback = "mock", resourceName = "获取用户名")
  @GetMapping(value = "/user/{name}")
  public String getUser(@PathVariable("name") String userName) {
    System.out.println("process:" + userName);
    return userName;
  }

  public String mock(String userName) {
    System.out.println("Mock::" + userName);
    return "Mock::" + userName;
  }
}
```



### 备用方法高级案例

前面只通过`fallback`实现备用方法的执行，但是平时为了让控制层的代码尽量保证精简，`EasySentinel`也支持通过工具类的方式来,指定`blockHandlerClass`处理类的方法`blockHandler`

```java
@RestController
public class WebController {
  @EasySentinel(resourceName = "获取用户名2", Qps = 1, blockHandler = "fallBack",    `		blockHandlerClass = DefaultLimiterBlockHandler.class)
  @GetMapping(value = "/user1/{name}")
  public String getUser2(@PathVariable("name") String userName) {
    System.out.println("process:" + userName);
    return userName;
  }
}
public class DefaultLimiterBlockHandler {
  public Object fallBack(BlockException block, String userName) {
    return "DefaultLimiterBlockHandler::" + userName + block.getMessage();
  }
}
```





## 创作者

lxchinesszz

- https://blog.springlearn.cn

