package com.ttu.mapoverlaytest.service.external;

import java.util.Collections;

import com.ttu.mapoverlaytest.model.rest.Constant;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestService {
	private RestTemplate getRestTemplate() {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setReadTimeout(60000);

		RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

		return restTemplate;
	}

	public Object processRequest(String uri, Class<?> responseClass) {
		String url = Constant.REST_URL + uri;

		HttpHeaders headers = new HttpHeaders();

		headers.setAccept(Collections.singletonList(new MediaType("application", "json")));
		headers.setContentType(MediaType.APPLICATION_JSON);

		if (responseClass == null) {
			responseClass = Object.class;
		}

		HttpEntity requestEntity = new HttpEntity<Object>(headers);

		ResponseEntity<?> responseEntity = getRestTemplate()
				.exchange(url, HttpMethod.GET, requestEntity, responseClass);

		return responseEntity.getBody();
	}
}
