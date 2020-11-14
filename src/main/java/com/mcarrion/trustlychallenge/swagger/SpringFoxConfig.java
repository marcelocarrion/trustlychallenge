package com.mcarrion.trustlychallenge.swagger;

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

/**
 * @author Marcelo Carrion
 */
@Configuration
@EnableSwagger2
public class SpringFoxConfig
{
	@Bean
	public Docket api()
	{
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.mcarrion.trustlychallenge.controllers"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}
	
	private ApiInfo apiInfo()
	{
		return new ApiInfoBuilder()
				.title("Trustly Technical Challenge for Developers")
				.description("This API uses web scraping techniques to obtain the total number of lines and bytes of all the files in a given Github repository, grouped by file extension.")
				.version("1.0.0")
				.contact(new Contact("Marcelo Carrion", "https://www.linkedin.com/in/marcelo-carrion/", "marcelo.tperc@gmail.com"))
				.build();
	}
}
