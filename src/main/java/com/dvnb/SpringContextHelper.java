package com.dvnb;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Dung ho tro truy cap bean cho component khong quan ly boi Spring
 * */
public class SpringContextHelper {
	private ApplicationContext context;
	public SpringContextHelper(ServletContext servletContext) {
		context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}

	public Object getBean(final String beanRef) {
		return context.getBean(beanRef);
	}
}
