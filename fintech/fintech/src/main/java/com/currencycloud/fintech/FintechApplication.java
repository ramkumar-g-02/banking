package com.currencycloud.fintech;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class FintechApplication {
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	public static void main(String[] args) throws JsonProcessingException {
		SpringApplication.run(FintechApplication.class, args);
	}

	@GetMapping("/token")
	private ResponseEntity<Object> getAuthToken(@RequestParam String email, @RequestParam String apiKey) throws IOException, InterruptedException {
		Map<String, Object> formData = new HashMap<>();
		formData.put("login_id", email);
		formData.put("api_key", apiKey);

		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(URI.create("https://devapi.currencycloud.com/v2/authenticate/api"))
				.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.POST(HttpRequest.BodyPublishers.ofString(getFormDataAsString(formData)))
				.build();
		HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		return ResponseEntity.status(httpResponse.statusCode()).body(httpResponse.body());
	}

	@GetMapping("/account/create")
	public ResponseEntity<Object> currencyCloudAccount(@RequestParam String token, @RequestBody AccountDTO accountDTO) throws IOException, InterruptedException {
		Map<String, Object> formData = new HashMap<>();
		formData.put("account_name", accountDTO.getAccountName());
		formData.put("legal_entity_type", accountDTO.getLegalEntityType());
		formData.put("street", accountDTO.getStreet());
		formData.put("city", accountDTO.getCity());
		formData.put("country", accountDTO.getCountry());
		HttpClient httpClient = HttpClient.newHttpClient();



		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(URI.create("https://devapi.currencycloud.com/v2/accounts/create"))
				.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("X-Auth-Token", token)
				.POST(HttpRequest.BodyPublishers.ofString(getFormDataAsString(formData)))
				.build();
//		HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
//		return ResponseEntity.ok(httpResponse.body());
		return null;
	}

	@GetMapping("/account/contact")
	public ResponseEntity<Object> currencyCloudContact(@RequestParam String token) throws IOException, InterruptedException {
		Map<String, Object> formData = new HashMap<>();
		formData.put("first_name", "Test");
		formData.put("last_name", "23");
		formData.put("email_address", "test23@gmail.com");
		formData.put("phone_number", "7708896665");
		formData.put("account_id", "7c8e694a-30c6-476d-84d1-291ef50aaa6e");
		LocalDate localDate = LocalDate.of(2000, Month.FEBRUARY, 22);
		formData.put("date_of_birth", localDate);
		HttpClient httpClient = HttpClient.newHttpClient();

		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(URI.create("https://devapi.currencycloud.com/v2/contacts/create"))
				.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("X-Auth-Token", token)
				.POST(HttpRequest.BodyPublishers.ofString(getFormDataAsString(formData)))
				.build();
		HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		return ResponseEntity.ok(httpResponse.body());
	}

	@GetMapping("/account")
	public Object getAccount(@RequestParam String accountId, @RequestParam String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Auth-Token", token);
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		Object object = restTemplateBuilder.build().exchange("https://devapi.currencycloud.com/v2/accounts/"+accountId, HttpMethod.GET, requestEntity, Object.class);
		return object;
	}

	@GetMapping("/balance")
	public Object balance(@RequestParam String currency, @RequestParam String token, @RequestParam(required = false) String onBehalfOf) throws IOException, InterruptedException {
		String url = "https://devapi.currencycloud.com/v2/balances/"+currency;
		url = onBehalfOf != null ? url + "?on_behalf_of="+onBehalfOf : url;

		HttpClient httpClient = HttpClient.newHttpClient();

		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("X-Auth-Token", token)
				.GET()
				.build();
		HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		return ResponseEntity.status(httpResponse.statusCode()).body(httpResponse.body());
	}

	@GetMapping("/find")
	public Object find(@RequestParam String token) {

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Auth-Token", token);
		String url = "https://devapi.currencycloud.com/v2/balances/find";
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		Object object = restTemplateBuilder.build().exchange(url, HttpMethod.GET, requestEntity, Object.class);
		return object;
	}

	private String getFormDataAsString(Map<String, Object> formData) {
		StringBuilder formBodyBuilder = new StringBuilder();
		for (Map.Entry<String, Object> singleEntry : formData.entrySet()) {
			if (formBodyBuilder.length() > 0) {
				formBodyBuilder.append("&");
			}
			formBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
			formBodyBuilder.append("=");
			if (singleEntry.getValue() instanceof String[]) {
				formBodyBuilder.append(singleEntry.getValue());
			} else {
				formBodyBuilder.append(URLEncoder.encode(String.valueOf(singleEntry.getValue()), StandardCharsets.UTF_8));
			}
		}
		return formBodyBuilder.toString();
	}

	@PostMapping("/top_up_margin")
	public Object topUpMargin(@RequestParam String token, @RequestBody Map<String, String> input) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();

		headers.set("X-Auth-Token", token);

		map.set("currency", input.get("currency"));
		map.set("amount", input.get("amount"));
		map.set("on_behalf_of", input.get("on_behalf_of"));

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		Object object = restTemplateBuilder.build().exchange("https://devapi.currencycloud.com/v2/balances/top_up_margin", HttpMethod.POST, request, Object.class);
		return object;
	}

	@PostMapping("/conversion/create")
	public Object createConversion(@RequestParam String token, @RequestBody Map<String, String> input) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();

		headers.set("X-Auth-Token", token);

		map.set("buy_currency", input.get("buy_currency"));
		map.set("sell_currency", input.get("sell_currency"));
		map.set("fixed_side", input.get("fixed_side"));
		map.set("amount", input.get("amount"));
		map.set("term_agreement", input.get("term_agreement"));
		map.set("reason", input.get("reason"));
		map.set("unique_request_id", input.get("unique_request_id"));

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		Object object = restTemplateBuilder.build().exchange("https://devapi.currencycloud.com/v2/conversions/create", HttpMethod.POST, request, Object.class);
		return object;
	}

	@GetMapping("/contacts")
	public Object contacts(@RequestParam String token, @RequestParam(required = false) String accountName) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Auth-Token", token);
		String url = "https://devapi.currencycloud.com/v2/contacts/find";
		url = accountName != null ? url + "?account_name="+accountName : url;
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		Object object = restTemplateBuilder.build().exchange(url, HttpMethod.GET, requestEntity, Object.class);
		return object;
	}

	@GetMapping("/transactions")
	public Object transactions(@RequestParam String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Auth-Token", token);
		String url = "https://devapi.currencycloud.com/v2/transactions/find";
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		Object object = restTemplateBuilder.build().exchange(url, HttpMethod.GET, requestEntity, Object.class);
		return object;
	}

	@GetMapping("/transfers")
	public Object transfers(@RequestParam String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Auth-Token", token);
		String url = "https://devapi.currencycloud.com/v2/transfers/find";
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		Object object = restTemplateBuilder.build().exchange(url, HttpMethod.GET, requestEntity, Object.class);
		return object;
	}

	@PostMapping("/bank_details")
	public Object transfers(@RequestParam String token, @RequestBody Map<String, String> input) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Auth-Token", token);
		String url = "https://devapi.currencycloud.com/v2/reference/bank_details";
		url += "?identifier_type=" + input.get("identifierType") + "&identifier_value=" + input.get("identifierValue");
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		Object object = restTemplateBuilder.build().exchange(url, HttpMethod.GET, requestEntity, Object.class);
		return object;
	}

}
