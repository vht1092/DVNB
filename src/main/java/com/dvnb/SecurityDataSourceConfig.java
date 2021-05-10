package com.dvnb;


import javax.sql.DataSource;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.vaadin.server.VaadinServlet;

/**
 * tanvh1 Oct 23, 2019
 *
 */
public class SecurityDataSourceConfig {
	private SpringConfigurationValueHelper configurationHelper;
	
    @Bean
    @ConfigurationProperties(prefix="spring.datasource.ebank")
    public DataSourceProperties securityDataSourceProperties() {
        return new DataSourceProperties();
    }
 
    @Bean
    public DataSource securityDataSourceEB() {
    	final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		
    	StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword("SMS");
        String decrypted = encryptor.decrypt(configurationHelper.getPasswordDatasourceEbank());
        
         return DataSourceBuilder.create()
        .driverClassName("oracle.jdbc.driver.OracleDriver")
        .url(configurationHelper.getUrlDatasourceEbank())
        .username(configurationHelper.getUsernameDatasourceEbank())
        .password(decrypted)
        .build();
    }
    
    @Bean
    public DataSource securityDataSourceFDSEB() {
    	final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		
    	StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword("DVNB");
        String decrypted = encryptor.decrypt(configurationHelper.getPasswordDatasourceFdseb());
        
         return DataSourceBuilder.create()
        .driverClassName("oracle.jdbc.driver.OracleDriver")
        .url(configurationHelper.getUrlDatasourceFdseb())
        .username(configurationHelper.getUsernameDatasourceFdseb())
        .password(decrypted)
        .build();
    }
    
    @Bean
    public DataSource securityDataSourceIM() {
    	final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		
    	StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword("SMS");
        String decrypted = encryptor.decrypt(configurationHelper.getPasswordDatasourceIm());
        
         return DataSourceBuilder.create()
        .driverClassName("oracle.jdbc.driver.OracleDriver")
        .url(configurationHelper.getUrlDatasourceIm())
        .username(configurationHelper.getUsernameDatasourceIm())
        .password(decrypted)
        .build();
    }
    

}
