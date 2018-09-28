package br.com.experian.cucumber.integration.cucumber.common.utils.httpclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import br.com.experian.cucumber.integration.cucumber.common.utils.PropertiesUtil;

public class HttpClient {

    private String hostSchema;
    private String hostName;
    private String apiVersion;
    private Integer hostPort;

    private String proxyHost;
    private int proxyPort;
    private String username;
    private String password;

    public HttpClient(String domain, String apiVersion) {    	
    	hostSchema = PropertiesUtil.getProperty("host.schema");
		hostName = PropertiesUtil.getProperty("host.name");
		hostPort = Integer.valueOf(PropertiesUtil.getProperty("host.port"));
        this.apiVersion = apiVersion;
        
        if (apiVersion == null || apiVersion.isEmpty()) {
            this.apiVersion = PropertiesUtil.getProperty("api.version");
        }
    	
    	if (domain != null && !domain.isEmpty()) {
    		
    		if(PropertiesUtil.getProperty(domain+".host.name") != null && !PropertiesUtil.getProperty(domain+".host.name").isEmpty()) {
            	hostSchema = PropertiesUtil.getProperty(domain+".host.schema");
        		hostName = PropertiesUtil.getProperty(domain+".host.name");
        		hostPort = Integer.valueOf(PropertiesUtil.getProperty(domain+".host.port"));
                this.apiVersion = PropertiesUtil.getProperty(domain+".api.version");
            }
    	}

        if (!"localhost".equalsIgnoreCase(hostName)) {
            proxyHost = PropertiesUtil.getProperty("proxy.host");
            proxyPort = Integer.valueOf(PropertiesUtil.getProperty("proxy.port"));
            username = PropertiesUtil.getProperty("proxy.username");
            password = PropertiesUtil.getProperty("proxy.password");
        }
    }

    // HTTP GET request
    public HttpResponse sendGet(String path,
                                List<NameValuePair> parameters, List<Header> headers) throws Exception {

        int port = getHostPort();
        HttpHost host = new HttpHost(hostName, port, hostSchema);

        URI uri = getUri(path, parameters);
        CloseableHttpClient client = getCloseableHttpClient(hostName);
        HttpGet request = new HttpGet(uri);
        setHeaders(headers, request);
        HttpResponse response = client.execute(host, request);

        return response;
    }

    // HTTP GET request - downloadFile
    public File sendGetAndDownload(String path,
                                List<NameValuePair> parameters, List<Header> headers, String filePath) throws Exception {

        int port = getHostPort();
        HttpHost host = new HttpHost(hostName, port, hostSchema);

        URI uri = getUri(path, parameters);
        CloseableHttpClient client = getCloseableHttpClient(hostName);
        HttpGet request = new HttpGet(uri);
        setHeaders(headers, request);
        //HttpResponse response = client.execute(host, request);

        File downloaded = client.execute(host, request, new FileDownloadResponseHandler(new File(filePath)));
        return downloaded;

    }

    // HTTP POST request
    public HttpResponse sendPost(String path, List<NameValuePair> parameters, List<Header> headers, StringEntity entity) throws Exception {

        int port = getHostPort();
        HttpHost host = new HttpHost(hostName, port, hostSchema);

        URI uri = getUri(path, parameters);
        CloseableHttpClient client = getCloseableHttpClient(hostName);
        HttpPost request = new HttpPost(uri);
        request.setEntity(entity);
        setHeaders(headers, request);
        HttpResponse response = client.execute(host, request);

        return response;
    }

    // HTTP PUT request
    public HttpResponse sendPut(String path,
                                List<NameValuePair> parameters, List<Header> headers, StringEntity entity) throws Exception {

        int port = getHostPort();
        HttpHost host = new HttpHost(hostName, port, hostSchema);

        URI uri = getUri(path, parameters);
        CloseableHttpClient client = getCloseableHttpClient(hostName);
        HttpPut request = new HttpPut(uri);
        request.setEntity(entity);
        setHeaders(headers, request);

        HttpResponse response = client.execute(host, request);
        return response;
    }

    // HTTP DELETE request
    public HttpResponse sendDelete(String path,
                                   List<NameValuePair> parameters, List<Header> headers) throws Exception {

        int port = getHostPort();
        HttpHost host = new HttpHost(hostName, port, hostSchema);

        URI uri = getUri(path, parameters);
        CloseableHttpClient client = getCloseableHttpClient(hostName);
        HttpDelete request = new HttpDelete(uri);
        setHeaders(headers, request);

        HttpResponse response = client.execute(host, request);
        return response;
    }

    // HTTP DELETE request with body
    public HttpResponse sendDeleteWithBody(String path,
                                   List<NameValuePair> parameters, List<Header> headers, StringEntity entity) throws Exception {

        int port = getHostPort();
        HttpHost host = new HttpHost(hostName, port, hostSchema);

        URI uri = getUri(path, parameters);
        CloseableHttpClient client = getCloseableHttpClient(hostName);
        HttpDeleteWithBody request = new HttpDeleteWithBody(uri);
        request.setEntity(entity);
        setHeaders(headers, request);

        HttpResponse response = client.execute(host, request);
        return response;
    }


    // HTTP PATCH request
    public HttpResponse sendPatch(String path,
                                  List<NameValuePair> parameters, List<Header> headers, StringEntity entity) throws Exception {
        int port = getHostPort();
        HttpHost host = new HttpHost(hostName, port, hostSchema);

        URI uri = getUri(path, parameters);
        CloseableHttpClient client = getCloseableHttpClient(hostName);
        HttpPatch request = new HttpPatch(uri);
        request.setEntity(entity);
        setHeaders(headers, request);

        HttpResponse response = client.execute(host, request);
        return response;
    }

    public HttpResponse sendOptions(String path,
                                  List<NameValuePair> parameters, List<Header> headers) throws Exception {
        int port = getHostPort();
        HttpHost host = new HttpHost(hostName, port, hostSchema);

        URI uri = getUri(path, parameters);
        CloseableHttpClient client = getCloseableHttpClient(hostName);
        HttpOptions request = new HttpOptions(uri);
        setHeaders(headers, request);

        HttpResponse response = client.execute(host, request);
        return response;
    }


    private SSLConnectionSocketFactory getSslConnectionSocketFactory() throws NoSuchAlgorithmException,
            KeyStoreException, KeyManagementException, CertificateException, IOException {

        SSLConnectionSocketFactory sslConnectionSocketFactory = null;
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();

        TrustStrategy trustStrategy = new TrustStrategy() {

            @Override
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        };
        sslContextBuilder.loadTrustMaterial(trustStrategy);

        HostnameVerifier hostnameVerifierAllowAll = new HostnameVerifier() {

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                // Allow All Hostnames
                return true;
            }
        };

        sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build(), hostnameVerifierAllowAll);
        return sslConnectionSocketFactory;
    }

    private CloseableHttpClient getCloseableHttpClient(String hostName) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException {

        SSLConnectionSocketFactory sslConnectionSocketFactory = getSslConnectionSocketFactory();

        if (System.getProperty("environment.name") != null && System.getProperty("environment.name").equals("LO") && (!"localhost".equalsIgnoreCase(hostName))) {

            Credentials credentials = new UsernamePasswordCredentials(username, password);

            AuthScope authScope = new AuthScope(proxyHost, proxyPort);
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(authScope, credentials);

            HttpHost proxy = new HttpHost(proxyHost, proxyPort);

            return HttpClients.custom()
                    .setProxy(proxy)
                    .setDefaultCredentialsProvider(credsProvider)
                    .setSSLSocketFactory(sslConnectionSocketFactory).build();
        }
        return HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory).build();

    }

    private void setHeaders(List<Header> headers, HttpRequestBase request) {
        if (headers != null && headers.size() > 0) {
            for (Header header : headers) {
                request.addHeader(header);
            }
        }
    }

    private URI getUri(String path, List<NameValuePair> parameters) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(apiVersion + path.replaceAll(" ","+"));
        builder.addParameters(parameters);
        return builder.build();
    }

    private int getHostPort() {
    	if (hostPort == 80 || hostPort <= 0) {
    		return -1;
    	}
    	return hostPort;
    }

    static class FileDownloadResponseHandler implements ResponseHandler<File> {

        private final File target;

        public FileDownloadResponseHandler(File target) {
            this.target = target;
        }

        @Override
        public File handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            InputStream source = response.getEntity().getContent();
            FileUtils.copyInputStreamToFile(source, this.target);
            return this.target;
        }

    }

}