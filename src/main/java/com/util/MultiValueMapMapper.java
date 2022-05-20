package com.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;

public abstract class MultiValueMapMapper {
    public static MultiValueMap<String, Object> toMultiValueMap(Object object) {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap();

        Arrays.stream(object.getClass().getDeclaredFields())
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        multiValueMap.add(field.getName(), field.get(object));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        return multiValueMap;
    }
}
