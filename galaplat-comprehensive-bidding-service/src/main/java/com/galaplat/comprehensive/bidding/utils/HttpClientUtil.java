package com.galaplat.comprehensive.reservation.utils;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class HttpClientUtil {

	static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	static RestTemplate client;
	
    static {
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        // 连接池最大连接数
        poolingConnectionManager.setMaxTotal(300);
        // 默认并发数
        poolingConnectionManager.setDefaultMaxPerRoute(150);
        // 使用Apache HttpClient替换默认实现支持GZIP
        HttpClient httpClient = HttpClientBuilder.create()
                // 设置连接管理器
                .setConnectionManager(poolingConnectionManager)
                // 禁用自动重定向功能
                .disableRedirectHandling()
                .build();
        // 设置连接超时时间
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setReadTimeout(1800000);
        requestFactory.setConnectTimeout(900000);
        requestFactory.setHttpClient(httpClient);
        client = new RestTemplate(requestFactory);
        // 解决中文乱码
        client.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }
    

	public static String send(String url, MultiValueMap<String, Object> params, String token, HttpMethod htpMethod) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("token", token);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params,
				headers);
		ResponseEntity<String> response = client.exchange(url, htpMethod, requestEntity, String.class);
		return response.getBody();
	}

	public static String send(String url, String reqJsonStr, String token, HttpMethod htpMethod) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("token", token);
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> entity = new HttpEntity<String>(reqJsonStr, headers);
		ResponseEntity<String> response = client.postForEntity(url, entity, String.class); // client.exchange(url,htpMethod,
																							// entity, String.class);
		return response.getBody();
	}

	public static String get(String url, String token) {
		HttpHeaders headers = new HttpHeaders();
		if (StringUtils.isNotBlank(token)) {
			headers.set("token", token);
		}
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = client.exchange(url, HttpMethod.GET, entity, String.class);
		String body = response.getBody();
		return body;
	}

	public static String postSend(String url, MultiValueMap<String, Object> params, String token) {
		ResponseEntity<String> response;
		try {
			HttpHeaders headers = new HttpHeaders();
			//setHttpHeaders(headers,token);
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
					params, headers);
			response = client.exchange(url, HttpMethod.POST, requestEntity, String.class);
			return response.getBody();
		} catch (RestClientException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	public static void setHttpHeaders(HttpHeaders headers,String token){
        headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.set("Authorization", "Bearer " + token);
    }

	public static String putSend(String url, String reqJsonStr, String token) {
		try {
			HttpHeaders headers = new HttpHeaders();
			setHttpHeaders(headers,token);
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<String> entity = new HttpEntity<String>(reqJsonStr, headers);
			ResponseEntity<String> response = client.exchange(url, HttpMethod.PUT, entity, String.class);
			return response.getBody();
		} catch (RestClientException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	public static String postSend(String url, String reqJsonStr, String token) {
		try {
			HttpHeaders headers = new HttpHeaders();
			setHttpHeaders(headers,token);
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<String> entity = new HttpEntity<String>(reqJsonStr, headers);
			ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, entity, String.class);
			return response.getBody();
		} catch (RestClientException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	public static String postSend(HttpHeaders headers,String url, String reqJsonStr) throws InterruptedException {
		try {
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<String> entity = new HttpEntity<String>(reqJsonStr, headers);
			Thread.sleep(5000);
			ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, entity, String.class);
			return response.getBody();
		} catch (RestClientException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public static String getSend(String url, String token) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("token", token);
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<String> entity = new HttpEntity<String>(null, headers);
			ResponseEntity<String> response = client.exchange(url, HttpMethod.GET, entity, String.class);
			String body = response.getBody();
			return body;
		} catch (RestClientException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}
