package com.nws.api.processors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nws.api.core.ISQLConstant;
import com.nws.api.esb.ConnectionHelper;
import com.nws.api.esb.IConstants;
import com.nws.api.model.TXNAuditLogs;
import com.nws.api.model.TXNAuditSubRequestsLogs;


public class ExceptionProcessor {
	
private static final Logger logger = LoggerFactory.getLogger(ExceptionProcessor.class);
	
	public String process(TXNAuditLogs auditLogs) {

		
		try {
			logger.info("Auditing records in AuditDataProcessor.");
			
			Timestamp esb_resp_time=new Timestamp(System.currentTimeMillis());
			ConnectionHelper conHelper=new ConnectionHelper();
			ExecutorService asyncExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			
			asyncExecutor.execute(() ->{
				Connection con = null;
				try {
					con = conHelper.getConnection();
					String data_encryption = auditLogs.getDataEncryption();
					String esb_resp_msg=auditLogs.getResponseMsg();
					String target_resp_msg= auditLogs.getTargetResponseMsg();
					String esb_req_msg= auditLogs.getRequestMsg();
					String target_req_msg=auditLogs.getTargetRequesMsg();
					String request_params ="";

					if(data_encryption!=null && data_encryption.trim().equals("Y")){
						esb_resp_msg="Please contact administrator";
						target_resp_msg="Please contact administrator";
						esb_req_msg="Please contact administrator";
						target_req_msg="Please contact administrator";
						request_params = "Please contact administrator";
					}
					
					PreparedStatement ps = con.prepareStatement(ISQLConstant.INSERT_INTO_AUDIT_LOGS);
					ps.setString(1, auditLogs.getEsbReqId());
					ps.setString(2, auditLogs.getEsbServiceCode());
					ps.setString(3, auditLogs.getConsumerCode());
			//		ps.setString(4, ex.getIn().getHeader(IConstants.ESB_CHANNEL_CODE,String.class));
					ps.setTimestamp(4, auditLogs.getRequestTime());
					ps.setTimestamp(5, esb_resp_time);
					ps.setString(6, "");
					ps.setString(7,esb_resp_msg);
					ps.setString(8, IConstants.FAILED);
					ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
					ps.setString(10, auditLogs.getEsbCategoryCode());
					ps.setString(11, "Y");
					ps.setString(12, esb_req_msg);
					ps.setString(13, auditLogs.getRequestUrl());
					ps.setString(14, "VIN-ESB"); //REMARKS
					ps.setTimestamp(15,  auditLogs.getTargetRequestTime());
					ps.setString(16, target_req_msg);
					ps.setTimestamp(17, auditLogs.getTargetResponseTime());
					ps.setString(18, target_resp_msg);
					ps.setString(19, auditLogs.getTargetResponseCode());
					ps.setString(20, auditLogs.getEsbCategoryCode());
					ps.setString(21, request_params);
					ps.setString(22, auditLogs.getRequestId());
			//		ps.setBlob(13, new ByteArrayInputStream(ex.getProperty(IConstants.ESB_REQUEST_PAYLOAD,String.class).getBytes()));
					
					ps.executeUpdate();
					logger.info("Auditing in database.");
					ps.close();
					ps = null;
				} catch (Exception e) {
					logger.error("Exception while Auditing records {}",e.getLocalizedMessage());
				} finally {
					conHelper.releaseConnection(con);
				}
			});
			
					
			asyncExecutor.shutdown();
			try {
			while (!asyncExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.HOURS)) {
			logger.info("Awaiting completion of threads.");
			}
			} catch (InterruptedException e) {
			e.printStackTrace();
			}
			
			//return esbRespMessage;
			//return body;
		} catch (Exception e) {
			logger.error("Error in AuditDataProcessor:",e);
			e.printStackTrace();
		}
		
		return "Error in AuditDataProcessor. Contact administrator..";
	}
	
	
	
	
	
	
public String InternalsubAuditRequestsLogs(TXNAuditSubRequestsLogs subauditLogs) {
		
		

		try {
			logger.info("Auditing records in AuditDataProcessor.");
			
			
			ConnectionHelper conHelper=new ConnectionHelper();
			ExecutorService asyncExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			
			asyncExecutor.execute(() ->{
				Connection con = null;
				try {
					con = conHelper.getConnection();
					String data_encryption = subauditLogs.getSubDataEncryption();
					String esb_resp_msg=subauditLogs.getSubResponseMsg();
					String target_resp_msg= subauditLogs.getSubTargetResponseMsg();
					
					String esb_req_msg=subauditLogs.getSubRequestMsg();
					String target_req_msg=subauditLogs.getSubTargetRequesMsg();
					String request_params ="";

					if(data_encryption!=null && data_encryption.trim().equals("Y")){
						esb_resp_msg="Please contact administrator";
						target_resp_msg="Please contact administrator";
						esb_req_msg="Please contact administrator";
						target_req_msg="Please contact administrator";
						request_params = "Please contact administrator";
					}
					
					PreparedStatement ps = con.prepareStatement(ISQLConstant.INSERT_INTO_SUBREQUESTS_AUDIT_LOGS);
					ps.setString(1,subauditLogs.getSubRequestId());
					ps.setString(2, subauditLogs.getSubEsbServiceCode());
					ps.setString(3, subauditLogs.getSubConsumerCode());
					ps.setTimestamp(4, subauditLogs.getSubRequestTime());
					ps.setTimestamp(5, subauditLogs.getSubResponseTime());
					ps.setString(6, subauditLogs.getSubTargetResponseCode());
					ps.setString(7,esb_resp_msg);
					ps.setString(8, IConstants.FAILED);
					ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
					ps.setString(10,subauditLogs.getSubEsbCategoryCode());
					ps.setString(11, "Y");
					ps.setString(12, esb_req_msg);
					ps.setString(13, subauditLogs.getSubRequestUrl());
					ps.setString(14, "VIN-ESB"); //REMARKS
					ps.setTimestamp(15, subauditLogs.getSubTargetRequestTime());
					ps.setString(16, target_req_msg);
					ps.setTimestamp(17, subauditLogs.getSubTargetResponseTime());
					ps.setString(18, target_resp_msg);
					ps.setString(19, subauditLogs.getSubTargetResponseCode());
					ps.setString(20, subauditLogs.getSubEsbCategoryCode());
				    ps.setString(21,subauditLogs.getSubParentRequestId());
				    ps.setInt(22,subauditLogs.getCallSequence());
					ps.setString(23, request_params);
					ps.setString(24, subauditLogs.getSubEsbReqId());
					
			
					
					ps.executeUpdate();
					logger.info("Auditing in database.");
					ps.close();
					ps = null;
				} catch (Exception e) {
					logger.error("Exception while Auditing records {}",e.getLocalizedMessage());
				} finally {
					conHelper.releaseConnection(con);
				}
			});
			
					
			asyncExecutor.shutdown();
			try {
			while (!asyncExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.HOURS)) {
			logger.info("Awaiting completion of threads.");
			}
			} catch (InterruptedException e) {
			e.printStackTrace();
			}
			
		} catch (Exception e) {
			logger.error("Error in AuditDataProcessor:",e);
			e.printStackTrace();
		}
		
		return "Error in AuditDataProcessor. Contact administrator..";
	}


}
