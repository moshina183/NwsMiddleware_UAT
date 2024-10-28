package com.nws.api.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.nws.api.esb.IConstants;

@Component
public class DBUtils {
	
	@Value("${esb-db-classname}")
	private String classname;

	@Value("${esb-db-url}")
	private String dbUrl;

	@Value("${esb-db-username}")
	private String dbUsername;

	@Value("${esb-db-password}")
	private String dbPassword;
		@Value("${timestamp.duration}")
	private String timestampDiff;
	
	
	@Value("${content-length}")
	private String contentLength;
		public static Map<String, String> properties = new HashMap<>();

	@Bean
	public void loadProperties() {
		System.out.println("Loading properties......");
		properties.put(IConstants.ORACLE_CLASSNAME, classname);
		properties.put(IConstants.ORACLE_DBURL, dbUrl);
		properties.put(IConstants.ORACLE_DBUSERNAME, dbUsername);
		properties.put(IConstants.ORACLE_DBPASSWORD, dbPassword);
		properties.put("timestampDifference", timestampDiff);
		/*
		 * properties.put(IConstants.AMR_ORACLE_CLASSNAME, amrOracleclassname);
		 * properties.put(IConstants.AMR_ORACLE_DBURL, amrOracledbUrl);
		 * properties.put(IConstants.AMR_ORACLE_USERNAME, amrOracledbUsername);
		 * properties.put(IConstants.AMR_ORACLE_PASSWORD, amrOracledbPassword);
		 * properties.put(IConstants.AMR_SQLSERVER_CLASSNAME, sqlServerClassname);
		 * properties.put(IConstants.AMR_SQLSERVER_DBURL, amrSqlServerUrl);
		 * properties.put(IConstants.AMR_SQL_SERVER_DBUSERNAME, sqlServerUsername);
		 * properties.put(IConstants.AMR_SQL_SERVER_DBPASSWORD, sqlServerPassword);
		 */
		properties.put(IConstants.CONTENT_LENGTH, contentLength);
		/*
		 * properties.put(IConstants.MMINTEGRATION_ORACLE_CLASSNAME,
		 * mmintegrationOracleClassname);
		 * properties.put(IConstants.MMINTEGRATION_ORACLE_DBURL,
		 * mmintegrationOracleDbUrl);
		 * properties.put(IConstants.MMINTEGRATION_ORACLE_USERNAME,
		 * mmintegrationOracleDbUsername);
		 * properties.put(IConstants.MMINTEGRATION_ORACLE_PASSWORD,
		 * mmintegrationOracleDbPassword);
		 */
		System.out.println("Properties loaded");
	}


}

