package com.amysue.learn.jeesite.common.persistence.interceptor;

import com.amysue.learn.jeesite.common.persistence.Page;
import com.amysue.learn.jeesite.common.utils.Reflections;
import com.amysue.learn.jeesite.common.utils.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Properties;

/**
 * Created by Amysue on 2016/9/30.
 */
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class PageInterceptor implements Interceptor, Serializable {
    private static final String PAGE = "page";
    private static final Logger logger = Logger.getLogger(PageInterceptor.class);
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] args = invocation.getArgs();
        final MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Object parameterObject = boundSql.getParameterObject();
        Page<?> page = null;
        if (parameterObject != null) {
            page = convertParameter(parameterObject);
        }
        if (page != null && page.getPageSize() != -1) {
            if (StringUtils.isBlank(boundSql.getSql())) {
                return null;
            }
            String orginalSql = boundSql.getSql().trim();
        }
        return null;
    }

    private static Page<?> convertParameter(Object parameterObject) {
        try {
            if (parameterObject instanceof Page) {
                return (Page<?>) parameterObject;
            } else {
                return (Page<?>)Reflections.getFieldValue(parameterObject, PAGE);
            }
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
