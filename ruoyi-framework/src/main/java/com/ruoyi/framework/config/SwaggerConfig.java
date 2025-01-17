package com.ruoyi.framework.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

  /**
   * 是否开启swagger
   */
  @Value("${swagger.enabled}")
  private boolean enabled;


  /**
   * 创建API
   */
  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
            // 是否启用Swagger
            .enable(enabled)
            // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
            .apiInfo(apiInfo())
            // 设置哪些接口暴露给Swagger展示
            .select()
            // 扫描所有有注解的api，用这种方式更灵活
            .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
            .paths(PathSelectors.any())
            .build()
            /* 设置安全模式，swagger可以设置访问token */
            .securitySchemes(securitySchemes())
            .securityContexts(securityContexts());
//                .pathMapping(pathMapping);
  }

  /**
   * 安全模式，这里指定token通过Authorization头请求头传递
   */
  private List<ApiKey> securitySchemes() {
    List<ApiKey> apiKeyList = new ArrayList<ApiKey>();
    apiKeyList.add(new ApiKey("Authorization", "Authorization", "header"));
    return apiKeyList;
  }

  /**
   * 安全上下文
   */
  private List<SecurityContext> securityContexts() {
    List<SecurityContext> securityContexts = new ArrayList<>();
    securityContexts.add(
            SecurityContext.builder()
                    .securityReferences(defaultAuth())
                    .forPaths(PathSelectors.regex("^(?!auth).*$"))
                    .build());
    return securityContexts;
  }

  /**
   * 默认的安全上引用
   */
  private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    List<SecurityReference> securityReferences = new ArrayList<>();
    securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
    return securityReferences;
  }


  /**
   * 添加摘要信息
   */
  private ApiInfo apiInfo() {
    // 用ApiInfoBuilder进行定制
    return new ApiInfoBuilder()
            // 设置标题
            .title("Vue管理系统_接口文档")
            // 描述
            .description("nature管理系统...")
            // 作者信息
            .contact(new Contact("ye", "http://bkywksj.xyz", "770492966@qq.com"))
            // 版本
            .version("版本号:1.0")
            .build();
  }

}
