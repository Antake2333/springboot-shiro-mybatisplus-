package com.antake.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Antake
 * @date 2020/4/30
 * @description this is description
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig {
    @Value("${swagger.enable}")
    private boolean swaggerEnable;
    @Bean
    public Docket docket(){
        List<Parameter> parameterList=new ArrayList<>();
        ParameterBuilder accessTokenBuilder = new ParameterBuilder();
        accessTokenBuilder.name("authorization").description("swagger调用（模拟传入用户认证凭证authorization）")
                .modelRef(new ModelRef("String"))
                .parameterType("header")
                .required(false);
        parameterList.add(accessTokenBuilder.build());
        ParameterBuilder refreshTokenBuilder = new ParameterBuilder();
        refreshTokenBuilder.name("refreshToken").description("swagger调用（模拟传入用户认证凭证refreshToken）")
                .modelRef(new ModelRef("String"))
                .parameterType("header")
                .required(false);
        parameterList.add(refreshTokenBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.antake.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(parameterList)
                .enable(swaggerEnable);
    }
    private ApiInfo apiInfo(){
        ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
        apiInfoBuilder.title("Antake Springboot+Shiro+MybatisPlus脚手架")
                .description("脚手架接口文档接口文档")
                .termsOfServiceUrl("https://xxxxx")
                .version("1.0");
        return apiInfoBuilder.build();
    }
}

