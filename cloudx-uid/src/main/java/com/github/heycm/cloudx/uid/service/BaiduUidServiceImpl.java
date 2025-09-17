package com.github.heycm.cloudx.uid.service;

import com.baidu.fsg.uid.UidGenerator;

/**
 * 百度UID生成服务
 * @author heycm
 * @version 1.0
 * @since 2025/8/26 23:00
 */
public class BaiduUidServiceImpl implements UidService {

    private final UidGenerator uidGenerator;

    public BaiduUidServiceImpl(UidGenerator uidGenerator) {
        this.uidGenerator = uidGenerator;
    }

    @Override
    public long nextId() {
        return uidGenerator.getUID();
    }
}
