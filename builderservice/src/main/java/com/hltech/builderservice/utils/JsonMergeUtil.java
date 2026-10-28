package com.hltech.builderservice.utils;



import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.util.Map;

public final class JsonMergeUtil {

    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    private JsonMergeUtil() {}

    /** Deep merge: existing data is kept, incoming adds new things / updates changed values. */
    public static JsonNode merge(JsonNode existing, JsonNode incoming) {
        if (isEmpty(incoming)) {
            return isEmpty(existing) ? MAPPER.createObjectNode() : existing.deepCopy();
        }
        if (isEmpty(existing)) {
            return incoming.deepCopy();
        }
        if (existing.isObject() && incoming.isObject()) {
            return mergeObjects((ObjectNode) existing, (ObjectNode) incoming);
        }
        if (existing.isArray() && incoming.isArray()) {
            return mergeArrays((ArrayNode) existing, (ArrayNode) incoming);
        }
        return incoming.deepCopy(); // plain value changed
    }

    private static JsonNode mergeObjects(ObjectNode existing, ObjectNode incoming) {
        ObjectNode result = existing.deepCopy();
        for (Map.Entry<String, JsonNode> e : incoming.properties()) {
            JsonNode value = e.getValue();
            if (value == null || value.isNull()) {
                continue; // null never wipes existing data
            }
            result.set(e.getKey(), merge(result.get(e.getKey()), value));
        }
        return result;
    }

    private static JsonNode mergeArrays(ArrayNode existing, ArrayNode incoming) {
        ArrayNode result = existing.deepCopy();
        for (JsonNode item : incoming) {
            String key = keyOf(item);
            if (key != null) {
                int idx = indexOfKey(result, key);
                if (idx >= 0) {
                    result.set(idx, merge(result.get(idx), item)); // same id -> merge
                } else {
                    result.add(item.deepCopy());                   // new id -> add
                }
            } else if (!contains(result, item)) {
                result.add(item.deepCopy());                       // new value -> add
            }
        }
        return result;
    }

    /** Identity of an array element: "id" first, then "productId". */
    private static String keyOf(JsonNode item) {
        if (item == null || !item.isObject()) return null;
        JsonNode id = item.path("id");
        if (!id.isMissingNode() && !id.isNull()) return "id:" + id;
        JsonNode pid = item.path("productId");
        if (!pid.isMissingNode() && !pid.isNull()) return "productId:" + pid;
        return null;
    }

    private static int indexOfKey(ArrayNode array, String key) {
        for (int i = 0; i < array.size(); i++) {
            if (key.equals(keyOf(array.get(i)))) return i;
        }
        return -1;
    }

    private static boolean contains(ArrayNode array, JsonNode value) {
        for (JsonNode n : array) {
            if (n.equals(value)) return true;
        }
        return false;
    }

    private static boolean isEmpty(JsonNode n) {
        return n == null || n.isNull() || n.isMissingNode();
    }
}