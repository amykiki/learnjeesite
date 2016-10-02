package com.amysue.learn.jeesite.common.persistence.interceptor;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.sql.Connection;

/**
 * Created by Amysue on 2016/10/2.
 */
public class SQLHelper {

    public static int getCount(final String sql, final Connection connection, final MappedStatement mappedStatement,
            final Object parameterObject, final BoundSql boundSql, Log log) {
        return 0;
    }
}
