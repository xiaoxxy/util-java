package tools.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by Joey on 2017/10/18 0018.
 */
public class HttpClientConnectionManager {

    private static int connectTimeout = 5000;
    private static int requestTimeout = 10000;
    private static int socketTimeout = 10000;

    private CloseableHttpClient httpClient = null;

    private HttpClientConnectionManager() {
        // 长连接保持30秒
        PoolingHttpClientConnectionManager pollingConnectionManager =
                new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
        // 总连接数
        pollingConnectionManager.setMaxTotal(500);
        // max connections per route
        pollingConnectionManager.setDefaultMaxPerRoute(500);

        final RequestConfig requestConfig = RequestConfig.custom()
                // the socket timeout (SO_TIMEOUT) in milliseconds
                .setSocketTimeout(socketTimeout)
                // the timeout in milliseconds until a connection is established.
                .setConnectTimeout(connectTimeout)
                // the timeout in milliseconds used when requesting a connection from the connection pool.
                .setConnectionRequestTimeout(requestTimeout).build();
        httpClient = HttpClients.custom().setConnectionManager(pollingConnectionManager)
                .setDefaultRequestConfig(requestConfig).build();
    }

    /**
     * 获取实例
     * @return
     */
    public static HttpClientConnectionManager getInstance() {
        return HttpClientConnectionManager.HttpClientConnectionManagerHolder.httpConnectionManager;
    }

    private static class HttpClientConnectionManagerHolder{
        private final static HttpClientConnectionManager httpConnectionManager = new HttpClientConnectionManager();
    }

//    public static HttpClient acceptsHttpClient() {
//        // 长连接保持30秒
//        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(30,
//                TimeUnit.SECONDS);
//        // 总连接数
//        pollingConnectionManager.setMaxTotal(500);
//        // 同路由的并发数
//        pollingConnectionManager.setDefaultMaxPerRoute(500);
//
//        HttpClientBuilder httpClientBuilder = HttpClients.custom();
//        httpClientBuilder.setConnectionManager(pollingConnectionManager);
//        // 重试次数，默认是3次，没有开启
//        // httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));
//        // 保持长连接配置，需要在头添加Keep-Alive
//        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
//
//        // RequestConfig.Builder builder = RequestConfig.custom();
//        // builder.setConnectionRequestTimeout(200);
//        // builder.setConnectTimeout(5000);
//        // builder.setSocketTimeout(5000);
//        //
//        // RequestConfig requestConfig = builder.build();
//        // httpClientBuilder.setDefaultRequestConfig(requestConfig);
//
//        List<Header> headers = new ArrayList<>();
//        headers.add(new BasicHeader("User-Agent",
//                "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
//        headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
//        headers.add(new BasicHeader("Accept-Language", "zh-CN"));
//        headers.add(new BasicHeader("Connection", "Keep-Alive"));
//
//        httpClientBuilder.setDefaultHeaders(headers);
//        HttpClient httpClient = httpClientBuilder.build();
//
//        // 添加内容转换器
//        //List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//        //messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
//        //messageConverters.add(new FormHttpMessageConverter());
//        //messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
//        //messageConverters.add(new MappingJackson2HttpMessageConverter());
//
//        return httpClient;
//    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public static int getConnectTimeout() {
        return connectTimeout;
    }

    public static void setConnectTimeout(int connectTimeout) {
        HttpClientConnectionManager.connectTimeout = connectTimeout;
    }

    public static int getRequestTimeout() {
        return requestTimeout;
    }

    public static void setRequestTimeout(int requestTimeout) {
        HttpClientConnectionManager.requestTimeout = requestTimeout;
    }

    public static int getSocketTimeout() {
        return socketTimeout;
    }

    public static void setSocketTimeout(int socketTimeout) {
        HttpClientConnectionManager.socketTimeout = socketTimeout;
    }
}
