package com.nws.api.filters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nws.api.core.ISQLConstant;
import com.nws.api.esb.ConnectionHelper;
import com.nws.api.esb.IConstants;
import com.nws.api.model.ConsumerServiceDetail;
import com.nws.api.utils.DBUtils;


public class AuthenticationFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	public static HashMap<String, String> authMap = new HashMap<>();
	private static final long TIMESTAMP_DURATION = 60;

	public String validateRequestParams(Map<String, String> m) {
		logger.info("inside request param validation");
		String requestId = m.get("request-id");
		// System.out.println("Request id--" + IConstants.REQUEST_ID);
		String esbCategoryCode = m.get("esb-category-code");
		String consumerCode = m.get("consumer-code");
		String authKey = m.get("auth-key");
		String requestTimestamp = m.get("request-timestamp");

		try {
			return validateHeaders(authKey, consumerCode, esbCategoryCode, requestTimestamp, requestId);
		} catch (Exception e) {

			e.printStackTrace();
			return "Invalid Request Params";
		}

	}

	private String validateHeaders(String apiKey, String consumer_code, String esb_category_code,
			String request_timestamp, String request_id) throws Exception {
		if (apiKey == null || apiKey.trim().isEmpty()) {
			logger.error("Invalid api key");
			return "1015";
		} else if (esb_category_code == null || esb_category_code.trim().isEmpty()) {
			logger.error("Invalid category code");
			return "1016";
		} else if (consumer_code == null || consumer_code.trim().isEmpty()) {
			logger.error("Invalid consumer code");
			return "1017";
		} else if (request_timestamp == null || request_timestamp.trim().isEmpty()) {
			logger.error("Invalid timestamp");
			return "1018";
		} else if (request_id == null || request_id.trim().isEmpty()) {
			logger.error("Invalid request id");
			return "1019";
		} else if (request_id.length() > 36) {
			logger.error("Invalid length of request id");
			return "1020";
		}
		return "SUCCESS";
	}
	 

	
	public String timeStampValidation(String requestTimeStamp) throws ParseException {

		logger.info("In Timestamp validation");
		try {
			long reqTimestampDiff = ((new Timestamp(System.currentTimeMillis()).getTime()
					- new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestTimeStamp).getTime()) / 1000);
			// System.out.println("Time difference is " + reqTimestampDiff);
			long timestampDiff = Long.parseLong(DBUtils.properties.get("timestampDifference"));
			logger.info("The timestamp difference is--", reqTimestampDiff);
			
			if(reqTimestampDiff<0)
			{
				reqTimestampDiff=((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestTimeStamp).getTime()) 
					-(new Timestamp(System.currentTimeMillis()).getTime()))/1000;
				if(requestTimeStamp!= null && (reqTimestampDiff > timestampDiff)){
					{
						return "Invalid Timestamp";
				    }
					
				}
                           
			}
			 if (requestTimeStamp != null
						&& (reqTimestampDiff > timestampDiff || reqTimestampDiff < 0)) {
					//throw new ESBException(1013, "Invalid Timestamp - " + vendorRequestTime);
				 return "Invalid Timestamp";
				}
			 return "SUCCESS";
			/*
			 * if (requestTimeStamp != null && (reqTimestampDiff > timestampDiff)) {
			 * 
			 * if(reqTimestampDiff < 0) {
			 * 
			 * if(reqTimestampDiff < timestampDiff) {
			 * 
			 * }else { logger.error("Inavalid timestamp "); return "Invalid Timestamp"; } }
			 * logger.error("Inavalid timestamp "); return "Invalid Timestamp"; } else {
			 * logger.info("timestamp validated"); return "SUCCESS"; }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			return "Invalid Date Format";
		}

	}

	public String validateAuthKey(String consumerCode, String authKey, String channelCode) throws Exception {

		logger.info("Validating auth key for consumerCode {} with auth key {}", consumerCode, authKey);
		String dbKey = getAuthKeyFromDB(consumerCode);
		logger.info("Auth Key from DB for Consumer code {} is  {}", consumerCode, dbKey);
		if (dbKey != null) {
			if (dbKey.equals(authKey)) {
				return "SUCCESS";
			} else {
				logger.info("Auth key Validated false");
				return "Invalid Auth Key";
			}
		}

		return "Consumer not present or key not generated";

	}

	public String getAuthKeyFromDB(String consumerCode) {
		logger.info("Inside getAuthKeyFromDB..");
		ConnectionHelper conHelper = new ConnectionHelper();
		Connection con = null;
		String key = "";
		String query = ISQLConstant.GET_CONSUMER_AUTH_KEY;
		try {
			con = conHelper.getConnection();

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, consumerCode);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				key = rs.getString(1);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			logger.error("Error while getting AuthKey {}" + e);
			e.printStackTrace();

		} finally {
			conHelper.releaseConnection(con);
		}
		return key;
	}

	public String authenticate(String apiKey, String consumerCode, String esb_category_code, String esbServiceCode)
			throws Exception {
		System.out.println("consumer code" + consumerCode);
		String dbKey = getAuthKeyFromDB(consumerCode);
		System.out.println("dbkey   " + dbKey);
		// System.out.println();

		if(dbKey!=null) {
			if (apiKey.equals(dbKey)) {
				authMap.put(consumerCode, dbKey);
				logger.info("Whitelist Process Starts here....");
				return isWhiteListed(consumerCode, esbServiceCode, esb_category_code);
			} else {
				logger.error("Authentication failed");
				return "Authentication failed";
			}
		}
		
		return "Consumer missing or Consumer key not generated.";
	}

	public String isWhiteListed(String consumerCode, String serviceCode, String esb_category_code) throws Exception {
		logger.info("Inside is whiteListed");
		ConsumerServiceDetail consumerServiceDetails = new ConsumerServiceDetail();
		consumerServiceDetails = checkConsumerAccess(consumerCode, serviceCode, esb_category_code);

		if (null == consumerServiceDetails) {
			logger.error("Access Denied.");
			return "Access Denied";

		} else if (!consumerServiceDetails.getStatus().equals("ACTIVE")) {
			return consumerServiceDetails.getStatus();
		} else {

			IConstants.CONSUMER_CODE = consumerServiceDetails.getConsumerCode();
			IConstants.ESB_SERVICE_CODE = consumerServiceDetails.getEsbServiceCode();
			IConstants.ESB_CATEGORY_CODE = consumerServiceDetails.getEsbCategoryCode();
			IConstants.DATA_ENCRYPTION = consumerServiceDetails.getDataEncryption();
		}
		return "SUCCESS";

	}

	public ConsumerServiceDetail checkConsumerAccess(String consumerCode, String serviceCode, String esb_category_code)
			throws Exception {
		ConnectionHelper conHelper = new ConnectionHelper();
		Connection con = null;

		/*
		 * ERROR CODES: 1003 INACTIVE SERVICE 1004 INACTIVE CHANNEL 1005 INACTIVE VENDOR
		 * 1009 VENDOR BLOCKED 1011 CHANNEL BLOCKED 1012 SERVICE BLOCKED 1006 CHANNEL
		 * BLOCKED FOR VENDOR
		 */
		System.out.println("Service code is " + serviceCode);
		String query = ISQLConstant.GET_CHECK_SERVICE_WHITELISTING;

		try {
			con = conHelper.getConnection();

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, consumerCode);
			stmt.setString(2, esb_category_code);
			stmt.setString(3, consumerCode);
			stmt.setString(4, serviceCode);
			stmt.setString(5, esb_category_code);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				ConsumerServiceDetail csDetails = new ConsumerServiceDetail();
				logger.debug("Consumer allowed for {}", serviceCode);

				csDetails.setConsumerCode(rs.getString(1));
				csDetails.setEsbServiceCode(rs.getString(2));
				csDetails.setEsbCategoryCode(rs.getString(3));

				csDetails.setDataEncryption(rs.getString(4));
				csDetails.setStatus(rs.getString(5));

				return csDetails;
			}

			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.getMessage();
		} finally {
			conHelper.releaseConnection(con);
		}
		return null;
	}


}
