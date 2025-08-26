package cn.heycm.d3framework.uid;

import cn.heycm.d3framework.uid.service.UidService;

/**
 * UID静态工具类
 * @author heycm
 * @version 1.0
 * @since 2025/8/26 23:06
 */
public class UidGenerator {

    private static UidService uidService;

    static void setUidService(UidService uidService) {
        if (uidService == null) {
            throw new IllegalArgumentException("UidService can not be null.");
        }
        UidGenerator.uidService = uidService;
    }

    private static UidService getUidService() {
        if (uidService == null) {
            throw new IllegalStateException("UidService is not injected.");
        }
        return uidService;
    }

    /**
     * 获取id
     * @return id
     */
    public static long nextId() {
        return getUidService().nextId();
    }
}
