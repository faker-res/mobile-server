package com.lzkj.mobile.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class MySpringMVCConfig extends WebMvcConfigurationSupport {
	@Autowired
	private SignatureCheckInterceptor signatureCheckInterceptor;

	//拦截器放行的url
	private final static String[] excludePath = {"/swagger-resources/**", "/swagger-ui.html", "/v2/**", "/webjars/**",
			"/mobileInterface/payCallBack","/mobileInterface/payPageLoad/submit","/mobileInterface/updateMerchantOrderId",
			"/mobileInterface/updatePassagewayResponse","/mobileInterface/addGameRecord","/mobileInterface/getActivityType",
			"/mobileInterface/getActivityListByMobile","/mobileInterface/getShareUrl","/agentSystem/updateResversion"
	};

	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(signatureCheckInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns(excludePath);
	}

	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
		return new StringHttpMessageConverter(Charset.forName("UTF-8"));
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(responseBodyConverter());
		// 这里必须加上加载默认转换器，不然bug玩死人，并且该bug目前在网络上似乎没有解决方案
		// 百度，谷歌，各大论坛等。你可以试试去掉。
		addDefaultHttpMessageConverters(converters);
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(false);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//因为继承了WebMvcConfigurationSupport会覆盖springboot默认的自动装配
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}
