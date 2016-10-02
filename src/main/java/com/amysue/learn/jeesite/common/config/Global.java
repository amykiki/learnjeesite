package com.amysue.learn.jeesite.common.config;

import com.google.common.collect.Maps;
import com.amysue.learn.jeesite.common.utils.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 全局配置类
 * Created by Amysue on 2016/9/29.
 */
public class Global {
    /**
     * 单例模式，当前对象实例
     */
    private static Global global = new Global();
    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = Maps.newHashMap();

    private static PropertiesLoader loader = new PropertiesLoader("jeesite.properties");

    /**
     * 获取当前对象实例
     * @return
     */
    public static Global getInstance() {
        return global;
    }

    /**
     * 获取配置
     * @param key 配置名
     * @return 配置结果
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null) {
            value = loader.getProperty(key);
            map.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }


}
