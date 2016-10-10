/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.common.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.*;

//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;

/**
 * 基于JMX动态配置Log4J日志级别，并控制Trace开关的MBean.
 * 
 * @author calvin
 * @version 2013-01-15
 */
@ManagedResource(objectName = Log4jManager.MBEAN_NAME, description = "Log4j Management Bean")
public class Log4jManager {

	/**
	 * Log4jManager的Mbean的注册名称.
	 */
	public static final String MBEAN_NAME = "log4j:name=Log4j";

	private static org.slf4j.Logger managerLogger = LoggerFactory.getLogger(Log4jManager.class);

	private String projectLoggerName;

	@ManagedAttribute(description = "Level of the root logger")
	public String getRootLoggerLevel() {
		Logger logger = LogManager.getRootLogger();
//		return logger.getEffectiveLevel().toString();
        return logger.getLevel().toString();
	}

	@ManagedAttribute
	public void setRootLoggerLevel(String newLevel) {
		Level level = Level.toLevel(newLevel);
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(level);
        ctx.updateLoggers();
		managerLogger.info("设置Root Logger 级别为{}", newLevel);
	}

	/**
	 * 获得项目默认logger的级别.
	 * 项目默认logger名称通过#setProjectLoggerName(String)配置.
	 */
	@ManagedAttribute(description = "Level of the project default package logger")
	public String getProjectLoggerLevel() {
		if (projectLoggerName != null) {
			return getLoggerLevel(projectLoggerName);
		}

		return null;
	}

	/**
	 * 设置项目默认logger的级别.
	 * 项目默认logger名称通过#setProjectLoggerName(String)配置.
	 */
	@ManagedAttribute
	public void setProjectLoggerLevel(String newLevel) {
		if (projectLoggerName != null) {
			setLoggerLevel(projectLoggerName, newLevel);
		}
	}

	/**
	 * 获取Logger的日志级别.
	 */
	@ManagedOperation(description = "Get logging level of the logger")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "loggerName", description = "Logger name") })
	public String getLoggerLevel(String loggerName) {
//		Logger logger = Logger.getLogger(loggerName);
        Logger logger = LogManager.getLogger(loggerName);
        return logger.getLevel().toString();
	}

	/**
	 * 设置Logger的日志级别.
	 * 如果日志级别名称错误, 设为DEBUG.
	 */
	@ManagedOperation(description = "Set new logging level to the logger")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "loggerName", description = "Logger name"),
			@ManagedOperationParameter(name = "newlevel", description = "New level") })
	public void setLoggerLevel(String loggerName, String newLevel) {
        Level level = Level.toLevel(newLevel);
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(loggerName);
        loggerConfig.setLevel(level);
        ctx.updateLoggers();
        managerLogger.info("设置{}级别为{}", loggerName, newLevel);
	}

	/**
	 * 根据log4j.properties中的定义, 设置项目默认的logger名称, 如com.thinkgem.jeesite.
	 */
	public void setProjectLoggerName(String projectLoggerName) {
		this.projectLoggerName = projectLoggerName;
	}

}