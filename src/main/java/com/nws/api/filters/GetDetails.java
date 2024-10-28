package com.nws.api.filters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nws.api.core.ISQLConstant;
import com.nws.api.esb.ConnectionHelper;


public class GetDetails {
	
private static final Logger logger = LoggerFactory.getLogger(GetDetails.class);
	
	public String getEncryptionStatus(String esbServiceCode) throws Exception {
		logger.info("Inside getEncryption status.. for service code {}" , esbServiceCode);
		String encryption = null;
		ConnectionHelper conHelper=new ConnectionHelper();
		
		Connection con=null;
		try {
			String query = ISQLConstant.GET_SERVICE_DATA_ENCRYPTION;
			con = conHelper.getConnection();
			
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, esbServiceCode);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				logger.debug("Encryption status is {}", rs.getString(1));
				encryption = rs.getString(1);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			logger.error("Error while fetching encryption status {}", e.getLocalizedMessage());
		} finally {
			conHelper.releaseConnection(con);
		}
		
		return encryption;
	}
	
	
//	
//	public String getAggregationStatus(String esbServiceCode) throws Exception {
//		logger.info("Inside getAggragation status.. for service code {}" , esbServiceCode);
//		String aggregation = "";
//		ConnectionHelper conHelper=new ConnectionHelper();
//		PreparedStatement statement;
//		Connection con=null;
//		try {
//			String query = ISQLConstant.GET_AGGREGATION_IS_SERVICE;
//			con = conHelper.getConnection();
//			statement=con.prepareStatement(query);
//			statement.setString(1, esbServiceCode);
//			ResultSet rs=statement.executeQuery();
//			
//			while (rs.next()) {
//				logger.debug("Aggregation status is {}", rs.getString(1));
//				aggregation = rs.getString(1);
//			}
//			rs.close();
//			statement.close();
//		} catch (Exception e) {
//			logger.error("Error while fetching aggregation status {}", e.getLocalizedMessage());
//		} finally {
//			conHelper.releaseConnection(con);
//		}
//		
//		return aggregation;
//	}
	
	
	public String getAggregationStatusFromSubRequestsLogs(String esbServiceCode) throws Exception {
		logger.info("Inside getAggragation status.. for service code {}" , esbServiceCode);
		String aggregation = "";
		ConnectionHelper conHelper=new ConnectionHelper();
		PreparedStatement statement;
		Connection con=null;
		try {
			String query = ISQLConstant.GET_AGGREGATION_STATUS_FROM_SUBREQUESTS_LOG;
			con = conHelper.getConnection();
			statement=con.prepareStatement(query);
			statement.setString(1, esbServiceCode);
			ResultSet rs=statement.executeQuery();
			
			while (rs.next()) {
				logger.debug("Aggregation status is {}", rs.getString(1));
				aggregation = rs.getString(1);
			}
			rs.close();
			statement.close();
		} catch (Exception e) {
			logger.error("Error while fetching aggregation status {}", e.getLocalizedMessage());
		} finally {
			conHelper.releaseConnection(con);
		}
		
		return aggregation;
	}
	
	
	
	
	
	public String getDuplicateRequestId(String esbServiceCode) throws Exception {
		logger.info("Inside getDuplicateRequestId status.. for service code {}" , esbServiceCode);
		String duplicate = null;
		ConnectionHelper conHelper=new ConnectionHelper();
		
		Connection con=null;
		try {
			String query = ISQLConstant.GET_SERVICE_DATA_IS_DUPLICATE;
			con = conHelper.getConnection();
			
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, esbServiceCode);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				logger.debug("DuplicateRequestid status is {}", rs.getString(1));
				duplicate = rs.getString(1);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			logger.error("Error while fetching DuplicateRequestid status {}", e.getLocalizedMessage());
		} finally {
			conHelper.releaseConnection(con);
		}
		System.out.println("duplicate "+duplicate);
		return duplicate;
	}
	
	
	public boolean checkIfRecordExist(String requestId) throws Exception {
		logger.info("Inside CheckIfRecordExist status.. for service code {}" , requestId);
		boolean isRecordExist = false;
		ConnectionHelper conHelper=new ConnectionHelper();
		
		Connection con=null;
		try {
			String query = ISQLConstant.GET_SERVICE_DATA_CHEAK_IF_RECORD_EXIST;
			con = conHelper.getConnection();
			
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, requestId);
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				logger.debug("Encryption status is {}", rs.getString(1));
				isRecordExist = true;
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			logger.error("Error while fetching CheckIfRecordExist status {}", e.getLocalizedMessage());
		} finally {
			conHelper.releaseConnection(con);
		}
		System.out.println("isRecordExist "+isRecordExist);
		return isRecordExist;
	}
	
	public HashMap<String, String> getErrorCodes() throws Exception {
		logger.info("Inside get error codes.");
       ConnectionHelper conHelper=new ConnectionHelper();
		Connection con=null;
		HashMap<String,String> errorCodeMap = new HashMap<>();
		String query = ISQLConstant.GET_ALL_ERROR_CODES;
		try {
			con = conHelper.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				errorCodeMap.put(rs.getString(1), rs.getString(2).toUpperCase());
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			logger.error("Error while fetching error codes {}", e.getLocalizedMessage());
		} finally {
			conHelper.releaseConnection(con);
		}
		return errorCodeMap;
	}


}
