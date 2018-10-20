package spring.test.util;

import java.util.Map;

public class RequestContextUtil {

    private static ThreadLocal<Map<String, String>> context = new ThreadLocal<Map<String, String>>();
    private static ThreadLocal<String> uri = new ThreadLocal<String>();

    public static void setContext(Map<String, String> params) {
        context.set(params);
    }

    public static Map<String, String> getContext() {
        return context.get();
    }

    public static void setUri(String loc) {
        uri.set(loc);
    }

    public static String getUri() {
        return uri.get();
    }

    public static void clear() {
        context.remove();
        uri.remove();
    }

}
