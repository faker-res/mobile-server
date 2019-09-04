package com.lzkj.mobile.util;

import com.lzkj.mobile.exception.YunpianException;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Deprecated
public class HttpUtil {
    public static final int CONNECTION_TIMEOUT = 5000;
    public static final int SOCKETCOOECTION_TIMEOUT = 5000;

    public static final int NORMAL = 200;
    private static CloseableHttpClient httpClient = createSSLClientDefault();

    public static final Properties properties = new Properties();
    // Encoding UTF-8
    public static final String ENCODING = properties.getProperty("ENCODING");

    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    public static String get(String url, Map<String, String> paramsMap) throws YunpianException {

        if (null == httpClient)
            httpClient = createSSLClientDefault();

        CloseableHttpClient client = httpClient;
        String responseText = null;
        HttpEntity entity = null;

        CloseableHttpResponse response = null;
        try {
            StringBuilder sb = new StringBuilder();
            if (paramsMap != null) {
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    sb.append("&" + param.getKey() + "=" + param.getValue());
                }
                url = url + "?" + sb.toString().substring(1);
            }

            HttpGet method = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECTION_TIMEOUT)
                    .setSocketTimeout(SOCKETCOOECTION_TIMEOUT).build();// 设置请求超时时间
            method.setConfig(requestConfig);
            response = client.execute(method);
            entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
//            if (response.getStatusLine().getStatusCode() != NORMAL)
//                throw new YunpianException(responseText);

            if (response != null) {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw new YunpianException(e);

        } catch (IOException e) {
            e.printStackTrace();
            throw new YunpianException(e);
        }
        return responseText;
    }

    public static String post(String url, Map<String, String> paramsMap) throws YunpianException {

        if (null == httpClient)
            httpClient = createSSLClientDefault();

        CloseableHttpClient client = httpClient;
        String responseText = "";
        HttpPost method = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000)
                .setSocketTimeout(10000).build();// 设置请求超时时间

        method.setConfig(requestConfig);
//        method.setHeader("Accept","application/json;charset=utf-8");
//        method.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        method.addHeader("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        method.setHeader("Accept", "application/json;charset=utf-8");

        //method.setEntity(new StringEntity(paramsMap.toString(), Charset.forName("UTF-8")));

        HttpEntity entity = null;
        CloseableHttpResponse response = null;
        try {
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, "utf-8"));
            }
            response = client.execute(method);
            entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }

            if (response != null) {
                response.close();
            }
//            if (response.getStatusLine().getStatusCode() != NORMAL)
//                throw new YunpianException(responseText);


        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw new YunpianException(e);

        } catch (IOException e) {
            e.printStackTrace();
            throw new YunpianException(e);
        }
        return responseText;
    }


}
