package com.amysue.learn.jeesite.common.persistence.dialect.db;

import com.amysue.learn.jeesite.common.persistence.dialect.Dialect;

/**
 * Created by Amysue on 2016/10/3.
 */
public class MySQLDialect implements Dialect {
    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql, offset, Integer.toString(offset), Integer.toString(limit));
    }

    /**
     * 将sql变成分页sql语句，将offset及limit使用占位符号(placeholder)替换
     * <pre>
     *     如mysql
     *     dialect.getLimitString("select * from user", 12, ":offset", ":limit") 将返回
     *     select * from user limit :offset, :limit
     * </pre>
     * @param sql               实际sql语句
     * @param offset            分页开始记录条数
     * @param offsetPlaceholder 分页开始记录条数-占位符号
     * @param limitPlaceholder  分页记录条数占位符号
     * @return 包含占位符的分页sql
     */
    private String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
        StringBuilder sb = new StringBuilder(sql);
        sb.append(" limit ");
        if (offset > 0) {
            sb.append(offsetPlaceholder).append(",").append(limitPlaceholder);
        } else {
            sb.append(limitPlaceholder);
        }
        return sb.toString();
    }
}
