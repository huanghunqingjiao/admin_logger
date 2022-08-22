package com.logger.demo264.logrecord.context;

import cn.hutool.core.lang.Assert;

import java.util.*;

/**
 * 自定义添加的参数
 *
 * @author 风尘
 * @date 2022-07-18
 */
public class LogRecordContext {

    private static final InheritableThreadLocal<Stack<Map<String, Object>>> VARIABLE_MAP_STACK = new InheritableThreadLocal<>();

    /**
     * 初始化数据
     */
    public static void init() {
        if (Objects.isNull(VARIABLE_MAP_STACK.get())) {
            VARIABLE_MAP_STACK.set(new Stack<>());
        }
        Stack<Map<String, Object>> mapStack = VARIABLE_MAP_STACK.get();
        mapStack.push(new HashMap<>(16));
    }

    public static void addVariable(String name, Object value) {
        Stack<Map<String, Object>> mapStack = VARIABLE_MAP_STACK.get();
        Assert.notNull(mapStack, "please init first");
        Map<String, Object> maps = mapStack.peek();
        maps.put(name, value);
    }


    public static Map<String, Object> getVariables() {
        Stack<Map<String, Object>> mapStack = VARIABLE_MAP_STACK.get();
        Assert.notNull(mapStack, "please init first");
        Map<String, Object> maps = mapStack.peek();
        return Collections.unmodifiableMap(maps);
    }

    public static void remove() {
        Stack<Map<String, Object>> mapStack = VARIABLE_MAP_STACK.get();
        Assert.notNull(mapStack, "please init first");
        mapStack.pop();
        if (mapStack.isEmpty()) {
            VARIABLE_MAP_STACK.remove();
        }
    }
}
