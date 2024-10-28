package com.nws.api.service;

import java.sql.Timestamp;
import java.util.Optional;

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
public class GetRecordGISCSHandlerVer2 {
	
	private static final Logger logger = LoggerFactory.getLogger(GetRecordGISCSHandlerVer2.class);

	@Value("${getmeterreading-target-url}")
	private String getmeterreadingTargetUurl;

	@Value("${getrecordcs-target-url}")
	private String getRecordCS;

	@Value("${gis-getrecord-target-url}")
	private String getRecordGIS;
	
	@Value("${auth-username}")
	private String authUsername;

	@Value("${auth-password}")
	private String authPassword;

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
	
	public ResponseEntity<String> getRecordGISHandler(Optional<String> accountId,Optional<String> latitude
			,Optional<String> longitude,
			HttpServletRequest request){
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getRecordGISEntity = new HttpEntity<String>(header);
		
		//TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		TXNAuditLogs auditLogs = new TXNAuditLogs();
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
		String targetURI = "";
		if (accountId.isPresent()) {
			targetURI = targetURI + IConstants.FWD_SLASH + accountId.get();
		}

		if (latitude.isPresent()) {
			targetURI = targetURI + IConstants.FWD_SLASH + latitude.get();
		}

		if (longitude.isPresent()) {
			targetURI = targetURI + IConstants.FWD_SLASH + longitude.get();
		}
		targetURI += IConstants.FWD_SLASH;
		System.out.println(targetURI);
		try {
			getRecordGISEntity = restTemplate
					.getForEntity(getRecordGIS +targetURI , String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getRecordGISEntity.getBody());
			auditLogs.setTargetResponseMsg(getRecordGISEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling GIS get Record URL {}", e.getMessage());
			e.printStackTrace();
		}
		
		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		
		String body=auditLogs.getResponseMsg();
		String esbRespMessage = String.format(IConstants.ESB_SUCCESS_RESPONSE_TEMPLATE_JSON,(body!=null && !body.trim().isEmpty())?body:"{}");
			
		return new ResponseEntity<String>(esbRespMessage, HttpStatus.OK);
	}


}
