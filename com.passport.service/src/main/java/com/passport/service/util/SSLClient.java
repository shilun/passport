package com.passport.service.util;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

class SSLClient {
    private static Logger logger = LoggerFactory.getLogger(SSLClient.class);

    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
                    null, new TrustStrategy() {
                        // 信任所有
                        public boolean isTrusted(X509Certificate[] chain,
                                                 String authType) throws CertificateException {
                            return true;
                        }
                    }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (Exception e) {
            logger.error("实例化 sslContext eroor", e);
        }
        return HttpClients.createDefault();
    }
}