package com.amysue.learn.jeesite.common.persistence.interceptor;

import com.amysue.learn.jeesite.common.config.Global;
import com.amysue.learn.jeesite.common.persistence.Page;
import com.amysue.learn.jeesite.common.persistence.dialect.Dialect;
import com.amysue.learn.jeesite.common.persistence.dialect.db.MySQLDialect;
import com.amysue.learn.jeesite.common.utils.Reflections;
import com.amysue.learn.jeesite.common.utils.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
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
    private Dialect DIALECT;
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
            String originalSql = boundSql.getSql().trim();
            // 得到总记录数
            page.setCount(SQLHelper.getCount(originalSql, null, mappedStatement, parameterObject, boundSql, logger));
            //分页查询，本地化对象 修改数据库
            String pageSql = SQLHelper.generatePageSql(originalSql, page, DIALECT);
            if (logger.isDebugEnabled()) {
                logger.debug("PAGE SQL: " + StringUtils.replace(pageSql, "\n", ""));

            }
            invocation.getArgs()[2] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
            BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), pageSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            //=========解决Mybatis分页foreach参数失效 Begin======
            if (Reflections.getFieldValue(boundSql, "metaParameters") != null) {
                MetaObject mo = (MetaObject)Reflections.getFieldValue(boundSql, "metaParameters");
                Reflections.setFieldValue(newBoundSql, "metaParameters", mo);
            }
            //=========解决Mybatis分页foreach参数失效 End======
            MappedStatement newMS = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));
            invocation.getArgs()[0] = newMS;
        }
        return invocation.proceed();
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
        initProperties(properties);
    }

    /**设置属性，支持自定义方言类和制定数据库的方式
     * <code>dialectClass</code>,自定义方言类，可以不配置这项
     * <code>dbms</code>数据库类型，插件支持的数据库
     * <cod>sqlPattern</cod> 需要拦截的SQL ID
     * @param p 属性
     */
    private void initProperties(Properties p) {
        Dialect dialect = null;
        String dbType = Global.getConfig("jdbc.type");
        if ("mysql".equals(dbType)) {
            dialect = new MySQLDialect();
        }
        if (dialect == null) {
            throw new RuntimeException("mybatis dialect error.");
        }
        DIALECT = dialect;
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource sqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), sqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null) {
            for (String keyProperty : ms.getKeyProperties()) {
                builder.keyProperty(keyProperty);
            }
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }
    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }
        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

}
