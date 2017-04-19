package com.epam.internship.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import com.epam.internship.converter.UserConverter;
import com.epam.internship.converter.UserEntityConverter;

@Configuration
public class GroupNewsServiceConfig {

	@Bean
	public ConversionService conversionService(){
		ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
		Set<Converter<?, ?>> converterSet = new HashSet<>();
		converterSet.add(new UserConverter());
		converterSet.add(new UserEntityConverter());
		factoryBean.setConverters(converterSet);
		factoryBean.afterPropertiesSet();
		ConversionService conversionService = factoryBean.getObject();
		return conversionService;
	}
}
