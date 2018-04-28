package tools.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import tools.common.BusinessHttpException;
import tools.json.JSONUtils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Joey on 2017/10/17 0017.
 */
public class RestHttpClient {

    /**
     * 自定义连接时间
     * @param connectTimeout
     * @param requestTimeout
     * @param socketTimeout
     */
    public static void customerConnectionTimes(int connectTimeout,int requestTimeout,int socketTimeout){
        HttpClientConnectionManager.setConnectTimeout(connectTimeout);
        HttpClientConnectionManager.setRequestTimeout(requestTimeout);
        HttpClientConnectionManager.setSocketTimeout(socketTimeout);
    }

    public static <T> T post(String url,Object body, TypeReference<T> resultType)
            throws IOException {
        String result = postForEntity(url,null,body);
        return MessageConvert.convert(result,resultType,null);
    }

    public static <T> T post(String url, Map<String, String> headers,
                             Object body, TypeReference<T> resultType, PropertyNamingStrategy strategy)
            throws IOException {
        String result = postForEntity(url,headers,body);
        return MessageConvert.convert(result,resultType,strategy);
    }

    /**
     * Json格式的post请求
     * @param url 请求URL
     * @param bodyObj 请求参数,可以是对象或者Map<String, String> parameters
     * @return 返回结果 获取返回值使用 response.body().string()
     * @throws IOException
     */
    public static String postForEntity(String url,Object bodyObj)
            throws IOException {
        return postForEntity(url,null,bodyObj);
    }

    /**
     * Json格式的post请求
     * @param url 请求URL
     * @param headers 请求头
     * @param bodyObj 请求参数,可以是对象或者Map<String, String> parameters
     * @return 返回结果
     * @throws IOException
     */
    public static String postForEntity(String url, Map<String, String> headers,Object bodyObj)
            throws IOException {

        String bodyJsonParams = getJsonStringFromObj(bodyObj);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(bodyJsonParams, ContentType.create("application/json", "UTF-8")));
        CloseableHttpResponse response = null;
        try {
            response = HttpClientConnectionManager.getInstance().getHttpClient().execute(httpPost);
            String result = getBodyFromResponse(response);
            return result;
        }finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * UrlEncodedForm格式的post请求
     * @param url 请求URL
     * @param headers 请求头
     * @param params 请求参数,可以是对象或者Map<String, String> parameters
     * @return 返回结果
     * @throws IOException
     */
    public static String postForEntity(String url, Map<String, String> headers,Map<String,String> params)
            throws IOException {

        List<NameValuePair> nameValuePairs = new ArrayList<>();
        params.forEach((k,v)->{
            nameValuePairs.add(new BasicNameValuePair(k, v));
        });
        HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = HttpClientConnectionManager.getInstance().getHttpClient().execute(httpPost);
            String result = getBodyFromResponse(response);
            return result;
        }finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T get(String url, TypeReference<T> resultType) throws IOException {
        return get(url, null,resultType,null);
    }

    /**
     * Get请求
     *
     * @param url
     * @param headers
     * @param resultType
     * @return
     * @throws IOException
     */
    public static <T> T get(String url, Map<String, String> headers,TypeReference<T> resultType,PropertyNamingStrategy strategy) throws IOException {
        String result = getForEntity(url,headers);
        return MessageConvert.convert(result,resultType,strategy);
    }

    public static String getForEntity(String url) throws IOException {
        return getForEntity(url,null);
    }

    /**
     * Get请求
     * @param url
     * @param headers
     * @return
     * @throws IOException
     */
    public static String getForEntity(String url, Map<String, String> headers) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = HttpClientConnectionManager.getInstance().getHttpClient().execute(httpGet);
            String result = getBodyFromResponse(response);
            return result;
        }finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T put(String url,Object body,TypeReference<T> resultType) throws IOException {
        return put(url, null, body, resultType,  null);
    }

    public static <T> T put(String url, Map<String, String> headers,
                            Object body, TypeReference<T> resultType, PropertyNamingStrategy strategy)
            throws IOException {
        String result = putForEntity(url,headers,body);
        return MessageConvert.convert(result,resultType,strategy);
    }

    /**
     * @param url
     * @param headers
     * @param bodyObj 请求参数,可以是对象或者Map<String, String> parameters
     * @return
     * @throws IOException
     */
    public static String putForEntity(String url, Map<String, String> headers,Object bodyObj)
            throws IOException {
        String bodyJsonParams = getJsonStringFromObj(bodyObj);

        HttpPut httpPut = new HttpPut(url);
        httpPut.setEntity(new StringEntity(bodyJsonParams, ContentType.create("application/json", "UTF-8")));
        CloseableHttpResponse response = null;
        try {
            response = HttpClientConnectionManager.getInstance().getHttpClient().execute(httpPut);
            String result = getBodyFromResponse(response);
            return result;
        }finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T delete(String url,Object body,
                               TypeReference<T> resultType) throws IOException {
        return put(url, null, body, resultType,  null);
    }

    public static <T> T delete(String url, Map<String, String> headers,
                               Object body, TypeReference<T> resultType, PropertyNamingStrategy strategy)
            throws IOException {
        String result = deleteForEntity(url,headers,body);
        return MessageConvert.convert(result,resultType,strategy);
    }

    /**
     * @param url
     * @param headers
     * @param bodyObj 请求参数,可以是对象或者Map<String, String> parameters
     * @return
     * @throws IOException
     */
    public static String deleteForEntity(String url, Map<String, String> headers,Object bodyObj)
            throws IOException {
        String bodyJsonParams = getJsonStringFromObj(bodyObj);

        HttpPut httpPost = new HttpPut(url);
        httpPost.setEntity(new StringEntity(bodyJsonParams, ContentType.create("application/json", "UTF-8")));
        CloseableHttpResponse response = null;
        try {
            response = HttpClientConnectionManager.getInstance().getHttpClient().execute(httpPost);
            String result = getBodyFromResponse(response);
            return result;
        }finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getJsonStringFromObj(Object bodyObj) throws JsonProcessingException {
        if(null == bodyObj){
            return "{}";
        }
        if(bodyObj instanceof String){
            return bodyObj.toString();
        }
        String bodyJson = JSONUtils.toJSON(bodyObj);
        return bodyJson;
    }

    public static String getBodyFromResponse(CloseableHttpResponse response) throws IOException{
        //获取响应状态码
        int status = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        String body = entity != null ? EntityUtils.toString(entity,"utf-8") : null;
        if (status >= 200 && status < 300) {
            return body;
        } else {
            try {
                throw new BusinessHttpException(status,body);
            }catch (Exception e){
                throw new BusinessHttpException(status,"解析response中没有报错body");
            }
        }
    }

}
