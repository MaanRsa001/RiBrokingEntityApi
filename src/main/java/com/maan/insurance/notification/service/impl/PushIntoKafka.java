package com.maan.insurance.notification.service.impl;

import java.util.Arrays;
import java.util.concurrent.Callable;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
 

public class PushIntoKafka implements Callable<Object> {
	private Object request;
	private String url;
 

	public PushIntoKafka(Object request, String url) {
		super();
		this.request = request;
		this.url = url;
	}


	@Override
	public Object call() throws Exception {
		try {
			
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Basic dmlzaW9uOnZpc2lvbkAxMjMj");
			HttpEntity<Object> entityReq = new HttpEntity<>(request, headers);
		 
			 ResponseEntity<Object> response = restTemplate.postForEntity(url, entityReq, Object.class);
			System.out.println(response.getBody());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
