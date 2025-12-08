package util;

import java.lang.reflect.Field;
import java.util.*;

/**
 * [PATTERN: INTERPRETER] (Slightly loose interpretation, but fits for parsing)
 * Reason: Interpreting JSON string structures into Java Objects and vice versa.
 * 
 * Helper class to parse and stringify JSON without external libraries.
 * This ensures the project is standalone and demonstrates low-level handling.
 */
public class JSONHelper {

    // [PATTERN: SINGLETON]
    // Reason: We only need one instance of the helper utilities or static access.
    // However, purely static methods are often used for utils. 
    // We will keep it static for utility but internal logic could be instance based if needed.

    public static String toJson(Object object) {
        if (object == null) return "null";
        
        if (object instanceof String) {
            return "\"" + escape((String) object) + "\"";
        }
        
        if (object instanceof Number || object instanceof Boolean) {
            return object.toString();
        }
        
        if (object instanceof List) {
            List<?> list = (List<?>) object;
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < list.size(); i++) {
                sb.append(toJson(list.get(i)));
                if (i < list.size() - 1) sb.append(",");
            }
            sb.append("]");
            return sb.toString();
        }
        
        if (object instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) object;
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            int i = 0;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                sb.append("\"").append(entry.getKey()).append("\":");
                sb.append(toJson(entry.getValue()));
                if (i < map.size() - 1) sb.append(",");
                i++;
            }
            sb.append("}");
            return sb.toString();
        }

        // Reflection for objects
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            Field[] fields = object.getClass().getDeclaredFields();
            int i = 0;
            for (Field field : fields) {
                field.setAccessible(true);
                // Skip transient or synthetic
                if (java.lang.reflect.Modifier.isTransient(field.getModifiers())) continue;

                sb.append("\"").append(field.getName()).append("\":");
                sb.append(toJson(field.get(object)));
                if (i < fields.length - 1) sb.append(",");
                i++;
            }
            // cleanup trailing comma if exists
            if (sb.length() > 1 && sb.charAt(sb.length() - 1) == ',') {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("}");
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    private static String escape(String s) {
        return s.replace("\"", "\\\"");
    }
    
    // Very basic parser that returns Map<String, Object> or List<Object>
    // This is a naive implementation for the sake of the exercise constraints (No DB, pure Java)
    public static Object parse(String json) {
        json = json.trim();
        if (json.startsWith("{")) {
            return parseObject(json);
        } else if (json.startsWith("[")) {
            return parseArray(json);
        } else if (json.startsWith("\"")) {
            return json.substring(1, json.length() - 1);
        } else if (json.equals("true")) return true;
        else if (json.equals("false")) return false;
        else if (json.matches("-?\\d+(\\.\\d+)?")) {
            if (json.contains(".")) return Double.parseDouble(json);
            return Integer.parseInt(json);
        }
        return null;
    }

    private static Map<String, Object> parseObject(String json) {
        Map<String, Object> map = new HashMap<>();
        String content = json.substring(1, json.length() - 1).trim();
        if (content.isEmpty()) return map;

        List<String> tokens = splitByComma(content);
        for (String token : tokens) {
            int colonIndex = token.indexOf(':');
            String key = token.substring(0, colonIndex).trim().replace("\"", "");
            String valueStr = token.substring(colonIndex + 1).trim();
            map.put(key, parse(valueStr));
        }
        return map;
    }

    private static List<Object> parseArray(String json) {
        List<Object> list = new ArrayList<>();
        String content = json.substring(1, json.length() - 1).trim();
        if (content.isEmpty()) return list;

        List<String> tokens = splitByComma(content);
        for (String token : tokens) {
            list.add(parse(token.trim()));
        }
        return list;
    }

    private static List<String> splitByComma(String content) {
        List<String> tokens = new ArrayList<>();
        int braceCount = 0;
        int bracketCount = 0;
        boolean inQuote = false;
        StringBuilder currentToken = new StringBuilder();

        for (char c : content.toCharArray()) {
            if (c == '\"') inQuote = !inQuote;
            if (!inQuote) {
                if (c == '{') braceCount++;
                if (c == '}') braceCount--;
                if (c == '[') bracketCount++;
                if (c == ']') bracketCount--;
            }
            
            if (c == ',' && braceCount == 0 && bracketCount == 0 && !inQuote) {
                tokens.add(currentToken.toString());
                currentToken = new StringBuilder();
            } else {
                currentToken.append(c);
            }
        }
        tokens.add(currentToken.toString());
        return tokens;
    }
}
