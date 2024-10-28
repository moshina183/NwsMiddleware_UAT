package com.nws.api.esb;

import java.util.Arrays;
import java.util.List;

public class IConstants {
	
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";
	public static final String QM="?";
	public static final String AND="&";
	public static final String MM_SERV_TYPE="Work Compleation Certificate";
	
	
	public static final String FWD_SLASH = "/";
	public static String CURRENT_AUDIT = "CURRENT_AUDIT";
	public static String REQUEST_ID = "REQUEST_ID";
	public static String CONSUMER_CODE = "CONSUMER_CODE";
	
	public static String REQUEST_TIMESTAMP = "REQUEST_TIMESTAMP";
	public static String ESB_CATEGORY_CODE = "ESB_CATEGORY_CODE";
	public static String AUTH_KEY = "x-api-key";
	public static String ESB_SERVICE_CODE="esb_service_code";
	public static String DATA_ENCRYPTION="data_encryption";
	
	public static String ORACLE_CLASSNAME = "oracleClassname";
	public static String ORACLE_DBURL = "oracledburl";
	public static String ORACLE_DBUSERNAME = "oracledbUsername";
	public static String ORACLE_DBPASSWORD = "oracledbPassword";
	
	public static String AMR_ORACLE_CLASSNAME = "amrOracleClassname";
	public static String AMR_ORACLE_DBURL = "amrOracleDBUrl";
	public static String AMR_ORACLE_USERNAME = "amrOracleUsername";
	public static String AMR_ORACLE_PASSWORD = "amrOraclePassword";
	
	public static String AMR_SQLSERVER_CLASSNAME = "sqlserverclassname";
	public static String AMR_SQLSERVER_DBURL = "sqlserverdburl";
	public static String AMR_SQL_SERVER_DBUSERNAME = "sqlserverusername";
	public static String AMR_SQL_SERVER_DBPASSWORD = "sqlserverpassword";
	
	public static String MMINTEGRATION_ORACLE_CLASSNAME="mmintegrationOracleClassname";
	public static String MMINTEGRATION_ORACLE_DBURL="mmintegrationOracleDBUrl";
	public static String MMINTEGRATION_ORACLE_USERNAME="mmintegrationOracleUsename";
	public static String MMINTEGRATION_ORACLE_PASSWORD="mmintegrationOraclePassword";
	
	public static String CONTENT_LENGTH = "content-length";
	public static List<String> urlList = Arrays.asList("/api/v1/getmeterreading",
			"/api/v1/GetGISDetails","/api/v1/sendamralarms"
			,"/api/v1/GetGISMap","/api/v1/getBillData","/api/v1/GetBillsDetails",
			"/api/v1/GetBillsSummary","/api/v1/GetBillPdf","/api/v1/GetGroupBillsInfo",
			"/api/v1/GetGroupBillDetails","api/v1/GetBill64"
			,"api/v1/InquiryPayment","/api/v1/addpay","/api/v1/getCompanyData","/api/v1/inquiryaccount",
			"/api/v1/getplotdetailsbycrookie","/api/v1/getsocialbenefitdata","/api/v1/getpersoninformation","/api/v1/getdata","/api/v1/geteddata",
			"/api/v1/getmmdata","api/v1/getintegrationdata","/api/v1/gettrafficinformaton");
	
	public static List<String> urlListV2 = Arrays.asList("/api/v2/getmeterreading",
			"/api/v2/GetGISDetails","/api/v2/sendamralarms"
			,"/api/v2/GetGISMap","/api/v2/getBillData","/api/v2/GetBillsDetails",
			"/api/v2/GetBillsSummary","/api/v2/GetBillPdf","/api/v2/GetGroupBillsInfo",
			"/api/v2/GetGroupBillDetails","/api/v2/GetBill64"
			,"api/v2/InquiryPayment","/api/v2/addpay","/api/v2/getCompanyData","/api/v2/inquiryaccount",
			"/api/v2/getplotdetailsbycrookie","/api/v2/getsocialbenefitdata","/api/v2/getpersoninformation","/api/v2/geteddata",
			"api/v2/getmmdata","api/v2/getintegrationdata","/api/v2/gettrafficinformaton","api/v2/test");
	

	public static String ESB_SUCCESS_RESPONSE_TEMPLATE_JSON="{\"responseHeader\":{\"esbStatus\":\"SUCCESS\", \"targetStatus\":\" SUCCESS\",\"esbResponseCode\":\"200\" },\"responseBody\": %s }";
			
	public static String ESB_FAILURE_RESPONSE_TEMPLATE_JSON="{\"responseHeader\":{\"esbStatus\":\"FAILURE\"},\"responseBody\":{\"esbResponseCode\":\"%s\",\"esbResponseMessage\":\"%s\"}}";
	
	public static String ESB_FAILURE_RESPONSE_TEMPLATE_JSONV1="{\"errorCode\":\"%s\",\"errorMessage\":\"%s\"}";
	
	public static String ESB_FAILURE_RESPONSE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><response><errorCode>%s</errorCode><errorMessage>%s</errorMessage></response>";
	
	public static String ESB_FAILURE_RESPONSE_XML_V2 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><response><responseHeader><esbStatus>FAILURE</esbStatus></responseHeader><responseBody><errorCode>%s</errorCode><errorMessage>%s</errorMessage></responseBody></response>";

	public static String ESB_XML_SUCCESS_RESPONSE_V2 = "<response><responseHeader><esbStatus> SUCCESS</esbStatus><targetStatus> SUCCESS </targetStatus><esbResponseCode>200</esbResponseCode><esbResponseMessage></esbResponseMessage></responseHeader><responseBody>%s</responseBody></response>";



}
