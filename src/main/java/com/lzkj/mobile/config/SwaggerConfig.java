package com.lzkj.mobile.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${spring.cloud.config.profile}")
    private String profile;

    @Bean
    public Docket createRestApi() {
        Predicate<RequestHandler> selector1 = RequestHandlerSelectors.basePackage("com.lzkj.mobile.v2.controller");
        boolean enable = true;
        log.info("SwaggerConfig获取spring.cloud.config.profile={}", profile);
        if("prod".equals(profile)){
            enable = false;
        }
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enable)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(Predicates.or(selector1))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("量子棋牌-业主管理后台V1.0版本--基于RESTful风格API接口文档")
                //描述
                .description("量子棋牌-业主管理后台API接口服务（API）V1.0版本")
                //创建人
                .contact(new Contact("Horus", "", ""))
                //版本号
                .version("1.0")
                .build();
    }

}
