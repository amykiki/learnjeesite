package com.amysue.learn.jeesite.common.persistence;

import com.amysue.learn.jeesite.modules.sys.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Map;

/**
 * Entity 支持类
 * Created by Amysue on 2016/10/6.
 */
public abstract class BaseEntity<T> implements Serializable {
    private static final long serialVersionUID = -6092539048081337270L;

    /**
     * 实体编号（唯一标识）
     */
    protected String id;
    protected User currentUser;
    protected Page<T> page;
    protected Map<String, String> sqlMap;
    protected  boolean isNewRecord = false;

    public BaseEntity() {

    }

    public BaseEntity(String id) {
        this();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    @XmlTransient
    public User getCurrentUser() {
        if (currentUser == null) {

        }
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
