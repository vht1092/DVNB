package com.dvnb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Dung de ho tro get value tu file config cho component khong quan ly boi
 * Spring
 */
@Component
public class SpringConfigurationValueHelper {

	@Value("${path.file.root}")
	private String pathFileRoot;

	@Value("${time.refresh.content}")
	private int sTimeRefreshContent;

	@Value("${spring.datasource.ebank.url}")
	private String urlDatasourceEbank;
	
	@Value("${spring.datasource.ebank.username}")
	private String usernameDatasourceEbank;
	
	@Value("${spring.datasource.ebank.password}")
	private String passwordDatasourceEbank;
	
	@Value("${spring.datasource.ebank.turnon}")
	private String turnonDatasourceEbank;
	
	@Value("${spring.datasource.fdseb.url}")
	private String urlDatasourceFdseb;
	
	@Value("${spring.datasource.fdseb.username}")
	private String usernameDatasourceFdseb;
	
	@Value("${spring.datasource.fdseb.password}")
	private String passwordDatasourceFdseb;
	
	@Value("${spring.datasource.fdseb.turnon}")
	private String turnonDatasourceFdseb;
	
	@Value("${spring.datasource.im.url}")
	private String urlDatasourceIm;
	
	@Value("${spring.datasource.im.username}")
	private String usernameDatasourceIm;
	
	@Value("${spring.datasource.im.password}")
	private String passwordDatasourceIm;
	
	@Value("${spring.datasource.im.turnon}")
	private String turnonDatasourceIm;
	
	private String cardtype;
	
	private String ngayAdv;
	
	public String appname;
	
	public int sTimeRefreshContent() {
		return sTimeRefreshContent;
	}

	public String getPathFileRoot() {
		return pathFileRoot;
	}

	public String getUrlDatasourceEbank() {
		return urlDatasourceEbank;
	}

	public String getUsernameDatasourceEbank() {
		return usernameDatasourceEbank;
	}

	public String getTurnonDatasourceEbank() {
		return turnonDatasourceEbank;
	}

	public String getPasswordDatasourceEbank() {
		return passwordDatasourceEbank;
	}

	public String getUrlDatasourceIm() {
		return urlDatasourceIm;
	}

	public String getUsernameDatasourceIm() {
		return usernameDatasourceIm;
	}

	public String getPasswordDatasourceIm() {
		return passwordDatasourceIm;
	}

	public String getTurnonDatasourceIm() {
		return turnonDatasourceIm;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getUrlDatasourceFdseb() {
		return urlDatasourceFdseb;
	}

	public String getUsernameDatasourceFdseb() {
		return usernameDatasourceFdseb;
	}

	public String getPasswordDatasourceFdseb() {
		return passwordDatasourceFdseb;
	}

	public String getTurnonDatasourceFdseb() {
		return turnonDatasourceFdseb;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getNgayAdv() {
		return ngayAdv;
	}

	public void setNgayAdv(String ngayAdv) {
		this.ngayAdv = ngayAdv;
	}
	
	

}
