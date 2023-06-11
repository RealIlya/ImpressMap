package com.example.impressmap.model.data;

import java.util.Map;

public interface DatabaseTransfer<T>
{
    /**
     * Converts data to map
     */
    Map<String, Object> toMap(T t);
}
