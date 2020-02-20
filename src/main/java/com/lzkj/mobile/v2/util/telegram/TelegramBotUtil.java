/****************************************************************** 
 *
 *    Powered By tianxia-online. 
 *
 *    Copyright (c) 2018-2020 Digital Telemedia 天下科技 
 *    http://www.d-telemedia.com/ 
 *
 *    Package:     com.tx.platform.utils 
 *
 *    Filename:    TelegramBotUtil.java 
 *
 *    Description: TODO(用一句话描述该文件做什么) 
 *
 *    Copyright:   Copyright (c) 2018-2020 
 *
 *    Company:     天下科技 
 *
 *    @author:     Horus 
 *
 *    @version:    1.0.0 
 *
 *    Create at:   2019年07月20日 10:43 
 *
 *    Revision: 
 *
 *    2019/7/20 10:43 
 *        - first revision 
 *
 *****************************************************************/
package com.lzkj.mobile.v2.util.telegram;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 *    
 *  *  
 *  * @Project: data-server 
 *  * @Package: com.lzkj.treasure.v2.util.telegram 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/10 17:33  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Slf4j
@Component
public class TelegramBotUtil {

    private final static String url_prefix = "https://api.telegram.org/bot";

    /*public static void main(String[] args) throws Exception {
        //请求查询群组ID https://api.telegram.org/bot1092780048:AAFJws0tQGUttAt6msjdCPPQNBwcra4HW8s/getUpdates
        //给机器人发
        //sendAlertMessage("995317779", "1092780048:AAFJws0tQGUttAt6msjdCPPQNBwcra4HW8s", "你好，telegram");
        //给群组发
        //sendAlertMessage("-364456047", "1092780048:AAFJws0tQGUttAt6msjdCPPQNBwcra4HW8s", "你好，telegram");
        sendAlertMessage("-328730204", "1092780048:AAFJws0tQGUttAt6msjdCPPQNBwcra4HW8s", "你好，telegram");
    }*/

    /**
     * 集成Telegram第三方API,发送告警信息
     *
     * @param chatId
     * @param msg
     */
    static void sendAlertMessage(String chatId, String token, String msg)  {
//        try {
//            // String url = "https://api.telegram.org/bot1092780048:AAFJws0tQGUttAt6msjdCPPQNBwcra4HW8s/sendMessage";
//            String url = url_prefix + token + "/sendMessage";
//            JSONObject obj = new JSONObject();
//            obj.put("chat_id", chatId);
//            obj.put("text", URLEncoder.encode(String.format(LocalDateTime.now() + " " + msg), "UTF-8"));
//            StringBuilder param = new StringBuilder();
//            Set<String> set = obj.keySet();
//            for (String str : set) {
//                param.append(str).append("=");
//                param.append(obj.get(str)).append("&");
//            }
//            doGet(url + "?" + param.substring(0, param.length() - 1), 15000);
//        } catch (Exception e) {
//            log.info("预警-发送失败{}", e);
//        }
    }

    private static void doGet(String url, Integer timeout) throws Exception {
        HttpClient httpclient = getCloseableHttpClient(timeout);
        HttpGet httpGet = new HttpGet(url);
        httpclient.execute(httpGet);
    }

    private static HttpClient getCloseableHttpClient(Integer timeout) throws Exception {
        return HttpClients.custom().setConnectionManager(createConnectionManager())
                .setDefaultRequestConfig(getRequestConfig(timeout))
                .build();
    }

    private static RequestConfig getRequestConfig(Integer timeout) {
        return RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
    }

    private static PoolingHttpClientConnectionManager createConnectionManager() throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManager tm = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }
        };
        context.init(null, new TrustManager[]{tm}, null);

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE);

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", socketFactory).build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);
        // 设置连接池大小
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(connectionManager.getMaxTotal());
        // Validate connections after 1 sec of inactivity
        connectionManager.setValidateAfterInactivity(1000);
        return connectionManager;
    }

}
