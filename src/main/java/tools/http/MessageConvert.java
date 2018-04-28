package tools.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import tools.common.AssertUtils;
import tools.json.JSONUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Joey on 2017/8/2 0002.
 */
public class MessageConvert {

    public static  <T> T convert(String responseBody,Class<T> resultClass) throws IOException {

        AssertUtils.notNull(resultClass, "参数 resultClass 不能为空!");
        return JSONUtils.toObject(responseBody, resultClass);
    }

    public static <T> T convert(String responseBody,Class<T> resultClass, PropertyNamingStrategy pse) throws IOException {
        AssertUtils.notNull(resultClass, "参数 resultClass 不能为空!");
        return JSONUtils.toObject(responseBody, resultClass, pse);
    }

    public static <T> T convert(String responseBody,TypeReference<T> typeReference, PropertyNamingStrategy pse) throws IOException {
        AssertUtils.notNull(typeReference, "参数 typeReference 不能为空!");
        return JSONUtils.toObject(responseBody, typeReference, pse);
    }

    public static <T> T convert(String responseBody,Class<T> resultClass, String key) throws IOException {

        AssertUtils.notNull(resultClass, "参数 resultClass 不能为空!");
        AssertUtils.notNull(resultClass, "参数 key 不能为空!");

        JsonNode jsonNode = JSONUtils.toJSON(responseBody);
        return JSONUtils.toObject(keyNodeJson(jsonNode, key), resultClass);
    }

    public static <T> List<T> convertToList(String responseBody,Class<T> resultClass) throws IOException {

        AssertUtils.notNull(resultClass, "参数 resultClass 不能为空!");
        return JSONUtils.toList(responseBody, resultClass);
    }

    public static <T> List<T> convertToList(String responseBody,Class<T> resultClass, String key) throws IOException {

        AssertUtils.notNull(resultClass, "参数 resultClass 不能为空!");
        AssertUtils.notNull(resultClass, "参数 key 不能为空!");
        JsonNode jsonNode = JSONUtils.toJSON(responseBody);
        return JSONUtils.toList(keyNodeJson(jsonNode, key), resultClass);
    }

    private static JsonNode keyNode(JsonNode jsonNode, String key) {
        JsonNode keyNode = jsonNode.path(key);
        if (null != keyNode) {
            keyNode = jsonNode.get(key);
        }
        return keyNode;
    }

    private static String keyNodeJson(JsonNode jsonNode, String key) {
        JsonNode keyNode = keyNode(jsonNode, key);
        String json = keyNode.textValue();
        if (null == json || json.trim().equals("null"))
            json = keyNode.toString();
        return json;
    }

}
