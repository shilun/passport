package com.passport.service.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;

@SuppressWarnings("deprecation")
public class HttpClientFactory {
	private int requestTimeout = 30000;

	private int connectTimeout = 30000;

	private int socketTimeout = 30000;

	private int maxTotal = 200;

	private Charset charset = Charsets.UTF_8;

	private Integer retry = 3;

	private Map<String, String> headers = new HashMap<String, String>();

	public int getRequestTimeout() {
		return requestTimeout;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public RequestConfig getRequestConfig() {
		return requestConfig;
	}

	public void setRequestConfig(RequestConfig requestConfig) {
		this.requestConfig = requestConfig;
	}

	public void setRequestTimeout(int requestTimeout) {
		this.requestTimeout = requestTimeout;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	private String accept = "text/html;charset=Utf-8,application/json";
	private String contentType = "application/x-www-form-urlencoded; charset=UTF-8";
	private String userAgent = "Mozilla/5.0";
	private String XRequestWith = "XMLHttpRequest";

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Integer getRetry() {
		return retry;
	}

	public void setRetry(Integer retry) {
		this.retry = retry;
	}

	// public static String cookie;
	private RequestConfig requestConfig = RequestConfig.custom()
			.setConnectionRequestTimeout(this.requestTimeout)
			.setConnectTimeout(this.connectTimeout)
			.setSocketTimeout(this.socketTimeout).build();;
	private ConnectionConfig connectionConfig = ConnectionConfig.custom()
			.setCharset(charset).build();
	private CloseableHttpClient client = createClient();

	private HttpClientFactory() {
	};

	public ConnectionConfig getConnectionConfig() {
		return connectionConfig;
	}

	public void setConnectionConfig(ConnectionConfig connectionConfig) {
		this.connectionConfig = connectionConfig;
	}

	public CloseableHttpClient getClient() {
		return client;
	}

	public void setClient(CloseableHttpClient client) {
		this.client = client;
	}

	public static HttpClientFactory createInstance() {
		HttpClientFactory instance = new HttpClientFactory();
		return instance;
	}

	public static HttpClientFactory createSSLInstance() throws Exception {
		HttpClientFactory instance = new HttpClientFactory();
		instance.setClient(instance.createSSLClientDefault());
		return instance;
	}

	public CloseableHttpClient createClient() {
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(this.maxTotal);
		connManager.setDefaultMaxPerRoute(connManager.getMaxTotal());
		CloseableHttpClient client = HttpClients
				.custom()
				.setRetryHandler(
						new DefaultHttpRequestRetryHandler(retry, true))
				.setConnectionManager(connManager).build();
		return client;
	}

	public CloseableHttpClient createSSLClientDefault()
			throws GeneralSecurityException {
		SSLContext sslContext;
		try {
			sslContext = new SSLContextBuilder().loadTrustMaterial(null,
					new TrustStrategy() {
						@Override
						public boolean isTrusted(
								java.security.cert.X509Certificate[] chain,
								String authType)
								throws java.security.cert.CertificateException {
							return true;
						}
					}).build();
		} catch (KeyManagementException | NoSuchAlgorithmException
				| KeyStoreException e) {
			throw e;
		}
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslContext);
		return HttpClients
				.custom()
				.setSSLSocketFactory(sslsf)
				.setMaxConnTotal(this.maxTotal)
				.setMaxConnPerRoute(this.maxTotal)
				.setRetryHandler(
						new DefaultHttpRequestRetryHandler(retry, true))
				.setDefaultConnectionConfig(connectionConfig).build();
	}

	public String doPost(CloseableHttpClient client, String uri,
                         List<NameValuePair> nameValuePairs) throws IOException {
		HttpPost post = new HttpPost(uri);
		this.initPostHeaders(post);

		if (null != nameValuePairs && nameValuePairs.size() > 0) {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
					nameValuePairs, Consts.UTF_8);
			post.setEntity(entity);
		}

		try {
			CloseableHttpResponse response = client.execute(post);
			String result = returnStringRes(response);
			return result;
		} catch (IOException e) {
			throw e;
		}

	}

	public String doGet(CloseableHttpClient client, String uri,
                        List<NameValuePair> nameValuePairs) throws IOException {
		HttpGet get = new HttpGet(uri);

		if (null != nameValuePairs && nameValuePairs.size() > 0) {

			for (int i = 0; i < nameValuePairs.size(); i++) {
				NameValuePair nameValuePair = nameValuePairs.get(i);
				if (i == 0)
					uri += "?";
				else
					uri += "&";
				uri += nameValuePair.getName() + "=" + nameValuePair.getValue();
			}
			get = new HttpGet(uri);
		}

		this.initGetHeaders(get);

		try {
			CloseableHttpResponse response = client.execute(get);
			return returnStringRes(response);
		} catch (IOException e) {
			throw e;
		}
	}

	public String doPost(String uri, List<NameValuePair> nameValuePairs)
			throws IOException {
		HttpPost post = new HttpPost(uri);
		this.initPostHeaders(post);

		if (null != nameValuePairs && nameValuePairs.size() > 0) {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
					nameValuePairs, Consts.UTF_8);
			post.setEntity(entity);
		}

		try {
			CloseableHttpResponse response = client.execute(post);
			return returnStringRes(response);
		} catch (IOException e) {
			throw e;
		}
	}

	public String doPost(String uri, Map<String, Object> map)
			throws IOException {
		List<NameValuePair> params = map2NameValuePair(map);

		return this.doPost(uri, params);
	}

	public String doGet(String uri, Map<String, Object> map) throws IOException {
		List<NameValuePair> params = map2NameValuePair(map);
		return this.doGet(uri, params);
	}

	public String doPut(String uri, Map<String, Object> map) throws IOException {
		List<NameValuePair> params = map2NameValuePair(map);

		return this.doPut(uri, params);
	}

	private List<NameValuePair> map2NameValuePair(Map<String, Object> map) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (null != map && map.size() > 0)
			for (String key : map.keySet()) {
				Object val = map.get(key);
				String valStr = "";
				if (null != val)
					valStr = String.valueOf(val);
				params.add(new BasicNameValuePair(key, valStr));
			}
		return params;
	}

	public String doPut(String uri, List<NameValuePair> nameValuePairs)
			throws IOException {
		HttpPut httpPut = new HttpPut(uri);
		this.initPostHeaders(httpPut);

		if (null != nameValuePairs && nameValuePairs.size() > 0) {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
					nameValuePairs, Consts.UTF_8);
			httpPut.setEntity(entity);
		}

		try {
			CloseableHttpResponse response = client.execute(httpPut);
			return returnStringRes(response);
		} catch (IOException e) {
			throw e;
		}
	}

	public String doDelete(String uri) throws IOException {
		HttpDelete httpDelete = new HttpDelete(uri);
		this.initPostHeaders(httpDelete);

		try {
			CloseableHttpResponse response = client.execute(httpDelete);
			return returnStringRes(response);
		} catch (IOException e) {
			throw e;
		}
	}

	private String returnStringRes(CloseableHttpResponse response)
			throws IOException, HttpResponseException {
		StatusLine statusLine = response.getStatusLine();
		String result = "";
		if (statusLine.getStatusCode() >= 200
				&& statusLine.getStatusCode() < 300) {
			HttpEntity responseEntity = response.getEntity();
			result = EntityUtils.toString(responseEntity, Consts.UTF_8);
			EntityUtils.consume(responseEntity);
			response.close();
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(),
					statusLine.getReasonPhrase());
		}
		return result;
	}

	public CloseableHttpResponse doPost(CloseableHttpClient client, String uri,
                                        NameValuePair[] nameValuePairs) throws IOException {
		HttpPost post = new HttpPost(uri);
		this.initPostHeaders(post);

		if (null != nameValuePairs && nameValuePairs.length > 0) {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
					Arrays.asList(nameValuePairs), Consts.UTF_8);
			post.setEntity(entity);
		}

		try {
			return client.execute(post);
		} catch (IOException e) {
			throw e;
		}
	}

	public void addPostHeader(String headerName, String headerValue) {
		this.headers.put(headerName, headerValue);
	}

	private void initPostHeaders(HttpEntityEnclosingRequestBase http) {
		http.setConfig(requestConfig);
		if (StringUtils.isNotEmpty(this.accept))
			http.setHeader(HttpHeaders.ACCEPT, this.accept);
		if (StringUtils.isNotEmpty(this.contentType))
			http.setHeader(HttpHeaders.CONTENT_TYPE, this.contentType);
		if (StringUtils.isNotEmpty(this.userAgent))
			http.setHeader(HttpHeaders.USER_AGENT, this.userAgent);
		if (StringUtils.isNotEmpty(this.XRequestWith))
			http.setHeader("X-Requested-With", this.XRequestWith);
		for (String headerName : this.headers.keySet()) {
			http.setHeader(headerName, this.headers.get(headerName));
		}
	}

	private void initPostHeaders(HttpRequestBase http) {
		http.setConfig(requestConfig);
		if (StringUtils.isNotEmpty(this.accept))
			http.setHeader(HttpHeaders.ACCEPT, this.accept);
		if (StringUtils.isNotEmpty(this.contentType))
			http.setHeader(HttpHeaders.CONTENT_TYPE, this.contentType);
		if (StringUtils.isNotEmpty(this.userAgent))
			http.setHeader(HttpHeaders.USER_AGENT, this.userAgent);
		if (StringUtils.isNotEmpty(this.XRequestWith))
			http.setHeader("X-Requested-With", this.XRequestWith);
		for (String headerName : this.headers.keySet()) {
			http.setHeader(headerName, this.headers.get(headerName));
		}
	}

	public String doPost(String uri, NameValuePair[] nameValuePairs)
			throws IOException {
		CloseableHttpResponse response = this.doPost(this.client, uri,
				nameValuePairs);
		try {
			return returnStringRes(response);
		} catch (IOException e) {
			throw e;
		}
	}

	public String doPut(String uri, NameValuePair[] nameValuePairs)
			throws ParseException, IOException {
		CloseableHttpResponse response = this.doPut(this.client, uri,
				nameValuePairs);
		String result = returnStringRes(response);
		return result;
	}

	public CloseableHttpResponse doPut(CloseableHttpClient client2, String uri,
                                       NameValuePair[] nameValuePairs) throws IOException {
		HttpPut put = new HttpPut(uri);
		this.initPostHeaders(put);

		if (null != nameValuePairs && nameValuePairs.length > 0) {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
					Arrays.asList(nameValuePairs), Consts.UTF_8);
			put.setEntity(entity);
		}

		try {
			return this.client.execute(put);
		} catch (IOException e) {
			throw e;
		}
	}

	public String doGet(String uri) throws IOException {
		HttpGet get = new HttpGet(uri);

		this.initGetHeaders(get);

		try {
			CloseableHttpResponse response = this.client.execute(get);
			return returnStringRes(response);
		} catch (IOException e) {
			throw e;
		}
	}

	public String doGet(String uri, List<NameValuePair> nameValuePairs)
			throws IOException {
		HttpGet get = new HttpGet(uri);

		if (null != nameValuePairs && nameValuePairs.size() > 0) {

			for (int i = 0; i < nameValuePairs.size(); i++) {
				NameValuePair nameValuePair = nameValuePairs.get(i);
				if (i == 0)
					uri += "?";
				else
					uri += "&";
				uri += nameValuePair.getName() + "=" + nameValuePair.getValue();
			}
			get = new HttpGet(uri);
		}

		this.initGetHeaders(get);

		try {
			CloseableHttpResponse response = this.client.execute(get);
			return returnStringRes(response);
		} catch (IOException e) {
			throw e;
		}
	}

	private void initGetHeaders(HttpGet get) {
		get.setConfig(requestConfig);
		if (StringUtils.isNotEmpty(this.accept))
			get.setHeader(HttpHeaders.ACCEPT, this.accept);
		if (StringUtils.isNotEmpty(this.contentType))
			get.setHeader(HttpHeaders.CONTENT_TYPE, this.contentType);
		if (StringUtils.isNotEmpty(this.userAgent))
			get.setHeader(HttpHeaders.USER_AGENT, this.userAgent);
		if (StringUtils.isNotEmpty(this.XRequestWith))
			get.setHeader("X-Requested-With", this.XRequestWith);
		for (String headerName : this.headers.keySet()) {
			get.setHeader(headerName, this.headers.get(headerName));
		}
	}

	public String doPost(String uri) throws IOException {
		return doPost(uri, new ArrayList<NameValuePair>());
	}

	public String doPostJson(String url, JSONObject json,
			Map<String, String> headers) throws Exception {
		HttpPost post = new HttpPost(url);
		for (Entry<String, String> entry : headers.entrySet()) {
			post.addHeader(entry.getKey(), entry.getValue());
		}
		StringEntity entity = new StringEntity(json.toJSONString(), "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		post.setEntity(entity);
		CloseableHttpResponse resp = client.execute(post);
		return returnStringRes(resp);
	}

}
