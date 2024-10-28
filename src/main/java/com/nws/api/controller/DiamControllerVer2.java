package com.nws.api.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import java.sql.Timestamp;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;
import com.nws.api.esb.IConstants;
import com.nws.api.esb.MeterReading;
import com.nws.api.model.Envelope;
import com.nws.api.model.TXNAuditLogs;
import com.nws.api.service.DiamServiceVer2;
import com.nws.api.service.GetRecordCSHandlerVer2;
import com.nws.api.service.GetRecordGISCSHandlerVer2;
import com.nws.api.utils.DBUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v2")
public class DiamControllerVer2 {
	
private static final Logger logger = LoggerFactory.getLogger(DiamControllerVer2.class);
	
	@Autowired
	private DiamServiceVer2 diamServiceVer2;
	
	@Autowired
	private GetRecordCSHandlerVer2 csHandler;

	@Autowired
	private GetRecordGISCSHandlerVer2 getRecordHandler;
	
	@Value("${getPersonInformationURL-internal-service-url}")
	private String getPersonInformationURL;
	
	@Value("${getTrafficInformationURL-internal-service-url}")
	private String getTrafficInformationURL;
	
	@Value("${getplotdetailsbycrookie-target-url}")
	private String getPlotDetailsByCrookieURL;
	
	@Value("${getplotdetailsbycrookie-soap-action}")
	private String getPlotDetailsByCrookieSoapAction;
	
	@Value("${getplotdetailsbycrookie.username}")
	private String getPlotDetailsByCrookieUsername;

	@Value("${getplotdetailsbycrookie.password}")
	private String getPlotDetailsByCrookiePassword;
	
	@Value("${getsocialbenefitdata.username}")
	private String getSocialBenefitDataUsername;

	@Value("${getsocialbenefitdata.password}")
	private String getSocialBenefitDataPassword;
	
	@Value("${getsocialbenefitdata-target-url}")
	private String getSocialBenefitDataURL;
	
	@Value("${getsocialbenefitdata-soap-action}")
	private String getSocialBenefitDataSoapAction;
	
	@Value("${getmeterreading-target-url}")
	private String getmeterreadingTargetUurl;
	
	@Value("${auth-username}")
	private String authUsername;

	@Value("${auth-password}")
	private String authPassword;
	
	
	//ROP - Services
	
		@RequestMapping(value = "/getpersoninformation", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_XML_VALUE)
		public ResponseEntity<String> getPersonInformation(@RequestBody String reqPayload, HttpServletRequest request,
				HttpServletResponse response) throws IOException {
			TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
			//TXNAuditLogs auditLogs = new TXNAuditLogs();
			ResponseEntity<String> responseEntity = null;
			
			try {
				RestTemplate restTemplate = getRestTemplate();	
				HttpHeaders headers = new HttpHeaders();
		        headers.setContentType(MediaType.APPLICATION_XML); // Or MediaType.APPLICATION_JSON     
				HttpEntity<String> mrReadingEntity = new HttpEntity<>(reqPayload, headers);
				auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setRequestMsg(reqPayload);
				auditLogs.setTargetRequesMsg(reqPayload);
				//String url = "http://10.160.4.111:8081/SoapRopBothPROD/ropservice/personInfo";
		        /*String response = restTemplate.exchange(
		            url, 
		            HttpMethod.POST, 
		            mrReadingEntity, 
		            String.class 
		        ).getBody();*/

				try {
					responseEntity = restTemplate.exchange(getPersonInformationURL, HttpMethod.POST,
							mrReadingEntity, String.class);
			        System.out.println("Result responseEntity: --->" + responseEntity);
					auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
					auditLogs.setTargetResponseMsg(responseEntity.getBody());
					String responseMsg = String.format(IConstants.ESB_XML_SUCCESS_RESPONSE_V2, responseEntity.getBody());
					responseMsg = responseMsg.replaceAll("(<\\?xml.*?\\?>)","");
					responseMsg = responseMsg.replaceAll("(?m)^[ \\t]*\\r?\\n", "");
					auditLogs.setResponseMsg(responseMsg);
					auditLogs.setTargetResponseCode(responseEntity.getStatusCode().toString());
				}
				catch (HttpStatusCodeException e) {
					e.printStackTrace();
					auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
					System.out.println("In exception  "+ e.getResponseBodyAsString());
					auditLogs.setResponseMsg(e.getResponseBodyAsString());
				}catch (Exception ex) {
					System.err.println("--------- Exception"+ex.getMessage());
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML_V2, "FAILURE", ex.getMessage());
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);
					response.getOutputStream().println(failresponse);
					/*auditLogs.setTargetResponseMsg(ex.getMessage());
					System.out.println("In exception  "+ ex.getMessage());
					auditLogs.setResponseMsg(ex.getMessage());*/
				}

			} catch (IOException e) {
				System.err.println("--------- IOException | JAXBException");
				e.printStackTrace();
				String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML_V2, "FAILURE", e.getMessage());
				System.out.println("failresponse" + failresponse);
				auditLogs.setResponseMsg(failresponse);
				response.getOutputStream().println(failresponse);
				
			}
			
			return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
		}
		
		@RequestMapping(value = "/gettrafficinformaton", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_XML_VALUE)
		public ResponseEntity<String> getTrafficInformation(@RequestBody String reqPayload, HttpServletRequest request,
				HttpServletResponse response) throws IOException {
			TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
			//TXNAuditLogs auditLogs = new TXNAuditLogs();
			ResponseEntity<String> responseEntity = null;
			
			try {
				RestTemplate restTemplate = getRestTemplate();	
				HttpHeaders headers = new HttpHeaders();
		        headers.setContentType(MediaType.APPLICATION_XML); // Or MediaType.APPLICATION_JSON     
				HttpEntity<String> mrReadingEntity = new HttpEntity<>(reqPayload, headers);
				auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setRequestMsg(reqPayload);
				auditLogs.setTargetRequesMsg(reqPayload);
				//String url = "http://10.160.4.111:8081/SoapRopBothPROD/ropservice/trafficInfo";
		        /*String response = restTemplate.exchange(
		            url, 
		            HttpMethod.POST, 
		            mrReadingEntity, 
		            String.class 
		        ).getBody();*/

				try {
					responseEntity = restTemplate.exchange(getTrafficInformationURL, HttpMethod.POST,
							mrReadingEntity, String.class);
			        System.out.println("Result responseEntity: --->" + responseEntity);
					auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
					auditLogs.setTargetResponseMsg(responseEntity.getBody());
					String responseMsg = String.format(IConstants.ESB_XML_SUCCESS_RESPONSE_V2, responseEntity.getBody());
					responseMsg = responseMsg.replaceAll("(<\\?xml.*?\\?>)","");
					responseMsg = responseMsg.replaceAll("(?m)^[ \\t]*\\r?\\n", "");
					auditLogs.setResponseMsg(responseMsg);
					auditLogs.setTargetResponseCode(responseEntity.getStatusCode().toString());
				}
				catch (HttpStatusCodeException e) {
					e.printStackTrace();
					auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
					System.out.println("In exception  "+ e.getResponseBodyAsString());
					auditLogs.setResponseMsg(e.getResponseBodyAsString());
				}catch (Exception ex) {
					System.err.println("--------- Exception"+ex.getMessage());
					String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML_V2, "FAILURE", ex.getMessage());
					System.out.println("failresponse" + failresponse);
					auditLogs.setResponseMsg(failresponse);
					response.getOutputStream().println(failresponse);
					/*auditLogs.setTargetResponseMsg(ex.getMessage());
					System.out.println("In exception  "+ ex.getMessage());
					auditLogs.setResponseMsg(ex.getMessage());*/
				}

			} catch (IOException e) {
				System.err.println("--------- IOException");
				e.printStackTrace();
				String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML_V2, "FAILURE", e.getMessage());
				System.out.println("failresponse" + failresponse);
				auditLogs.setResponseMsg(failresponse);
				response.getOutputStream().println(failresponse);
				
			}
			
			return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
		}


	//External Services
	
	@RequestMapping(value = "/getplotdetailsbycrookie", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getPlotDetailsByCrookie(@RequestPayload Envelope envelope, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		//TXNAuditLogs auditLogs = new TXNAuditLogs();
		ResponseEntity<String> responseEntity = null;
		try {
			//System.out.println(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
			JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);  
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); 
			/*
			 * Envelope env = (Envelope) jaxbUnmarshaller.unmarshal(request.getReader());
			 * env.toString();
			 */
			String xml = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

			HttpHeaders header = createHeaders();
			header.set("SOAPAction", getPlotDetailsByCrookieSoapAction);
			header.set("Content-Type", "text/xml;charset=UTF-8");

			String auth = getPlotDetailsByCrookieUsername + ":" + getPlotDetailsByCrookiePassword;
			String basicAuthValue = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
			header.set("Authorization", basicAuthValue);

			RestTemplate restTemplate = getRestTemplate();
			HttpEntity mrReadingEntity = new HttpEntity(xml, header);
			auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setRequestMsg(xml);
			auditLogs.setTargetRequesMsg(xml);
			try {
				responseEntity = restTemplate.exchange(getPlotDetailsByCrookieURL, HttpMethod.POST,
						mrReadingEntity, String.class);

				auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setTargetResponseMsg(responseEntity.getBody());
				String responseMsg = String.format(IConstants.ESB_XML_SUCCESS_RESPONSE_V2, responseEntity.getBody());
				responseMsg = responseMsg.replaceAll("(<\\?xml.*?\\?>)","");
				responseMsg = responseMsg.replaceAll("(?m)^[ \\t]*\\r?\\n", "");
				auditLogs.setResponseMsg(responseMsg);
				auditLogs.setTargetResponseCode(responseEntity.getStatusCode().toString());
			}
			catch (HttpStatusCodeException e) {
				e.printStackTrace();
				auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
				System.out.println("In exception  "+ e.getResponseBodyAsString());
				auditLogs.setResponseMsg(e.getResponseBodyAsString());
			}catch (Exception ex) {
				System.err.println("--------- Exception"+ex.getMessage());
				String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML_V2, "FAILURE", ex.getMessage());
				System.out.println("failresponse" + failresponse);
				auditLogs.setResponseMsg(failresponse);
				response.getOutputStream().println(failresponse);
				/*auditLogs.setTargetResponseMsg(ex.getMessage());
				System.out.println("In exception  "+ ex.getMessage());
				auditLogs.setResponseMsg(ex.getMessage());*/
			}

		} catch (IOException | JAXBException e) {
			System.err.println("--------- IOException | JAXBException");
			e.printStackTrace();
			String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML_V2, "FAILURE", e.getMessage());
			System.out.println("failresponse" + failresponse);
			auditLogs.setResponseMsg(failresponse);
			response.getOutputStream().println(failresponse);
			
		}
		
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getsocialbenefitdata", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getSocialBenefitData(@RequestPayload Envelope envelope, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		ResponseEntity<String> responseEntity = null;
		try {
			//System.out.println(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
			JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);  
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); 
		
			String xml = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

			HttpHeaders header = createHeaders();
			header.set("SOAPAction", getSocialBenefitDataSoapAction);
			header.set("Content-Type", "text/xml;charset=UTF-8");

			String auth = getSocialBenefitDataUsername + ":" + getSocialBenefitDataPassword;
			String basicAuthValue = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
			header.set("Authorization", basicAuthValue);

			RestTemplate restTemplate = getRestTemplate();
			HttpEntity mrReadingEntity = new HttpEntity(xml, header);
			auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setRequestMsg(xml);
			auditLogs.setTargetRequesMsg(xml);
			try {
				responseEntity = restTemplate.exchange(getSocialBenefitDataURL, HttpMethod.POST,
						mrReadingEntity, String.class);

				auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setTargetResponseMsg(responseEntity.getBody());
				String responseMsg = String.format(IConstants.ESB_XML_SUCCESS_RESPONSE_V2, responseEntity.getBody());
				responseMsg = responseMsg.replaceAll("(<\\?xml.*?\\?>)","");
				responseMsg = responseMsg.replaceAll("(?m)^[ \\t]*\\r?\\n", "");
				auditLogs.setResponseMsg(responseMsg);
				auditLogs.setTargetResponseCode(responseEntity.getStatusCode().toString());
			}
			catch (HttpStatusCodeException e) {
				System.err.println("--------- HttpStatusCodeException");
				//e.printStackTrace();
				auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
				System.out.println("In exception  "+ e.getResponseBodyAsString());
				auditLogs.setResponseMsg(e.getResponseBodyAsString());
			}catch (Exception ex) {
				System.err.println("--------- Exception"+ex.getMessage());
				String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML_V2, "FAILURE", ex.getMessage());
				System.out.println("failresponse" + failresponse);
				auditLogs.setResponseMsg(failresponse);
				response.getOutputStream().println(failresponse);
				/*auditLogs.setTargetResponseMsg(ex.getMessage());
				System.out.println("In exception  "+ ex.getMessage());
				auditLogs.setResponseMsg(ex.getMessage());*/
			}

		} catch (IOException | JAXBException e) {
			System.err.println("--------- IOException | JAXBException");
			e.printStackTrace();
			String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML_V2, "FAILURE", e.getMessage());
			System.out.println("failresponse" + failresponse);
			auditLogs.setResponseMsg(failresponse);
			response.getOutputStream().println(failresponse);
			
		}
		
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}

	
	
	@PostMapping(value = "/getmeterreading", produces = "application/json")
	public ResponseEntity<String> getMeterReading(HttpServletRequest request, HttpServletResponse response,
			@RequestBody(required = false) MeterReading mrPayload, @RequestHeader Map<String, String> headers)
			throws Exception {
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		//TXNAuditLogs auditLogs = new TXNAuditLogs();
		String auth = authUsername + ":" + authPassword;
		String basicAuthValue = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
		header.add("Authorization", basicAuthValue);
		header.add("Content-Type", "application/json");

		auditLogs.setRequestMsg(new Gson().toJson(mrPayload));
		auditLogs.setTargetRequesMsg(new Gson().toJson(mrPayload));
		HttpEntity<MeterReading> mrReadingEntity = new HttpEntity<MeterReading>(mrPayload, header);
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(getmeterreadingTargetUurl, HttpMethod.POST,
					mrReadingEntity, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(responseEntity.getBody());
			auditLogs.setResponseMsg(responseEntity.getBody());
			auditLogs.setTargetResponseCode(responseEntity.getStatusCode().toString());
		} catch (HttpStatusCodeException e) {

			e.printStackTrace();
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			System.out.println("In exception  " + e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
		}catch (Exception ex) {
			System.err.println("--------- Exception"+ex.getMessage());
			String failresponse = String.format(IConstants.ESB_FAILURE_RESPONSE_XML_V2, "FAILURE", ex.getMessage());
			System.out.println("failresponse" + failresponse);
			auditLogs.setResponseMsg(failresponse);
		}

		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		String body = auditLogs.getResponseMsg();
		String esbRespMessage = String.format(IConstants.ESB_SUCCESS_RESPONSE_TEMPLATE_JSON,
				(body != null && !body.trim().isEmpty()) ? body : "{}");
		return new ResponseEntity<String>(esbRespMessage, HttpStatus.OK);

	}

	
	
	//Internal services 
	@GetMapping(value = "/getBillData", produces = "application/json")
	public ResponseEntity<String> getBillDataService(@RequestParam String BillId, @RequestParam String ccbAccount,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {
			return diamServiceVer2.getBilldataServiceHandler(BillId, ccbAccount, request);

	}
	
	@GetMapping(value = "/GetBillPdf", produces = "application/json")
	public ResponseEntity<String> getBillPdf(@RequestParam String billID, @RequestParam String ccbAccount,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {

		String queryString = request.getQueryString();
		return diamServiceVer2.getBillPDFServiceHandler(billID, ccbAccount, request);
	}
	
	@GetMapping(value = "/GetBill64", produces = "application/json")
	public ResponseEntity<String> getBill64(@RequestParam String BID, @RequestParam String ccbAccount,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {
		return diamServiceVer2.getBill64ServiceHandler(BID, ccbAccount, request);
	}
	
	@GetMapping(value = "/GetBillsDetails", produces = "application/json")
	public ResponseEntity<String> getBillDetails(@RequestParam String ccbAccount, @RequestParam String Months,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {
		return diamServiceVer2.getBillDetailServiceHandler(ccbAccount, Months, request);
	}

	@GetMapping(value = "/GetBillsSummary", produces = "application/json")
	public ResponseEntity<String> getBillSummary(@RequestParam String ccbAccount, @RequestParam String Months,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {

		return diamServiceVer2.getBillDetailsSummaryHandler(ccbAccount, Months, request);
	}
	@GetMapping(value = "/GetGroupBillsInfo", produces = "application/json")
	public ResponseEntity<String> getGroupBillInfo(@RequestParam String year, @RequestParam String month,
			@RequestParam String GroupId, HttpServletRequest request, HttpServletResponse response,
			@RequestHeader Map<String, String> headers) throws Exception {

		return diamServiceVer2.getGroupBillsInfoServiceHandler(year, month, GroupId, request);
	}


	@GetMapping(value = "/GetGroupBillDetails", produces = "application/json")
	public ResponseEntity<String> getGroupBillDetails(@RequestParam String year, @RequestParam String month,
			@RequestParam String GroupId, HttpServletRequest request, HttpServletResponse response,
			@RequestHeader Map<String, String> headers) throws Exception {

		String queryString = request.getQueryString();
		return diamServiceVer2.getGroupBillDetailsServiceHandler(year, month, GroupId, request);
	}
	
	@GetMapping(value = "/InquiryPayment", produces = "application/json")
	public ResponseEntity<String> inquiryPayment(@RequestParam String TrackID, @RequestParam String Gateway,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {
		String queryString = request.getQueryString();
		return diamServiceVer2.inquiryPaymentServiceHandler(TrackID, Gateway, request);
	}

	@GetMapping(value = "/GetGISDetails/{accountId}/{lat}/{long}", produces = "application/json")
	public ResponseEntity<String> getRecordsGISService(
			@PathVariable(name = "accountId", required = false) Optional<String> accountId,
			@PathVariable(name = "lat", required = false) Optional<String> latitude,
			@PathVariable(name = "long", required = false) Optional<String> longitude, HttpServletRequest request,
			HttpServletResponse response, @RequestHeader Map<String, String> headers) throws Exception {

		return getRecordHandler.getRecordGISHandler(accountId, latitude, longitude, request);
	}

	@GetMapping(value = "/GetGISDetails/{accountId}/{lat}", produces = "application/json")
	public ResponseEntity<String> getRecordsGISServiceWithAccontIdAndLat(
			@PathVariable(name = "accountId", required = false) Optional<String> accountId,
			@PathVariable(name = "lat", required = false) Optional<String> latitude,
			@PathVariable(name = "long", required = false) Optional<String> longitude, HttpServletRequest request,
			HttpServletResponse response, @RequestHeader Map<String, String> headers) throws Exception {

		return getRecordHandler.getRecordGISHandler(accountId, latitude, longitude, request);
	}

	@GetMapping(value = "/GetGISDetails/{accountId}", produces = "application/json")
	public ResponseEntity<String> getRecordsGISServiceWithAccontId(
			@PathVariable(name = "accountId", required = false) Optional<String> accountId,
			@PathVariable(name = "lat", required = false) Optional<String> latitude,
			@PathVariable(name = "long", required = false) Optional<String> longitude, HttpServletRequest request,
			HttpServletResponse response, @RequestHeader Map<String, String> headers) throws Exception {

		return getRecordHandler.getRecordGISHandler(accountId, latitude, longitude, request);
	}

	
	@GetMapping(value = "/GetGISMap/{accountId}/{lat}/{long}", produces = "application/json")
	public ResponseEntity<String> getRecordsCS(
			@PathVariable(name = "accountId", required = false, value = "") Optional<String> accountId,
			@PathVariable(name = "lat", required = false, value = "") Optional<String> latitude,
			@PathVariable(name = "long", required = false, value = "") Optional<String> longitude,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println(accountId);
		System.out.println(latitude);
		System.out.println(longitude);
		System.out.println("PAWESBServicesV2.getRecordsCS()");
		return csHandler.handleGetRecordCS(accountId, latitude, longitude, request, response);
	}

	@GetMapping(value = "/GetGISMap/{accountId}", produces = "application/json")
	public ResponseEntity<String> getRecordsCSOnlyAccountID(
			@PathVariable(name = "accountId", required = false, value = "") Optional<String> accountId,
			@PathVariable(name = "lat", required = false, value = "") Optional<String> latitude,
			@PathVariable(name = "long", required = false, value = "") Optional<String> longitude,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println(accountId);
		System.out.println(latitude);
		System.out.println(longitude);
		System.out.println("PAWESBServicesV2.getRecordsCSOnlyAccountID()");
		return csHandler.handleGetRecordCS(accountId, latitude, longitude, request, response);
	}

	@GetMapping(value = "/GetGISMap/{accountId}/{lat}", produces = "application/json")
	public ResponseEntity<String> getRecordsCSOnlyAccountIDAndLat(
			@PathVariable(name = "accountId", required = false, value = "") Optional<String> accountId,
			@PathVariable(name = "lat", required = false, value = "") Optional<String> latitude,
			@PathVariable(name = "long", required = false, value = "") Optional<String> longitude,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println(accountId);
		System.out.println(latitude);
		System.out.println(longitude);
		System.out.println("PAWESBServicesV2.getRecordsCSOnlyAccountIDAndLat()");
		return csHandler.handleGetRecordCS(accountId, latitude, longitude, request, response);
	}

	
	
	
	//Test services
	
	@GetMapping("/test")
	public String testApi(){
		return "Welcome to DIAM services";	
	}
	
	//getBillData internal service
	@GetMapping(value = "/getBillDataTest", produces = "application/json")
	public ResponseEntity<String> getBillDataService1(@RequestParam String BillID, @RequestParam String ccbAccount) throws Exception {
		return diamServiceVer2.getBilldataServiceHandler(BillID, ccbAccount);
	}
	
	//getPlotDetailsByCrookie MICIT External service
	@RequestMapping(value = "/getplotdetailsbycrookieTest", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getPlotDetailsByCrookieTest(@RequestPayload Envelope envelope, HttpServletRequest request,
			HttpServletResponse response) {
		//TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		TXNAuditLogs auditLogs = new TXNAuditLogs();
		ResponseEntity<String> responseEntity = null;
		try {
			//System.out.println(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
			JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);  
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); 
			
			String xml = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

			HttpHeaders header = createHeaders();
			header.set("SOAPAction", getPlotDetailsByCrookieSoapAction);
			header.set("Content-Type", "text/xml;charset=UTF-8");

			String auth = getPlotDetailsByCrookieUsername + ":" + getPlotDetailsByCrookiePassword;
			String basicAuthValue = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
			header.set("Authorization", basicAuthValue);

			RestTemplate restTemplate = getRestTemplate();
			HttpEntity mrReadingEntity = new HttpEntity(xml, header);
			//auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setRequestMsg(xml);
			auditLogs.setTargetRequesMsg(xml);
			try {
				responseEntity = restTemplate.exchange(getPlotDetailsByCrookieURL, HttpMethod.POST,
						mrReadingEntity, String.class);

				auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setTargetResponseMsg(responseEntity.getBody());
				String responseMsg = String.format(IConstants.ESB_XML_SUCCESS_RESPONSE_V2, responseEntity.getBody());
				responseMsg = responseMsg.replaceAll("(<\\?xml.*?\\?>)","");
				responseMsg = responseMsg.replaceAll("(?m)^[ \\t]*\\r?\\n", "");
				auditLogs.setResponseMsg(responseMsg);
				auditLogs.setTargetResponseCode(responseEntity.getStatusCode().toString());
			}
			catch (HttpStatusCodeException e) {
				e.printStackTrace();
				auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
				System.out.println("In exception  "+ e.getResponseBodyAsString());
				auditLogs.setResponseMsg(e.getResponseBodyAsString());
			}

		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}

	
	@GetMapping("/getDataFromDb")
	public String getEncryptionStatus(@RequestParam String esbServiceCode) throws Exception {
		logger.info("Inside getEncryption status.. for service code {}" , esbServiceCode);
		String encryption = null;
		//ConnectionHelper conHelper=new ConnectionHelper();
		Connection con = null;
		
		try {
			String query = "select auth_key from TBL_ESB_MST_CONSUMER where is_active='Y' and consumer_code='ESP'";
			//con = conHelper.getConnection();
			try {
				Class.forName(DBUtils.properties.get(IConstants.ORACLE_CLASSNAME));
				con = DriverManager.getConnection(DBUtils.properties.get(IConstants.ORACLE_DBURL),
						DBUtils.properties.get(IConstants.ORACLE_DBUSERNAME),
						DBUtils.properties.get(IConstants.ORACLE_DBPASSWORD));
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			PreparedStatement stmt = con.prepareStatement(query);
			//stmt.setString(1, esbServiceCode);
			ResultSet rs = stmt.executeQuery();
			//System.out.println("rs.getString(0);"+rs.getString(0));
			//System.out.println("rs.getString(1);"+rs.getString(1));
			
			while (rs.next()) {
				logger.debug("Encryption status is {}", rs.getString(1));
				encryption = rs.getString(1);
				System.out.println("encryt result: "+encryption);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			logger.error("Error while fetching encryption status {}", e.getLocalizedMessage());
		} finally {
			//conHelper.releaseConnection(con);
			try {
				if (con != null) {
					con.close();
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		return encryption;
	}
	
	//Object creation methods
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
