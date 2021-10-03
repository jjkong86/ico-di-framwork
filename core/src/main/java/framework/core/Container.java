package framework.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Container {
    private static final Map<String, Object> beans = new ConcurrentHashMap<>();

    public static Map<String, Object> getBeans() {
        return beans;
    }
}
