package com.nws.api.interceptor;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.nws.api.esb.Helper;
import com.nws.api.esb.IConstants;
import com.nws.api.filters.AuthenticationFilter;
import com.nws.api.filters.GetDetails;
import com.nws.api.model.TXNAuditLogs;
import com.nws.api.processors.AuditLogProcessor;
import com.nws.api.processors.ExceptionProcessor;
import com.nws.api.utils.DBUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class BaseInterceptor implements HandlerInterceptor{
	
	private static final Logger logger = LoggerFactory.getLogger(BaseInterceptor.class);
	GetDetails getDetails = new GetDetails();
	Map<String, String> error = null;

	public Map<String, String> getResponseMsg() throws Exception {
		error = getDetails.getErrorCodes();
		return error;
	}

	public String getValue(String responseCode) {
		String value = null;
		if (error.containsKey(responseCode)) {
			value = error.get(responseCode);
		}
		return value;

	}

	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		System.out.println("4i-Pre Handle method is Calling");
		logger.info("Inside prehandle");
		String s = request.getRequestURI();
		System.out.println("URI" + s +" : "+request.getHeader("Content-Type") +" request.getContentLength(): "+request.getContentLength());
		TXNAuditLogs auditLogs = null;
		
		if (request.getHeader("Content-Type")==null || request.getHeader("Content-Type").contains("application/json")) {
			if (s.contains("/api/v1")) {
				auditLogs = new TXNAuditLogs();
				auditLogs.setRequestTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setRequestId(UUID.randomUUID().toString());
				Helper helper = new Helper();
				AuthenticationFilter filter = new AuthenticationFilter();
				Map<String, String> headersMap = helper.getHeadersInfo(request);
				String consumerCode = headersMap.get("consumer-code");
				String reqId = headersMap.get("request-id");
				String authKey = headersMap.get("auth-key");
				String esbCategoryCode = headersMap.get("esb-category-code");
				String esbServiceCode = "";
				String uri = request.getRequestURI();
				logger.info("############Request Processing started for request-id {} and esbURL is {}", reqId, uri);
				String[] code = uri.split("/");
				int i = 0;
				boolean isUrlValid = false;
				String[] esbServiceCodeList = null;
				Iterator<String> url = IConstants.urlList.iterator();
				while (url.hasNext()) {
					String urlList = (String) url.next();
					System.out.println(urlList + "  " + urlList.contains(uri));
					if (uri.contains(urlList)) {
						esbServiceCodeList = urlList.split("/");
						isUrlValid = true;
					}
				}
				System.out.println(isUrlValid);
				if (!isUrlValid) {
					logger.info("Invalid URL sent by requestId {}", reqId);
					response.sendError(404, "Not Found");

					return false;
				}
				System.out.println("Code length ::" + code.length);
				esbServiceCode = esbServiceCodeList[esbServiceCodeList.length - 1]; 
				/*while (i < code.length && code.length >= 4 && i < 4) {
					esbServiceCode = code[i];
					i++;
				}*/
				System.out.println("esbServiceCode: "+esbServiceCode);
				GetDetails getDetails = new GetDetails();
				String dataEncryption = getDetails.getEncryptionStatus(esbServiceCode);
				auditLogs.setEsbServiceCode(esbServiceCode);

				auditLogs.setConsumerCode(consumerCode);
				auditLogs.setEsbCategoryCode(esbCategoryCode);
				auditLogs.setEsbReqId(reqId);
				auditLogs.setRequestUrl(uri);

				logger.info("Inside DDOS filtering");
				error = getResponseMsg();
				ExceptionProcessor auditLogProcessor = new ExceptionProcessor();
				if (request.getContentLength() > 0 && request.getContentLength() > Integer
						.parseInt(DBUtils.properties.get(IConstants.CONTENT_LENGTH))) {
					logger.error("Invalid content length");
					response.setHeader("Content-Type", "application/json");
					String key = "1014";// content length too large
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_TEMPLATE_JSONV1, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}

				if (dataEncryption == null) {
					logger.error("Invalid ESB service code sent by request id {} ", reqId);
					response.setHeader("Content-Type", "application/json");
					String key = "1003";// Service Inactive
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_TEMPLATE_JSONV1, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}

				auditLogs.setDataEncryption(dataEncryption);
				if (headersMap.isEmpty()) {
					auditLogProcessor.process(auditLogs);
					return false;
				}
				
				String requestParamsResponse = filter.validateRequestParams(headersMap);
				if (!requestParamsResponse.equals("SUCCESS")) {
					logger.error("Invalid or empty request params");
					if (reqId == null || reqId.trim().length() == 0) {
						logger.error("Request id not sent");
						auditLogs.setEsbReqId(esbCategoryCode + "-" + consumerCode + "-" + auditLogs.getRequestId());
					}
					response.setHeader("Content-Type", "application/json");
					String key = requestParamsResponse;
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_TEMPLATE_JSONV1, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}
				
				String requestTimeStamp = headersMap.get("request-timestamp");
				String responseTimestamp = filter.timeStampValidation(requestTimeStamp);
				if (!responseTimestamp.equals("SUCCESS")) {

					String key = "1013";
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_TEMPLATE_JSONV1, key, value);

					System.out.println("failresponse" + failresponse);

					logger.error("Invalid timestamp");
					response.setHeader("Content-Type", "application/json");
					// auditLogs.setResponseMsg("{\"errorCode\":2,\"errorMessage\":\""+responseTimestamp+"\"}");

					// response.getOutputStream().println("{\"errorCode\":2,\"errorMessage\":\""+responseTimestamp+"\"}");
					auditLogs.setResponseMsg(failresponse);
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);

					return false;
				}
				
				String checkAuthKey = filter.validateAuthKey(consumerCode, authKey, esbCategoryCode);
				if (!checkAuthKey.equals("SUCCESS")) {
					logger.error("Invalid auth key or consumer code sent by request id {}", reqId);
					String key = "1001";
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_TEMPLATE_JSONV1, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);

					// auditLogs.setResponseMsg("{\"errorCode\":3,\"errorMessage\":\"Authentication
					// Failed\"}");
					response.setHeader("Content-Type", "application/json");
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}
				
				requestParamsResponse = filter.authenticate(authKey, consumerCode, esbCategoryCode, esbServiceCode);
				if (!requestParamsResponse.equals("SUCCESS")) {
					String key = "1002";
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_TEMPLATE_JSONV1, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);

					response.setHeader("Content-Type", "application/json");
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}
				request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
				return true;

			}
			else {
				auditLogs = new TXNAuditLogs();
				ExceptionProcessor auditLogProcessor = new ExceptionProcessor();
				auditLogs.setRequestTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setRequestId(UUID.randomUUID().toString());
				Helper helper = new Helper();
				AuthenticationFilter filter = new AuthenticationFilter();
				Map<String, String> headersMap = helper.getHeadersInfo(request);
				String consumerCode = headersMap.get("consumer-code");
				String reqId = headersMap.get("request-id");
				String authKey = headersMap.get("auth-key");
				String esbCategoryCode = headersMap.get("esb-category-code");
				String esbServiceCode = "";
				String uri = request.getRequestURI();
				logger.info("############Request Processing started for request-id {} and esbURL is {}", reqId, uri);
				String[] code = uri.split("/");
				int i = 0;
				boolean isUrlValid = false;
				String[] esbServiceCodeList = null;
				Iterator<String> url = IConstants.urlListV2.iterator();
				while (url.hasNext()) {
					String urlList = (String) url.next();
					System.out.println(urlList + "  " + urlList.contains(uri));
					if (uri.contains(urlList)) {
						esbServiceCodeList = urlList.split("/");
						isUrlValid = true;
					}
				}
				System.out.println(isUrlValid);
				if (!isUrlValid) {
					logger.info("Invalid URL sent by requestId {}", reqId);
					response.sendError(404, "Not Found");

					return false;
				}
				System.out.println("Code length ::" + code.length);
				esbServiceCode = esbServiceCodeList[esbServiceCodeList.length - 1]; 
				/*while (i < code.length && code.length >= 4 && i < 4) {
					esbServiceCode = code[i];
					i++;
				}*/
				System.out.println("esbServiceCode - xml: "+esbServiceCode);
				GetDetails getDetails = new GetDetails();
				String dataEncryption = getDetails.getEncryptionStatus(esbServiceCode);
				auditLogs.setEsbServiceCode(esbServiceCode);

				auditLogs.setConsumerCode(consumerCode);
				auditLogs.setEsbCategoryCode(esbCategoryCode);
				auditLogs.setEsbReqId(reqId);
				auditLogs.setRequestUrl(uri);

				logger.info("Inside DDOS filtering");
				error = getResponseMsg();

				if (request.getContentLength() > 0 && request.getContentLength() > Integer
						.parseInt(DBUtils.properties.get(IConstants.CONTENT_LENGTH))) {
					logger.error("Invalid content length");
					response.setHeader("Content-Type", "application/json");
					String key = "1014";// content length too large
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_TEMPLATE_JSON, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}

				if (dataEncryption == null) {
					logger.error("Invalid ESB service code sent by request id {} ", reqId);
					response.setHeader("Content-Type", "application/json");
					String key = "1003";// Service Inactive
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_TEMPLATE_JSON, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}

				auditLogs.setDataEncryption(dataEncryption);

				if (headersMap.isEmpty()) {
					auditLogProcessor.process(auditLogs);
					return false;
				}
				String requestParamsResponse = filter.validateRequestParams(headersMap);
				if (!requestParamsResponse.equals("SUCCESS")) {
					logger.error("Invalid or empty request params");
					if (reqId == null || reqId.trim().length() == 0) {
						logger.error("Request id not sent");
						auditLogs.setEsbReqId(esbCategoryCode + "-" + consumerCode + "-" + auditLogs.getRequestId());
					}
					response.setHeader("Content-Type", "application/json");
					String key = requestParamsResponse;
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_TEMPLATE_JSON, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}

				String requestTimeStamp = headersMap.get("request-timestamp");
				String responseTimestamp = filter.timeStampValidation(requestTimeStamp);
				if (!responseTimestamp.equals("SUCCESS")) {

					String key = "1013";
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_TEMPLATE_JSON, key, value);

					System.out.println("failresponse" + failresponse);

					logger.error("Invalid timestamp");
					response.setHeader("Content-Type", "application/json");
					auditLogs.setResponseMsg(failresponse);
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);

					return false;
				}
				String checkAuthKey = filter.validateAuthKey(consumerCode, authKey, esbCategoryCode);
				if (!checkAuthKey.equals("SUCCESS")) {
					logger.error("Invalid auth key or consumer code sent by request id {}", reqId);
					String key = "1001";
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_TEMPLATE_JSON, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);
					response.setHeader("Content-Type", "application/json");
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}
				requestParamsResponse = filter.authenticate(authKey, consumerCode, esbCategoryCode, esbServiceCode);
				if (!requestParamsResponse.equals("SUCCESS")) {
					String key = "1002";
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_TEMPLATE_JSON, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);

					response.setHeader("Content-Type", "application/json");
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}
			}

		}
		
		///For XML requests
		if(request.getHeader("Content-Type")!=null && request.getHeader("Content-Type").contains("application/xml")){
			
			if (s.contains("/api/v1")) {

				ExceptionProcessor auditLogProcessor = new ExceptionProcessor();
				auditLogs = new TXNAuditLogs();
				auditLogs.setRequestTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setRequestId(UUID.randomUUID().toString());
				Helper helper = new Helper();
				AuthenticationFilter filter = new AuthenticationFilter();
				Map<String, String> headersMap = helper.getHeadersInfo(request);
				String consumerCode = headersMap.get("consumer-code");
				String reqId = headersMap.get("request-id");
				String authKey = headersMap.get("auth-key");
				String esbCategoryCode = headersMap.get("esb-category-code");
				String esbServiceCode = "";
				String uri = request.getRequestURI();
				logger.info("############Request Processing started for request-id {} and esbURL is {}", reqId, uri);
				String[] code = uri.split("/");
				int i = 0;
				boolean isUrlValid = false;
				String[] esbServiceCodeList = null;
				Iterator<String> url = IConstants.urlList.iterator();
				while (url.hasNext()) {
					String urlList = (String) url.next();
					System.out.println(urlList + "  " + urlList.contains(uri));
					if (uri.contains(urlList)) {
						esbServiceCodeList = urlList.split("/");
						isUrlValid = true;
					}
				}
				System.out.println(isUrlValid);
				if (!isUrlValid) {
					logger.info("Invalid URL sent by requestId {}", reqId);
					response.sendError(404, "Not Found");

					return false;
				}
				System.out.println("Code length ::" + code.length);
				esbServiceCode = esbServiceCodeList[esbServiceCodeList.length - 1]; 
				/*while (i < code.length && code.length >= 4 && i < 4) {
					esbServiceCode = code[i];
					i++;
				}*/
				System.out.println("esbServiceCode - xml: "+esbServiceCode);
				GetDetails getDetails = new GetDetails();
				String dataEncryption = getDetails.getEncryptionStatus(esbServiceCode);
				auditLogs.setEsbServiceCode(esbServiceCode);
				System.out.println("dataEncryption from DB: "+dataEncryption);
				auditLogs.setConsumerCode(consumerCode);
				auditLogs.setEsbCategoryCode(esbCategoryCode);
				auditLogs.setEsbReqId(reqId);
				auditLogs.setRequestUrl(uri);

				logger.info("Inside DDOS filtering");

				// get errors in map

				error = getResponseMsg();

				if (request.getContentLength() > 0 && request.getContentLength() > Integer
						.parseInt(DBUtils.properties.get(IConstants.CONTENT_LENGTH))) {
					logger.error("Invalid content length");
					response.setHeader("Content-Type", "application/xml");
					String key = "1014";// content length too large
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}

				if (dataEncryption == null) {
					logger.error("Invalid ESB service code sent by request id {} ", reqId);
					response.setHeader("Content-Type", "application/xml");
					String key = "1003";// Service Inactive
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);

					// auditLogs.setResponseMsg("{\"errorCode\":1,\"errorMessage\":\"ESB Service
					// does not exist\"}");
					auditLogProcessor.process(auditLogs);
					// response.getOutputStream().println("{\"errorCode\":1,\"errorMessage\":\"ESB
					// Service Does not exist\"}");

					response.getOutputStream().println(failresponse);
					return false;
				}

				auditLogs.setDataEncryption(dataEncryption);

				if (headersMap.isEmpty()) {
					auditLogProcessor.process(auditLogs);
					return false;
				}
				String requestParamsResponse = filter.validateRequestParams(headersMap);
				if (!requestParamsResponse.equals("SUCCESS")) {
					logger.error("Invalid or empty request params");
					if (reqId == null || reqId.trim().length() == 0) {
						logger.error("Request id not sent");
						auditLogs.setEsbReqId(esbCategoryCode + "-" + consumerCode + "-" + auditLogs.getRequestId());
					}
					response.setHeader("Content-Type", "application/xml");
					String key = requestParamsResponse;
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}

				String requestTimeStamp = headersMap.get("request-timestamp");
				String responseTimestamp = filter.timeStampValidation(requestTimeStamp);
				if (!responseTimestamp.equals("SUCCESS")) {

					String key = "1013";
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML, key, value);

					System.out.println("failresponse" + failresponse);

					logger.error("Invalid timestamp");
					response.setHeader("Content-Type", "application/xml");
					// auditLogs.setResponseMsg("{\"errorCode\":2,\"errorMessage\":\""+responseTimestamp+"\"}");

					// response.getOutputStream().println("{\"errorCode\":2,\"errorMessage\":\""+responseTimestamp+"\"}");
					auditLogs.setResponseMsg(failresponse);
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);

					return false;
				}
				String checkAuthKey = filter.validateAuthKey(consumerCode, authKey, esbCategoryCode);
				if (!checkAuthKey.equals("SUCCESS")) {
					logger.error("Invalid auth key or consumer code sent by request id {}", reqId);
					String key = "1001";
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);

					// auditLogs.setResponseMsg("{\"errorCode\":3,\"errorMessage\":\"Authentication
					// Failed\"}");
					response.setHeader("Content-Type", "application/xml");
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}
				requestParamsResponse = filter.authenticate(authKey, consumerCode, esbCategoryCode, esbServiceCode);
				if (!requestParamsResponse.equals("SUCCESS")) {
					String key = "1002";
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);

					response.setHeader("Content-Type", "application/xml");
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}
				request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
				return true;

			}
			else {
				auditLogs = new TXNAuditLogs();
				ExceptionProcessor auditLogProcessor = new ExceptionProcessor();
				auditLogs.setRequestTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setRequestId(UUID.randomUUID().toString());
				Helper helper = new Helper();
				AuthenticationFilter filter = new AuthenticationFilter();
				Map<String, String> headersMap = helper.getHeadersInfo(request);
				String consumerCode = headersMap.get("consumer-code");
				String reqId = headersMap.get("request-id");
				String authKey = headersMap.get("auth-key");
				String esbCategoryCode = headersMap.get("esb-category-code");
				String esbServiceCode = "";
				String uri = request.getRequestURI();
				logger.info("############Request Processing started for request-id {} and esbURL is {}", reqId, uri);
				String[] code = uri.split("/");
				int i = 0;
				boolean isUrlValid = false;
				String[] esbServiceCodeList = null;
				Iterator<String> url = IConstants.urlListV2.iterator();
				while (url.hasNext()) {
					String urlList = (String) url.next();
					System.out.println(urlList + "  " + urlList.contains(uri));
					if (uri.contains(urlList)) {
						esbServiceCodeList = urlList.split("/");
						isUrlValid = true;
					}
				}
				System.out.println(isUrlValid);
				if (!isUrlValid) {
					logger.info("Invalid URL sent by requestId {}", reqId);
					response.sendError(404, "Not Found");

					return false;
				}
				System.out.println("Code length ::" + code.length);
				esbServiceCode = esbServiceCodeList[esbServiceCodeList.length - 1]; 
				/*while (i < code.length && code.length >= 4 && i < 4) {
					esbServiceCode = code[i];
					i++;
				}*/
				System.out.println("esbServiceCode - xml: "+esbServiceCode);
				GetDetails getDetails = new GetDetails();
				String dataEncryption = getDetails.getEncryptionStatus(esbServiceCode);
				auditLogs.setEsbServiceCode(esbServiceCode);

				auditLogs.setConsumerCode(consumerCode);
				auditLogs.setEsbCategoryCode(esbCategoryCode);
				auditLogs.setEsbReqId(reqId);
				auditLogs.setRequestUrl(uri);

				logger.info("Inside DDOS filtering");

				// get errors in map

				error = getResponseMsg();

				if (request.getContentLength() > 0 && request.getContentLength() > Integer
						.parseInt(DBUtils.properties.get(IConstants.CONTENT_LENGTH))) {
					logger.error("Invalid content length");
					response.setHeader("Content-Type", "application/xml");
					String key = "1014";// content length too large
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML_V2, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}

				if (dataEncryption == null) {
					logger.error("Invalid ESB service code sent by request id {} ", reqId);
					response.setHeader("Content-Type", "application/xml");
					String key = "1003";// Service Inactive
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML_V2, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}

				auditLogs.setDataEncryption(dataEncryption);

				if (headersMap.isEmpty()) {
					auditLogProcessor.process(auditLogs);
					return false;
				}
				String requestParamsResponse = filter.validateRequestParams(headersMap);
				if (!requestParamsResponse.equals("SUCCESS")) {
					logger.error("Invalid or empty request params");
					if (reqId == null || reqId.trim().length() == 0) {
						logger.error("Request id not sent");
						auditLogs.setEsbReqId(esbCategoryCode + "-" + consumerCode + "-" + auditLogs.getRequestId());
					}
					response.setHeader("Content-Type", "application/xml");
					String key = requestParamsResponse;
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML_V2, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}

				String requestTimeStamp = headersMap.get("request-timestamp");
				String responseTimestamp = filter.timeStampValidation(requestTimeStamp);
				if (!responseTimestamp.equals("SUCCESS")) {

					String key = "1013";
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML_V2, key, value);

					System.out.println("failresponse" + failresponse);

					logger.error("Invalid timestamp");
					response.setHeader("Content-Type", "application/xml");
					auditLogs.setResponseMsg(failresponse);
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);

					return false;
				}
				String checkAuthKey = filter.validateAuthKey(consumerCode, authKey, esbCategoryCode);
				if (!checkAuthKey.equals("SUCCESS")) {
					logger.error("Invalid auth key or consumer code sent by request id {}", reqId);
					String key = "1001";
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML_V2, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);

					// auditLogs.setResponseMsg("{\"errorCode\":3,\"errorMessage\":\"Authentication
					// Failed\"}");
					response.setHeader("Content-Type", "application/xml");
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}
				requestParamsResponse = filter.authenticate(authKey, consumerCode, esbCategoryCode, esbServiceCode);
				if (!requestParamsResponse.equals("SUCCESS")) {
					String key = "1002";
					String value = getValue(key);
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML_V2, key, value);
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);

					response.setHeader("Content-Type", "application/xml");
					auditLogProcessor.process(auditLogs);
					response.getOutputStream().println(failresponse);
					return false;
				}

			}
		}
		
		if(auditLogs !=null) {
			request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
			return true;
		}
		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		return false;
	}
	

	
	@Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("4i-Post Handle method is Calling");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		System.out.println("after completion"+response.getStatus());
		
		TXNAuditLogs audit = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		AuditLogProcessor auditLogProcessor = new AuditLogProcessor();
		System.out.println("after completion-----"+audit);
		auditLogProcessor.process(audit);
		
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);

    	System.out.println("4i-Request and Response is completed");
    }


}
