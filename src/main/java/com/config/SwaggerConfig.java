package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@EnableSwagger2//开启swagger
public class SwaggerConfig {

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(true)//配置swagger的开启
                .select()
                .apis(RequestHandlerSelectors.basePackage("com"))
                //.path():过滤的路径 相当于第二层扫描
                .build();
    }


    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("冷空气业务系统")
                .description("系统接口描述")
                .version("1.0")
                .contact(new Contact("rein","http://baidu.com","843097542@qq.com"))
                .build();
    }

}