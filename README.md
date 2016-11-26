# spring-boot-dubbo-starter  
以springboot annotation + yml方式来启动dubbo  
  
## 实现步骤

### 1. pom.xml配置
```
<dependencies>
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>springboot-dubbo-demo-api</artifactId>
			<version>1.0</version>
		</dependency>

		<!-- Import dependency management from Spring Boot -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
			<version>1.3.8.RELEASE</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-parent</artifactId>
			<version>1.3.8.RELEASE</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>

		<!-- dubbo -->
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>dubbo</artifactId>
			<version>2.5.3</version>
			<exclusions>
				<exclusion>
					<artifactId>spring</artifactId>
					<groupId>org.springframework</groupId>
				</exclusion>
			</exclusions>
		</dependency>
		<dependency>
			<groupId>org.apache.zookeeper</groupId>
			<artifactId>zookeeper</artifactId>
			<version>3.4.9</version>
		</dependency>
		<dependency>
			<groupId>com.github.sgroschupf</groupId>
			<artifactId>zkclient</artifactId>
			<version>0.1</version>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
```

### 2. application.yml

```
dubbo:
  application:
    name: spring-boot-dubbo-consumer
  protocol:
    name: dubbo
    host: 127.0.0.1
    port: 20880
  registry:
    protocol: zookeeper
    address: 127.0.0.1
    port: 2181
```

### 3. dubbo代码配置
```
/**
  * 扫描dubbo annotation
  * @return
  */
  @Bean
  public AnnotationBean annotationBean() {
	 AnnotationBean annotationBean = new AnnotationBean();
	 annotationBean.setPackage("com.alibaba.dubbo.demo.consumer");
	 return annotationBean;
  }

/**
  * 注入dubbo上下文
  * 
  * @return
  */
  @Bean
  @ConfigurationProperties(prefix = "dubbo.application")
  public ApplicationConfig applicationConfig() {
	// 当前应用配置
	ApplicationConfig applicationConfig = new ApplicationConfig();
	return applicationConfig;
  }

/**
  * 注入dubbo注册中心配置,基于zookeeper
  * 
  * @return
  */
  @Bean
  @ConfigurationProperties(prefix = "dubbo.registry")
  public RegistryConfig registryConfig() {
    // 连接注册中心配置
    RegistryConfig registry = new RegistryConfig();
	return registry;
  }

/**
  * 默认基于dubbo协议提供服务
  * 
  * @return
  */
  @Bean
  @ConfigurationProperties(prefix = "dubbo.protocol")
  public ProtocolConfig protocolConfig() {
	// 服务提供者协议配置
	ProtocolConfig protocolConfig = new ProtocolConfig();
	return protocolConfig;
  }

  @Bean(name = "defaultProvider")
  public ProviderConfig providerConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig,
	ProtocolConfig protocolConfig) {
	ProviderConfig providerConfig = new ProviderConfig();
	providerConfig.setApplication(applicationConfig);
	providerConfig.setRegistry(registryConfig);
	providerConfig.setProtocol(protocolConfig);
	return providerConfig;
  }
```
### 4. 启动服务
```
@SpringBootApplication
@ComponentScan(basePackages = "com.alibaba.dubbo.demo")
@ImportAutoConfiguration(ConsumerConfiguration.class)
public class MainApplication implements EmbeddedServletContainerCustomizer{
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MainApplication.class, args);
	}

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.setPort(8082);
	}
}
```
### 5. 控制台输出
```
            dddddddd                 bbbbbbbb           bbbbbbbb                             
            d::::::d                 b::::::b           b::::::b                             
            d::::::d                 b::::::b           b::::::b                             
            d::::::d                 b::::::b           b::::::b                             
            d:::::d                   b:::::b            b:::::b                             
    ddddddddd:::::d uuuuuu    uuuuuu  b:::::bbbbbbbbb    b:::::bbbbbbbbb       ooooooooooo   
  dd::::::::::::::d u::::u    u::::u  b::::::::::::::bb  b::::::::::::::bb   oo:::::::::::oo 
 d::::::::::::::::d u::::u    u::::u  b::::::::::::::::b b::::::::::::::::b o:::::::::::::::o
d:::::::ddddd:::::d u::::u    u::::u  b:::::bbbbb:::::::bb:::::bbbbb:::::::bo:::::ooooo:::::o
d::::::d    d:::::d u::::u    u::::u  b:::::b    b::::::bb:::::b    b::::::bo::::o     o::::o
d:::::d     d:::::d u::::u    u::::u  b:::::b     b:::::bb:::::b     b:::::bo::::o     o::::o
d:::::d     d:::::d u::::u    u::::u  b:::::b     b:::::bb:::::b     b:::::bo::::o     o::::o
d:::::d     d:::::d u:::::uuuu:::::u  b:::::b     b:::::bb:::::b     b:::::bo::::o     o::::o
d::::::ddddd::::::ddu:::::::::::::::uub:::::bbbbbb::::::bb:::::bbbbbb::::::bo:::::ooooo:::::o
 d:::::::::::::::::d u:::::::::::::::ub::::::::::::::::b b::::::::::::::::b o:::::::::::::::o
  d:::::::::ddd::::d  uu::::::::uu:::ub:::::::::::::::b  b:::::::::::::::b   oo:::::::::::oo 
   ddddddddd   ddddd    uuuuuuuu  uuuubbbbbbbbbbbbbbbb   bbbbbbbbbbbbbbbb      ooooooooooo   

banner created by: http://patorjk.com/software/taag/      

....

2016-11-26 22:42:37.371  INFO 13428 --- [           main] com.alibaba.dubbo.config.AbstractConfig  :  [DUBBO] Export dubbo service com.alibaba.dubbo.demo.DemoService to local registry, dubbo version: 2.5.3, current host: 127.0.0.1
2016-11-26 22:42:37.371  INFO 13428 --- [           main] com.alibaba.dubbo.config.AbstractConfig  :  [DUBBO] Export dubbo service com.alibaba.dubbo.demo.DemoService to url dubbo://192.168.0.140:20880/com.alibaba.dubbo.demo.DemoService?anyhost=true&application=spring-boot-dubbo-provier&dubbo=2.5.3&interface=com.alibaba.dubbo.demo.DemoService&methods=sayHello&pid=13428&side=provider&timestamp=1480171357315, dubbo version: 2.5.3, current host: 127.0.0.1
2016-11-26 22:42:37.371  INFO 13428 --- [           main] com.alibaba.dubbo.config.AbstractConfig  :  [DUBBO] Register dubbo service com.alibaba.dubbo.demo.DemoService url dubbo://192.168.0.140:20880/com.alibaba.dubbo.demo.DemoService?anyhost=true&application=spring-boot-dubbo-provier&dubbo=2.5.3&interface=com.alibaba.dubbo.demo.DemoService&methods=sayHello&pid=13428&side=provider&timestamp=1480171357315 to registry registry://127.0.0.1:2181/com.alibaba.dubbo.registry.RegistryService?application=spring-boot-dubbo-provier&dubbo=2.5.3&pid=13428&registry=zookeeper&timestamp=1480171357308, dubbo version: 2.5.3, current host: 127.0.0.1
2016-11-26 22:42:37.500  INFO 13428 --- [           main] c.a.d.remoting.transport.AbstractServer  :  [DUBBO] Start NettyServer bind /0.0.0.0:20880, export /192.168.0.140:20880, dubbo version: 2.5.3, current host: 127.0.0.1
2016-11-26 22:42:37.513  INFO 13428 --- [           main] c.a.d.r.zookeeper.ZookeeperRegistry      :  [DUBBO] Load registry store file C:\Users\Administrator\.dubbo\dubbo-registry-127.0.0.1.cache, data: {com.alibaba.dubbo.demo.DemoService=empty://192.168.0.140/com.alibaba.dubbo.demo.DemoService?application=spring-boot-dubbo-consumer&category=configurators&dubbo=2.5.3&interface=com.alibaba.dubbo.demo.DemoService&methods=sayHello&pid=676&side=consumer&timestamp=1480169950158 empty://192.168.0.140/com.alibaba.dubbo.demo.DemoService?application=spring-boot-dubbo-consumer&category=routers&dubbo=2.5.3&interface=com.alibaba.dubbo.demo.DemoService&methods=sayHello&pid=676&side=consumer&timestamp=1480169950158 dubbo://192.168.0.140:20880/com.alibaba.dubbo.demo.DemoService?anyhost=true&application=spring-boot-dubbo-provier&dubbo=2.5.3&interface=com.alibaba.dubbo.demo.DemoService&methods=sayHello&pid=7616&side=provider&timestamp=1480169937812, cn.ma.dubbo.api.UserService:1.0.0=empty://192.168.0.140:20880/cn.ma.dubbo.api.UserService?anyhost=true&application=springboot-provider&category=configurators&check=false&default.delay=-1&default.retries=1&default.timeout=3000&dubbo=2.5.3&interface=cn.ma.dubbo.api.UserService&logger=slf4j&methods=Save&pid=11980&revision=1.0.0&side=provider&threads=200&timestamp=1480162095342&version=1.0.0, cn.ma.dubbo.api.UserService=empty://192.168.0.140/cn.ma.dubbo.api.UserService?application=demo-dubbo&category=configurators&dubbo=2.5.3&interface=cn.ma.dubbo.api.UserService&logger=slf4j&methods=Save&pid=12688&side=consumer&timeout=5000&timestamp=1480144802564 empty://192.168.0.140/cn.ma.dubbo.api.UserService?application=demo-dubbo&category=routers&dubbo=2.5.3&interface=cn.ma.dubbo.api.UserService&logger=slf4j&methods=Save&pid=12688&side=consumer&timeout=5000&timestamp=1480144802564 empty://192.168.0.140/cn.ma.dubbo.api.UserService?application=demo-dubbo&category=providers&dubbo=2.5.3&interface=cn.ma.dubbo.api.UserService&logger=slf4j&methods=Save&pid=12688&side=consumer&timeout=5000&timestamp=1480144802564}, dubbo version: 2.5.3, current host: 127.0.0.1

....

2016-11-26 22:42:37.608  INFO 13428 --- [           main] c.a.d.r.zookeeper.ZookeeperRegistry      :  [DUBBO] Register: dubbo://192.168.0.140:20880/com.alibaba.dubbo.demo.DemoService?anyhost=true&application=spring-boot-dubbo-provier&dubbo=2.5.3&interface=com.alibaba.dubbo.demo.DemoService&methods=sayHello&pid=13428&side=provider&timestamp=1480171357315, dubbo version: 2.5.3, current host: 127.0.0.1
2016-11-26 22:42:37.712  INFO 13428 --- [           main] c.a.d.r.zookeeper.ZookeeperRegistry      :  [DUBBO] Subscribe: provider://192.168.0.140:20880/com.alibaba.dubbo.demo.DemoService?anyhost=true&application=spring-boot-dubbo-provier&category=configurators&check=false&dubbo=2.5.3&interface=com.alibaba.dubbo.demo.DemoService&methods=sayHello&pid=13428&side=provider&timestamp=1480171357315, dubbo version: 2.5.3, current host: 127.0.0.1
2016-11-26 22:42:37.786  INFO 13428 --- [           main] c.a.d.r.zookeeper.ZookeeperRegistry      :  [DUBBO] Notify urls for subscribe url provider://192.168.0.140:20880/com.alibaba.dubbo.demo.DemoService?anyhost=true&application=spring-boot-dubbo-provier&category=configurators&check=false&dubbo=2.5.3&interface=com.alibaba.dubbo.demo.DemoService&methods=sayHello&pid=13428&side=provider&timestamp=1480171357315, urls: [empty://192.168.0.140:20880/com.alibaba.dubbo.demo.DemoService?anyhost=true&application=spring-boot-dubbo-provier&category=configurators&check=false&dubbo=2.5.3&interface=com.alibaba.dubbo.demo.DemoService&methods=sayHello&pid=13428&side=provider&timestamp=1480171357315], dubbo version: 2.5.3, current host: 127.0.0.1
2016-11-26 22:42:37.915  INFO 13428 --- [           main] s.w.s.m.m.a.RequestMappingHandlerAdapter : Looking for @ControllerAdvice: org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@1dd4eda: startup date [Sat Nov 26 22:42:34 CST 2016]; root of context hierarchy
2016-11-26 22:42:37.959  INFO 13428 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/error]}" onto public org.springframework.http.ResponseEntity<java.util.Map<java.lang.String, java.lang.Object>> org.springframework.boot.autoconfigure.web.BasicErrorController.error(javax.servlet.http.HttpServletRequest)
2016-11-26 22:42:37.959  INFO 13428 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/error],produces=[text/html]}" onto public org.springframework.web.servlet.ModelAndView org.springframework.boot.autoconfigure.web.BasicErrorController.errorHtml(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)
2016-11-26 22:42:37.978  INFO 13428 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/webjars/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2016-11-26 22:42:37.978  INFO 13428 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2016-11-26 22:42:38.001  INFO 13428 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**/favicon.ico] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2016-11-26 22:42:38.069  INFO 13428 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2016-11-26 22:42:38.114  INFO 13428 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8081 (http)
2016-11-26 22:42:38.118  INFO 13428 --- [           main] com.alibaba.dubbo.demo.MainApplication   : Started MainApplication in 3.506 seconds (JVM running for 3.758)
```