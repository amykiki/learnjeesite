package com.amysue.learn.jeesite.common.persistence.interceptor;

import com.amysue.learn.jeesite.common.config.Global;
import com.amysue.learn.jeesite.common.persistence.Page;
import com.amysue.learn.jeesite.common.persistence.dialect.Dialect;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Amysue on 2016/10/2.
 */
public class SQLHelper {
    /**
     * 对SQL参数(?)设值
     * @param ps 预编译的SQL语句对象
     * @param mappedStatement MappedStatement
     * @param boundSql BoundSql
     * @param parameterObject 参数对象
     * @throws java.sql.SQLException 数据库异常
     */
    public static void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject)
    throws SQLException{
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            Configuration       configuration       = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject          metaObject          = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
                        }
                    } else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
                }
            }
        }
    }
    /**
     * @param sql             SQL语句
     * @param connection      数据库连接
     * @param mappedStatement mapped statement
     * @param parameterObject sql参数
     * @param boundSql
     * @param log
     * @return 总记录数
     */
    public static int getCount(final String sql, final Connection connection, final MappedStatement mappedStatement,
            final Object parameterObject, final BoundSql boundSql, Log log) {
        String dbName = Global.getConfig("jdbc.type");
        return 0;
    }

    public static String generatePageSql(String sql, Page<?> page, Dialect dialect) {
        if (dialect.supportsLimit()) {
            return dialect.getLimitString(sql, page.getFirstResult(), page.getMaxResults());
        } else {
            return sql;
        }
    }
    /**
     * 去除sql的orderBy子句
     * @param sqlString
     * @return
     */
    private static String removeOrders(String sqlString) {
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sqlString);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
