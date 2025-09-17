package com.github.heycm.cloudx.shortid.entity;

/**
 * 短ID实体
 * @author heycm
 * @version 1.0
 * @since 2025/8/30 22:59
 */
public class ShortId {

    /**
     * 短ID键
     */
    private String idKey;

    /**
     * ID值
     */
    private Integer idValue;

    /**
     * 步长
     */
    private Integer idStep;

    /**
     * 下一个ID
     */
    private int nextId;

    /**
     * 本段最大ID
     */
    private int maxId;

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public Integer getIdValue() {
        return idValue;
    }

    public void setIdValue(Integer idValue) {
        this.idValue = idValue;
    }

    public Integer getIdStep() {
        return idStep;
    }

    public void setIdStep(Integer idStep) {
        this.idStep = idStep;
    }

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

    public int getMaxId() {
        return maxId;
    }

    public void setMaxId(int maxId) {
        this.maxId = maxId;
    }
}
