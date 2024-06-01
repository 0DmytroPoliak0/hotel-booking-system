package org.example;

import java.util.List;
import java.util.Map;

public class JsonUtils {

    public static String toJson(List<Map<String, Object>> dataList) {
        StringBuilder json = new StringBuilder("[");
        for (Map<String, Object> data : dataList) {
            json.append("{");
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                json.append("\"").append(entry.getKey()).append("\":");
                if (entry.getValue() instanceof Number || entry.getValue() instanceof Boolean) {
                    json.append(entry.getValue());
                } else {
                    json.append("\"").append(entry.getValue()).append("\"");
                }
                json.append(",");
            }
            json.deleteCharAt(json.length() - 1); // Remove the last comma
            json.append("},");
        }
        if (!dataList.isEmpty()) {
            json.deleteCharAt(json.length() - 1); // Remove the last comma
        }
        json.append("]");
        return json.toString();
    }
}

