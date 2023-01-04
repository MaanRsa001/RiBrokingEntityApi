package com.maan.insurance.service.impl.jasper;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiObjectFactoryBean;

@Configuration
public class JasperConfiguration {
	
//	@Value("${common.path}")
//	private String commonPath;
	
	//@Value("${spring.datasource.jndi}")
	//private String datasourcebyjndi; 
	 
	// Enable This Value For run in Jndi Server
/*	@Value("${spring.datasource.jndi-name}") */ 
	private String jndiDatasource;
	
//	public String getCommonPath() {
//		return commonPath;		
//	}
	
	private static String classpathof;
	static{
		classpathof=(JasperConfiguration.class).getProtectionDomain().getCodeSource().getLocation().getPath();
	}
	public String getImagePath() {
		return classpathof+"/report/images/";
	} 
	
	private DataSource getDataSourceFromJNDI()  { 
    	try {
    		JndiObjectFactoryBean bean = new JndiObjectFactoryBean();    
    		bean.setJndiName(jndiDatasource);     		
    		bean.setProxyInterface(DataSource.class);
    		bean.setLookupOnStartup(false);
    		bean.afterPropertiesSet();
    		return (DataSource) bean.getObject();
		} catch(Exception e) {
			e.printStackTrace();
		}
    	return null;
	}
	
	// Enable This 4 Value For run in Application properties 
	@Value("${spring.datasource.driverClassName}")
	private String driverclassname;
	@Value("${spring.datasource.url}")
	private String datasourceurl;
	@Value("${spring.datasource.username}")
	private String datausername;
	@Value("${spring.datasource.password}")
	private String datapassword;
	
	
	
	
	
	
	private DataSource getDataSourceFromSpring()  { 
    	try {
    			DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
    	        dataSourceBuilder.driverClassName(driverclassname);
    	        dataSourceBuilder.url(datasourceurl);
    	        dataSourceBuilder.username(datausername);
    	        dataSourceBuilder.password(datapassword);
    	        return dataSourceBuilder.build();    	
		} catch(Exception e) {
			e.printStackTrace();
		}
    	return null;
	}
	
	 
	public DataSource getDataSourceForJasper() {
		return getDataSourceFromSpring();
	}
//		if("N".equals(datasourcebyjndi))
//				return getDataSourceFromSpring();
//		else 
//				return getDataSourceFromJNDI();
//	}


	
}
