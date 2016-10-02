package com.amysue.learn.jeesite.common.utils;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * Properties文件载入工具类，
 * 可载入多个properti文件，相同属性在最后载入的文件中的值将会覆盖之前的值，但以System的Property优先
 * Created by Amysue on 2016/9/29.
 */
public class PropertiesLoader {
    private static Logger         logger         = LogManager.getLogger(PropertiesLoader.class);
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    private final Properties properties;

    public PropertiesLoader(String... resourcesPaths) {
        properties = loadProperties(resourcesPaths);
    }

    /**
     * 载入多个文件
     * @param resourcesPaths 使用Spring Resource格式
     * @return
     */
    private Properties loadProperties(String... resourcesPaths) {
        Properties props = new Properties();
        for (String location : resourcesPaths) {
            InputStream is = null;
            try {
                Resource resource = resourceLoader.getResource(location);
                is = resource.getInputStream();
                props.load(is);
            } catch (IOException e) {
                logger.info("Could not load properties from path: " + location + ", " + e.getMessage());
            } finally {
                IOUtils.closeQuietly(is);
            }
        }
        return props;
    }

    /**
     *
     * @param key property 属性名
     * @return 以System Property 优先，取不到返回空字符串
     */
    private String getValue(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) {
            return systemProperty;
        }
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        }
        return "";
    }


    public String getProperty(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        String value = getValue(key);
        return value != null ? value : defaultValue;
    }

}
