package com.nws.api.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
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
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

import com.google.gson.Gson;
import com.nws.api.esb.IConstants;
import com.nws.api.esb.MeterReading;
import com.nws.api.model.Envelope;
import com.nws.api.model.TXNAuditLogs;
import com.nws.api.service.DiamServiceVer1;
import com.nws.api.service.GetRecordCSHandlerVer1;
import com.nws.api.service.GetRecordGISCSHandlerVer1;


@RestController
@RequestMapping("api/v1")
public class DiamControllerVer1 {
	
	private static final Logger logger = LoggerFactory.getLogger(DiamControllerVer1.class);

	@Autowired
	private DiamServiceVer1 diamServiceVer1;
	
	@Autowired
	private GetRecordCSHandlerVer1 csHandler;

	@Autowired
	private GetRecordGISCSHandlerVer1 getRecordHandler;
	
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

	@Value("${getsocialbenefitdata-target-request-body}")
	private String getSocialBenefitDataRequestBody;
	
	

	@Value("${getmeterreading-target-url}")
	private String getmeterreadingTargetUurl;
	
	@Value("${auth-username}")
	private String authUsername;

	@Value("${auth-password}")
	private String authPassword;
	
	//External Services
	
	@RequestMapping(value = "/getplotdetailsbycrookie", method = {
			RequestMethod.POST }, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getPlotDetailsByCrookie(@RequestPayload Envelope envelope, HttpServletRequest request,
			HttpServletResponse response) {
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		//TXNAuditLogs auditLogs = new TXNAuditLogs();
		ResponseEntity<String> responseEntity = null;
		try {
			// System.out.println(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
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
			//TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
			//auditLogs = new TXNAuditLogs();
			auditLogs.setRequestMsg(xml);
			auditLogs.setTargetRequesMsg(xml);
			try {
				responseEntity = restTemplate.exchange(getPlotDetailsByCrookieURL, HttpMethod.POST, mrReadingEntity,
						String.class);

				auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setTargetResponseMsg(responseEntity.getBody());
				auditLogs.setResponseMsg(responseEntity.getBody());
				auditLogs.setTargetResponseCode(responseEntity.getStatusCode().toString());
			} catch (HttpStatusCodeException e) {
				e.printStackTrace();
				auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
				System.out.println("In exception  " + e.getResponseBodyAsString());
				auditLogs.setResponseMsg(e.getResponseBodyAsString());
			}

		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/getsocialbenefitdata", method = {
			RequestMethod.POST }, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getSocialBenefitData(@RequestPayload Envelope envelope, HttpServletRequest request,
			HttpServletResponse response) {
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		//TXNAuditLogs auditLogs = new TXNAuditLogs();
		ResponseEntity<String> responseEntity = null;
		try {
			// System.out.println(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
			JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			/*
			 * Envelope env = (Envelope) jaxbUnmarshaller.unmarshal(request.getReader());
			 * env.toString();
			 */
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
				responseEntity = restTemplate.exchange(getSocialBenefitDataURL, HttpMethod.POST, mrReadingEntity,
						String.class);

				auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setTargetResponseMsg(responseEntity.getBody());
				auditLogs.setResponseMsg(responseEntity.getBody());
				auditLogs.setTargetResponseCode(responseEntity.getStatusCode().toString());
			} catch (HttpStatusCodeException e) {
				e.printStackTrace();
				auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
				System.out.println("In exception  " + e.getResponseBodyAsString());
				auditLogs.setResponseMsg(e.getResponseBodyAsString());
			}

		} catch (IOException | JAXBException e) {
			e.printStackTrace();
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
		}

		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);

	}


	
	
	
	
	//Internal Services
	
	@GetMapping(value = "/getBillData", produces = "application/json")
	public ResponseEntity<String> getBillDataService(@RequestParam String BillId, @RequestParam String ccbAccount,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {
		return diamServiceVer1.getBilldataServiceHandler(BillId, ccbAccount, request);
	}
	
	@GetMapping(value = "/GetBillPdf", produces = "application/json")
	public ResponseEntity<String> getBillPdf(@RequestParam String billID, @RequestParam String ccbAccount,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {

		String queryString = request.getQueryString();
		return diamServiceVer1.getBillPDFServiceHandler(billID, ccbAccount, request);
	}
	
	@GetMapping(value = "/GetBill64", produces = "application/json")
	public ResponseEntity<String> getBill64(@RequestParam String BID, @RequestParam String ccbAccount,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {
		return diamServiceVer1.getBill64ServiceHandler(BID, ccbAccount, request);
	}
	
	@GetMapping(value = "/GetBillsDetails", produces = "application/json")
	public ResponseEntity<String> getBillDetails(@RequestParam String ccbAccount, @RequestParam String Months,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {
		return diamServiceVer1.getBillDetailServiceHandler(ccbAccount, Months, request);
	}
	
	@GetMapping(value = "/GetBillsSummary", produces = "application/json")
	public ResponseEntity<String> getBillSummary(@RequestParam String ccbAccount, @RequestParam String Months,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {

		return diamServiceVer1.getBillDetailsSummaryHandler(ccbAccount, Months, request);
	}
	
	@GetMapping(value = "/GetGroupBillDetails", produces = "application/json")
	public ResponseEntity<String> getGroupBillDetails(@RequestParam String year, @RequestParam String month,
			@RequestParam String GroupId, HttpServletRequest request, HttpServletResponse response,
			@RequestHeader Map<String, String> headers) throws Exception {

		String queryString = request.getQueryString();
		return diamServiceVer1.getGroupBillDetailsServiceHandler(year, month, GroupId, request);
	}
	
	@GetMapping(value = "/GetGroupBillsInfo", produces = "application/json")
	public ResponseEntity<String> getGroupBillInfo(@RequestParam String year, @RequestParam String month,
			@RequestParam String GroupId, HttpServletRequest request, HttpServletResponse response,
			@RequestHeader Map<String, String> headers) throws Exception {

		return diamServiceVer1.getGroupBillsInfoServiceHandler(year, month, GroupId, request);
	}
	
	@GetMapping(value = "/InquiryPayment", produces = "application/json")
	public ResponseEntity<String> inquiryPayment(@RequestParam String TrackID, @RequestParam String Gateway,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {

		System.out.println("gateway" + Gateway);
		String queryString = request.getQueryString();
		return diamServiceVer1.inquiryPaymentServiceHandler(TrackID, Gateway, request);
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
		return csHandler.handleGetRecordCS(accountId, latitude, longitude, request, response);
	}

	
	
	//Object creation methods
	
	protected RestTemplate getRestTemplate() {
		if (logger.isInfoEnabled())
			logger.info("Entered::DiamControllerVer1::getRestTemplate()");
		String trustStorePath = System.getProperty("javax.net.ssl.trustStore");
        String trustStorePassword = System.getProperty("javax.net.ssl.trustStorePassword");

        // Print out the values to verify they are set correctly
        System.out.println("Trust Store Path - rest template: " + trustStorePath);
        System.out.println("Trust Store Password: - rest template: " + trustStorePassword);
		RestTemplate restTemplate = new RestTemplate(
				new BufferingClientHttpRequestFactory(getClientHttpRequestFactory()));
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new com.nws.api.core.LoggingRequestInterceptor());
		restTemplate.setInterceptors(interceptors);
		return restTemplate;
	}

	private SimpleClientHttpRequestFactory getClientHttpRequestFactory() {
		SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(5000);
		clientHttpRequestFactory.setReadTimeout(300000);
		return clientHttpRequestFactory;
	}

	public HttpHeaders createHeaders() {
		return new HttpHeaders();

	}
	
	


}
