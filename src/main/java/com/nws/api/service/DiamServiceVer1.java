package com.nws.api.service;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.nws.api.esb.IConstants;
import com.nws.api.model.TXNAuditLogs;



@Service
public class DiamServiceVer1 {
	
	private static final Logger logger = LoggerFactory.getLogger(DiamServiceVer1.class);
	
	
	@Value("${getbilldata-target-url}")
	private String getBillData;
	
	@Value("${getbillpdf-target-url}")
	private String getBillPDF;
	
	@Value("${getgbill64-target-url}")
	private String getBill64;
	
	@Value("${getbilldetails-target-url}")
	private String getBillDetails;
	
	@Value("${getbillsummary-target-url}")
	private String getBillSummary;
	
	@Value("${getgroupbilldetails-target-url}")
	private String getGroupBillDetail;
	
	@Value("${getgroupbillinfo-target-url}")
	private String getGroupBillInfo;
	
	@Value("${inquirypayment-target-url}")
	private String inquiryPaymentUrl;
	
	
	public ResponseEntity<String> getBilldataServiceHandler(String billId, String ccbAccount,HttpServletRequest request) 
	{
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getBillDataEntity = new HttpEntity<String>(header);
		
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		//TXNAuditLogs auditLogs = new TXNAuditLogs();
		logger.info("Current Time: {}", System.currentTimeMillis());
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
		String targetURI = "";
		if (!billId.isEmpty()) {
			targetURI = targetURI +IConstants.QM+"BillId="+ billId;
		}

		if (!ccbAccount.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"ccbAccount=" + ccbAccount;
		}
		
		String targetUrl=getBillData+targetURI;
		try {
			getBillDataEntity = restTemplate
					.getForEntity(targetUrl, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getBillDataEntity.getBody());
			auditLogs.setTargetResponseMsg(getBillDataEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling getbilldata URL {}", e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}

	public ResponseEntity<String> getBillPDFServiceHandler(String billID, String ccbAccount,HttpServletRequest request) 
	{
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getBillDataEntity = new HttpEntity<String>(header);
		
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		//TXNAuditLogs auditLogs = new TXNAuditLogs();
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
			
		
		String targetURI = "";
		if (!billID.isEmpty()) {
			targetURI = targetURI +IConstants.QM+"billID="+ billID;
		}

		if (!ccbAccount.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"ccbAccount=" + ccbAccount;
		}
		
		String targetUrl=getBillPDF+targetURI;
		try {
			getBillDataEntity = restTemplate
					.getForEntity(targetUrl, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getBillDataEntity.getBody());
			auditLogs.setTargetResponseMsg(getBillDataEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling getbilldata URL {}", e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}

	public ResponseEntity<String> getBill64ServiceHandler(String billId, String ccbAccount,
			HttpServletRequest request) {
		
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getBillDataEntity = new HttpEntity<String>(header);
		
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		//TXNAuditLogs auditLogs = new TXNAuditLogs();
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
		String targetURI = "";
		if (!billId.isEmpty()) {
			targetURI = targetURI +IConstants.QM+"BID="+ billId;
		}

		if (!ccbAccount.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"ccbAccount=" + ccbAccount;
		}
		
		String targetUrl=getBill64+targetURI;
		try {
			getBillDataEntity = restTemplate
					.getForEntity(targetUrl, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getBillDataEntity.getBody());
			auditLogs.setTargetResponseMsg(getBillDataEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling getbilldata URL {}", e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);

	}
	
	public ResponseEntity<String> getBillDetailServiceHandler(String ccbAccount, String Months,HttpServletRequest request) 
	{
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getBillDetailsEntity = new HttpEntity<String>(header);
		
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		//TXNAuditLogs auditLogs = new TXNAuditLogs();
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
		String targetURI = "";
		if (!ccbAccount.isEmpty()) {
			targetURI = targetURI +IConstants.QM+"ccbAccount="+ ccbAccount;
		}

		if (!Months.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"Months=" + Months;
		}
		
		String targetUrl=getBillDetails+targetURI;
		try {
			getBillDetailsEntity = restTemplate
					.getForEntity(targetUrl, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getBillDetailsEntity.getBody());
			auditLogs.setTargetResponseMsg(getBillDetailsEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling getBillDetail URL {}", e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}
	
	
	public ResponseEntity<String> getBillDetailsSummaryHandler(String ccbAccount, String Months,HttpServletRequest request) 
	{
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getBillDetailsEntity = new HttpEntity<String>(header);
		
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		//TXNAuditLogs auditLogs = new TXNAuditLogs();
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
		String targetURI = "";
		if (!ccbAccount.isEmpty()) {
			targetURI = targetURI +IConstants.QM+"ccbAccount="+ ccbAccount;
		}

		if (!Months.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"Months=" + Months;
		}
		
		String targetUrl=getBillSummary+targetURI;
		try {
			getBillDetailsEntity = restTemplate
					.getForEntity(targetUrl, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getBillDetailsEntity.getBody());
			auditLogs.setTargetResponseMsg(getBillDetailsEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling getBillDetail URL {}", e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		
		
		
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}

	public ResponseEntity<String> getGroupBillDetailsServiceHandler(String year, String month, String groupId,
			HttpServletRequest request) {
		
		
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getBillDataEntity = new HttpEntity<String>(header);
		
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		//TXNAuditLogs auditLogs = new TXNAuditLogs();
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
		String targetURI = "";
		if (!year.isEmpty()) {
			targetURI = targetURI +IConstants.QM+"year="+ year;
		}

		if (!month.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"month=" + month;
		}
		if (!month.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"GroupId=" + groupId;
		}
		
		String targetUrl=getGroupBillDetail+targetURI;
		try {
			getBillDataEntity = restTemplate
					.getForEntity(targetUrl, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getBillDataEntity.getBody());
			auditLogs.setTargetResponseMsg(getBillDataEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling getbilldata URL {}", e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
		
		
	}

	public ResponseEntity<String> getGroupBillsInfoServiceHandler(String year, String month, String groupId,
			HttpServletRequest request) {
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getBillDataEntity = new HttpEntity<String>(header);
		
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		//TXNAuditLogs auditLogs = new TXNAuditLogs();
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
		
		String targetURI = "";
		if (!year.isEmpty()) {
			targetURI = targetURI +IConstants.QM+"year="+ year;
		}

		if (!month.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"month=" + month;
		}
		if (!month.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"GroupId=" + groupId;
		}
		
		String targetUrl=getGroupBillInfo+targetURI;
		try {
			getBillDataEntity = restTemplate
					.getForEntity(targetUrl, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getBillDataEntity.getBody());
			auditLogs.setTargetResponseMsg(getBillDataEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling getbilldata URL {}", e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
		
	
	}

	public ResponseEntity<String> inquiryPaymentServiceHandler(String trackID, String gateWay,
			HttpServletRequest request) {
		
		System.out.println("ESPHandler.inquiryPaymentServiceHandler()");
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getBillDataEntity = new HttpEntity<String>(header);
		
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		//TXNAuditLogs auditLogs = new TXNAuditLogs();
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
			
		
		String targetURI = "";
		if (!trackID.isEmpty()) {
			targetURI = targetURI +IConstants.QM+"TrackID="+ trackID;
		}

		if (!gateWay.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"Gateway=" + gateWay;
		}
		
		String targetUrl=inquiryPaymentUrl+targetURI;
		try {
			getBillDataEntity = restTemplate
					.getForEntity(targetUrl, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getBillDataEntity.getBody());
			auditLogs.setTargetResponseMsg(getBillDataEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling getbilldata URL {}", e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}

	
	//Object creation
	
	
	protected RestTemplate getRestTemplate() {
		if (logger.isInfoEnabled())
			logger.info("Entered::AbstractEmailDelivery::getRestTemplate()");
		RestTemplate restTemplate = new RestTemplate(
				new BufferingClientHttpRequestFactory(getClientHttpRequestFactory()));
		return restTemplate;
	}

	private SimpleClientHttpRequestFactory getClientHttpRequestFactory() {
		SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		return clientHttpRequestFactory;
	}

	public HttpHeaders createHeaders() {
		return new HttpHeaders();

	}

}
