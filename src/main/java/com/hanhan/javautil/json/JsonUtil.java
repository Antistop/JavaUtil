package com.hanhan.javautil.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Slf4j
public class JsonUtil {
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        MAPPER.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,true);
        MAPPER.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
    }

    public static String toJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("json序列化错误", e);
            return "";
        }
    }

    /**
     * try住异常，返回null，让调用者自己去判断是否抛异常
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.error("json反序列化错误", e);
            return null;
        }
    }

    public static <T> T toObject(JsonNode jsonNode, Class<T> clazz) {
        try {
            return MAPPER.treeToValue(jsonNode, clazz);
        } catch (JsonProcessingException e) {
            log.error("json反序列化错误", e);
            return null;
        }
    }

    public static JsonNode toObject(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (IOException e) {
            log.error("json反序列化错误", e);
            return null;
        }
    }

    public static <T> T toObject(String json, TypeReference<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            log.error("json反序列化错误", e);
            return null;
        }
    }

    public static JavaType createJavaType(Class<?> mc, Class<?>... ac) {
        return MAPPER.getTypeFactory().constructParametricType(mc, ac);
    }

    public static <T> T fromJson(String json, JavaType type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            log.error("json反序列化错误", e);
            return null;
        }
    }

    public static ObjectNode createObjectNode() {
        return MAPPER.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return MAPPER.createArrayNode();
    }

    public static String getField(JsonNode jsonObject, String field, String defaultValue) {
        String text = getField(jsonObject, field);
        if (text.trim().length() == 0) {
            return defaultValue;
        } else {
            return text;
        }
    }

    public static ArrayNode getArray(JsonNode jsonObject, String field) {
        return null;
    }

    public static String getField(JsonNode jsonObject, String field) {
        if (field == null || field.length() == 0) {
            return "";
        }
        String[] split = field.split("\\.");
        if (jsonObject == null) {
            return "";
        }
        for (int i = 0; i < split.length - 1; i++) {
            String fieldName = split[i];
            jsonObject = jsonObject.get(fieldName);
            if (jsonObject == null) {
                return "";
            }
        }
        JsonNode jsonNode = jsonObject.get(split[split.length - 1]);
        if (jsonNode == null) {
            return "";
        } else {
            return jsonNode.asText();
        }
    }

    public static String getField(JsonNode jsonObject, String field, int... index) {
        if (field == null || field.length() == 0) {
            return "";
        }
        String[] split = field.split("\\.");
        int arrayPoint = 0;
        if (jsonObject == null) {
            return "";
        }
        for (int i = 0; i < split.length - 1; i++) {
            String fieldName = split[i];
            if (fieldName.endsWith("[]")) {
                fieldName = fieldName.substring(0, fieldName.length() - 2);
                JsonNode a = jsonObject.get(fieldName);
                if (a == null || !a.isArray()) {
                    return "";
                }
                jsonObject = a.get(index[arrayPoint++]);
            } else {
                jsonObject = jsonObject.get(fieldName);
            }
            if (jsonObject == null) {
                return "";
            }
        }
        JsonNode jsonNode = jsonObject.get(split[split.length - 1]);
        if (jsonNode == null) {
            return "";
        } else {
            return jsonNode.asText();
        }
    }

    public static void main(String[] args) throws IOException {
        JsonNode jsonObject = new ObjectMapper().readTree("{" +
                "    \"a\":[" +
                "        {" +
                "            \"b\":\"1\"," +
                "            \"c\":2," +
                "            \"d\":[" +
                "                {" +
                "                    \"e\":1," +
                "                    \"f\":\"2\"" +
                "                }," +
                "                {" +
                "                    \"e\":3," +
                "                    \"f\":\"4\"" +
                "                }" +
                "            ]" +
                "        }," +
                "        {" +
                "            \"b\":\"3\"," +
                "            \"c\":4," +
                "            \"d\":[" +
                "                {" +
                "                    \"e\":5," +
                "                    \"f\":\"6\"" +
                "                }," +
                "                {" +
                "                    \"e\":7," +
                "                    \"f\":\"8\"" +
                "                }" +
                "            ]" +
                "        }" +
                "    ]," +
                "    \"g\":2," +
                "    \"h\":\"3\"" +
                "}");
        System.out.println(getField(jsonObject, "g"));
        System.out.println(getField(jsonObject, "h"));
        System.out.println(getField(jsonObject, "a[].b", 0));
        System.out.println(getField(jsonObject, "a[].c", 1));
        System.out.println(getField(jsonObject, "a[].d[].e", 0, 1));
        System.out.println(getField(jsonObject, "a[].d[].f", 0, 1));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("[1,2,3]");
        System.out.println(jsonNode);
    }
}
