package com.amysue.learn.jeesite.common.persistence;

import com.amysue.learn.jeesite.common.config.Global;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amysue on 2016/9/29.
 */
public class Page<T> {
    private int pageNo = 1; //当前页码
    private int pageSize = Integer.valueOf(Global.getConfig("page.pageSize")); //页面大小，设置为“-1”表示不进行分页

    private long count; //总记录数，设置为“-1”表示不查询总数

    private int first; // 首页索引
    private int last; // 尾页索引
    private int prev; //上一页索引
    private int next; //下一页索引

    private boolean firstPage; //是否是第一页
    private boolean lastPage; //是否是最后一页

    private int length = 8;
    private int slider = 1;

    private List<T> list = new ArrayList<T>();

    private String orderBy = ""; //查询排序 ，如 updatedate desc, name asc
    private String message = ""; // 设置提示消息，显示在共n条之后

    public Page() {
        this.pageSize = -1;
    }

    public Page(int pageNo, int pageSize, String orderBy) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public long getCount() {
        return count;
    }

    public int getFirstResult() {
        int firstResult = (getPageNo() - 1) * getPageSize();
        if (firstResult >= getCount()) {
            firstResult = 0;
        }
        return firstResult;
    }

    public int getMaxResults() {
        return getPageSize();
    }
}
