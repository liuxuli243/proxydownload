package com.laoniu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
		.apiInfo(apiInfo())
		.select()
		.apis(RequestHandlerSelectors.basePackage("com.laoliu.controller"))
		.paths(PathSelectors.any())
		.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				// 页面标题
				.title("代理下载")
				// 创建人
				.contact(new Contact("老牛", "http://laoniu.com", "liuxuli232@sina.com"))
				// 版本号
				.version("1.0")
				// 描述
				.description("API 描述").build();
	}
}
