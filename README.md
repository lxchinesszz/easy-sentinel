![easy](https://ws3.sinaimg.cn/large/006tNc79gy1g2e0ejmbv0j312s0bsmxv.jpg)

### EasySentinel

---
![](https://img.shields.io/badge/build-passing-brightgreen.svg) ![](https://img.shields.io/badge/license-Apache%202-blue.svg)
[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)
是一款专门为`SpringBoot`项目设计的限流组件，利用`Redis`+`lua`从而来实现高性能和分布式的能力。使用比较简单。通过半嵌入式的开发即可使用分布式注解。引用使用`Redis`作为注册中心,所以需要添加redis依赖





SpringBoot2版本之后官方强烈建议以此来替换`spring-boot-starter-redis`

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### 提供报表接口【因为个人没有太多时间投入,所以搁置,如果有人感兴趣可以联系我一起开发】

为了有更好的用户体验,控制台的开发已经提上日常。当前项目只有本人维护,欢迎前端或者后端的同学一起来参与

![](https://ws3.sinaimg.cn/large/006tNc79gy1g2e0wi3bpuj31320ihmz8.jpg)


### 开始引入依赖

```
<dependency>
 <groupId>com.github.lxchinesszz</groupId>
    <artifactId>easy-sentinel</artifactId>
    <version>1.0.1-SNAPSHOT</version>
</dependency>
```



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


### 压力测试

`ab -n 14 -c 1  http://127.0.0.1:8889/user/lxchinesszz`


![](https://ws2.sinaimg.cn/large/006tNc79gy1g2evzcxz9rj311b0hy4qp.jpg)



## 创作者

lxchinesszz

<a href="https://www.toutiao.com/c/user/3686495601/#mid=1563737358895105">
<img src="https://blog.springlearn.cn/img/avatar.jpg" class="blur" style="width:100px;height:100px;border-radius:100%;overflow:hidden;filter: progid:DXImageTransform.Microsoft.Blur(PixelRadius=1, MakeShadow=false);  -webkit-filter: blur(12px); /* Chrome, Opera */
       -moz-filter: blur(1px);
        -ms-filter: blur(1px);
            filter: blur(1px);box-shadow: 2px 2px 20px #888888 ;margin : 10px 0px 15px 5px;" />
</a>

- https://blog.springlearn.cn

