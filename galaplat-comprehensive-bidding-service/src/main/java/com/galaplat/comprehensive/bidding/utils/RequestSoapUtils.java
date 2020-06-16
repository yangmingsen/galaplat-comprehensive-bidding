package com.galaplat.comprehensive.reservation.utils;

import java.nio.charset.Charset;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public abstract class RequestSoapUtils {
	
	static int socketTimeout = 300000;// 请求超时时间
    static int connectTimeout = 300000;// 传输超时时间
    
    
    /**
     * 使用SOAP1.1发送消息
     *
     * @param postUrl
     * @param mapXml
     * @param soapAction
     * @return
     * @throws Exception 
     */
    public static String doPostSoap(String postUrl, String soapAction,String xml) throws Exception {
        String retStr = "";
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(postUrl);
        //  设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout( socketTimeout )
                .setConnectTimeout( connectTimeout ).build();
        httpPost.setConfig(requestConfig);
        try {
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8" );
            httpPost.setHeader("SOAPAction", soapAction );
            StringEntity data = new StringEntity( xml, Charset.forName("UTF-8") );
            httpPost.setEntity(data);
            CloseableHttpResponse response = closeableHttpClient
                    .execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                // 打印响应内容
                retStr = EntityUtils.toString(httpEntity, "UTF-8");
            }
            // 释放资源
            closeableHttpClient.close();
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new Exception("SOAP请求异常");
        }
        return StringEscapeUtils.unescapeHtml( retStr);
    }
}
