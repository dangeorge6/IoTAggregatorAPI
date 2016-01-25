package com.shoppertrak;

import com.fasterxml.classmate.TypeResolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.*;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	// prefix for api version 1
	private static final String V1 = "v1.0"; 
	
	@Autowired
	private TypeResolver typeResolver;

	/**
	 * Initialize group for API ver 1 
	 */
	@Bean
	public Docket v1Api() {
		ApiInfo info = buildApiInfo(V1);

		return new Docket(DocumentationType.SWAGGER_2)
		.useDefaultResponseMessages(true)
		.apiInfo(info)
		.groupName(V1)
        .select()
          .paths(regex(".*/"+V1+".*")) 
          .build();
	}

	
    private ApiInfo buildApiInfo(String ver) {
        return new ApiInfoBuilder()
                .title("Traffic Data Web Service")
                .description("Interview Project Assignment")                
                .contact("ShopperTrak RCT, INC.")
                .version(ver)
                .build();
    }
    
}