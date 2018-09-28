package com.serasa.common.utils.httpclient;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

public class HttpClient {
	
	//proxy
	private String proxyHost = "spobrproxy.serasa.intranet";
	private int proxyPort = 3128;
	private HttpHost proxy = new HttpHost(proxyHost,proxyPort);

	private Credentials credentials = new UsernamePasswordCredentials("lsh00042","SWX@429H");
	private AuthScope authScope = new AuthScope(proxyHost, proxyPort);
	private CredentialsProvider credsProvider = new BasicCredentialsProvider();
	
	private final String keyStoreFileFullPath = "../Config/authkeystore.jks";

	// HTTP GET request
	public HttpResponse sendGet(String hostschema, String hostname, Integer hostport, String path,
			List<NameValuePair> parameters, List<Header> headers, Boolean allowAll) throws Exception {
		
		
		
		SSLConnectionSocketFactory sslConnectionSocketFactory = getSslConnectionSocketFactory(allowAll);

		int port = hostport != null ? hostport : -1;
		HttpHost host = new HttpHost(hostname, port, hostschema);

		URIBuilder builder = new URIBuilder(path);
		builder.addParameters(parameters);
		URI uri = builder.build();

		CloseableHttpClient client = HttpClients.custom()
								.setProxy(proxy)
								.setDefaultCredentialsProvider(credsProvider)
								.setSSLSocketFactory(sslConnectionSocketFactory).build();
		
		
		HttpGet request = new HttpGet(uri);

		if (headers != null && headers.size() > 0) {
			for (Header header : headers) {
				request.addHeader(header);
			}
		}

		HttpResponse response = client.execute(host, request);
		
		return response;
	}

	// HTTP POST request
	public HttpResponse sendPost(String hostschema, String hostname, Integer hostport, String path,
			List<NameValuePair> parameters, List<Header> headers, StringEntity entity, Boolean allowAll) throws Exception {

		SSLConnectionSocketFactory sslConnectionSocketFactory = getSslConnectionSocketFactory(allowAll);

		int port = hostport != null ? hostport : -1;
		HttpHost host = new HttpHost(hostname, port, hostschema);

		URIBuilder builder = new URIBuilder(path);
		builder.addParameters(parameters);
		URI uri = builder.build();
	
		credsProvider.setCredentials(authScope, credentials);
		CloseableHttpClient client = HttpClients.custom()
				.setProxy(proxy)
				.setDefaultCredentialsProvider(credsProvider)
				.setSSLSocketFactory(sslConnectionSocketFactory).build();
		
		HttpPost request = new HttpPost(uri);

		request.setEntity(entity);

		if (headers != null && headers.size() > 0) {
			for (Header header : headers) {
				request.addHeader(header);
			}
		}

		HttpResponse response = client.execute(host, request);
		
		return response;
	}
	
	// HTTP PUT request
	public HttpResponse sendPut(String hostschema, String hostname, Integer hostport, String path,
			List<NameValuePair> parameters, List<Header> headers, StringEntity entity, Boolean allowAll) throws Exception {

		SSLConnectionSocketFactory sslConnectionSocketFactory = getSslConnectionSocketFactory(allowAll);

		int port = hostport != null ? hostport : -1;
		HttpHost host = new HttpHost(hostname, port, hostschema);

		URIBuilder builder = new URIBuilder(path);
		builder.addParameters(parameters);
		URI uri = builder.build();

		CloseableHttpClient client = HttpClients.custom()
				.setProxy(proxy)
				.setDefaultCredentialsProvider(credsProvider)
				.setSSLSocketFactory(sslConnectionSocketFactory).build();
		
		HttpPut request = new HttpPut(uri);

		request.setEntity(entity);

		if (headers != null && headers.size() > 0) {
			for (Header header : headers) {
				request.addHeader(header);
			}
		}

		HttpResponse response = client.execute(host, request);
		return response;
	}
	
	// HTTP DELETE request
		public HttpResponse sendDelete(String hostschema, String hostname, Integer hostport, String path,
				List<NameValuePair> parameters, List<Header> headers, Boolean allowAll) throws Exception {

			SSLConnectionSocketFactory sslConnectionSocketFactory = getSslConnectionSocketFactory(allowAll);

			int port = hostport != null ? hostport : -1;
			HttpHost host = new HttpHost(hostname, port, hostschema);

			URIBuilder builder = new URIBuilder(path);
			builder.addParameters(parameters);
			URI uri = builder.build();

			CloseableHttpClient client = HttpClients.custom()
					.setProxy(proxy)
					.setDefaultCredentialsProvider(credsProvider)
					.setSSLSocketFactory(sslConnectionSocketFactory).build();
			
			HttpDelete request = new HttpDelete(uri);


			if (headers != null && headers.size() > 0) {
				for (Header header : headers) {
					request.addHeader(header);
				}
			}

			HttpResponse response = client.execute(host, request);
			return response;

		}
	
	// HTTP PATCH request
	public HttpResponse sendPatch(String hostschema, String hostname, Integer hostport, String path,
			List<NameValuePair> parameters, List<Header> headers, StringEntity entity, Boolean allowAll) throws Exception {

		SSLConnectionSocketFactory sslConnectionSocketFactory = getSslConnectionSocketFactory(allowAll);

		int port = hostport != null ? hostport : -1;
		HttpHost host = new HttpHost(hostname, port, hostschema);

		URIBuilder builder = new URIBuilder(path);
		builder.addParameters(parameters);
		URI uri = builder.build();

		CloseableHttpClient client = HttpClients.custom()
				.setProxy(proxy)
				.setDefaultCredentialsProvider(credsProvider)
				.setSSLSocketFactory(sslConnectionSocketFactory).build();
		
		HttpPatch request = new HttpPatch(uri);

		request.setEntity(entity);

		if (headers != null && headers.size() > 0) {
			for (Header header : headers) {
				request.addHeader(header);
			}
		}

		HttpResponse response = client.execute(host, request);
		return response;
	}

	private SSLConnectionSocketFactory getSslConnectionSocketFactory(Boolean allowAll) throws NoSuchAlgorithmException,
			KeyStoreException, KeyManagementException, CertificateException, IOException {
		SSLConnectionSocketFactory sslConnectionSocketFactory = null;
		if (allowAll) {
			SSLContextBuilder sslContextBuilder = new SSLContextBuilder();

			TrustStrategy trustStrategy = new TrustStrategy() {

				public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					return true;
				}
			};
			sslContextBuilder.loadTrustMaterial(trustStrategy);

			HostnameVerifier hostnameVerifierAllowAll = new HostnameVerifier() {

				public boolean verify(String arg0, SSLSession arg1) {
					// Allow All Hostnames
					return true;
				}
			};

			sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build(),
					hostnameVerifierAllowAll);
		} else {
			File keyStoreFile = new File(keyStoreFileFullPath);
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(keyStoreFile).build();
			sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
		}
		return sslConnectionSocketFactory;
	}

}