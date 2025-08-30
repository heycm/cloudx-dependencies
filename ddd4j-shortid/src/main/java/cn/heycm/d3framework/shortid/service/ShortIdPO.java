package cn.heycm.d3framework.shortid.service;

/**
 * @author heycm
 * @version 1.0
 * @since 2025/8/30 23:23
 */
public class ShortIdPO {

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
}
