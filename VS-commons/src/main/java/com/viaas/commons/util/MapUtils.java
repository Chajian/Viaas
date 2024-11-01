package com.viaas.commons.util;

import lombok.NonNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * map utils
 * @author Chajian
 * @date 20240924
 */
public class MapUtils {
    /**
     * convert Java Entity to Map
     * @param obj
     * @return
     */
    public static Map<String, Object> convertToMap(@NonNull Object obj) {
        Map<String, Object> resultMap = new HashMap<>();
        // Get all declared fields of the class
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true); // Allows access to private fields

            try {
                // Add the field name and its value to the map
                resultMap.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // Handle exception
            }
        }

        return resultMap;
    }
}
