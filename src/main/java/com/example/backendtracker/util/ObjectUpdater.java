package com.example.backendtracker.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.util.Map;

public class ObjectUpdater {
    public static <T> T updateFieldsFromJson(T object, String jsonString) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(jsonString,
                objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));

        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();
            if (jsonMap.containsKey(fieldName)) {
                field.setAccessible(true);
                field.set(object, jsonMap.get(fieldName));
            }
        }

        return object;
    }

}
