package com.github.heycm.cloudx.uid;

import com.baidu.fsg.uid.UidGenerator;
import com.baidu.fsg.uid.impl.CachedUidGenerator;
import com.baidu.fsg.uid.worker.WorkerIdAssigner;
import com.github.heycm.cloudx.uid.properties.UidProperties;
import com.github.heycm.cloudx.uid.service.BaiduUidServiceImpl;
import com.github.heycm.cloudx.uid.service.ShardUidService;
import com.github.heycm.cloudx.uid.service.ShardUidServiceImpl;
import com.github.heycm.cloudx.uid.service.UidService;
import com.github.heycm.cloudx.uid.shard.impl.ShardCachedUidGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * UID自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/26 22:10
 */
@Configuration
@ConditionalOnProperty(name = "cloudx.uid.worker-id")
@EnableConfigurationProperties(UidProperties.class)
public class UidAutoConfiguration {

    @Bean
    public WorkerIdAssigner workerIdAssigner(UidProperties uidProperties) {
        return uidProperties::getWorkerId;
    }

    @Bean
    public UidGenerator uidGenerator(WorkerIdAssigner workerIdAssigner, UidProperties uidProperties) {
        if (uidProperties.getShardBits() > 0) {
            ShardCachedUidGenerator uidGenerator = new ShardCachedUidGenerator();
            uidGenerator.setWorkerIdAssigner(workerIdAssigner);
            uidGenerator.setTimeBits(uidProperties.getTimeBits());
            uidGenerator.setWorkerBits(uidProperties.getWorkerBits());
            uidGenerator.setShardBits(uidProperties.getShardBits());
            uidGenerator.setSeqBits(uidProperties.getSeqBits());
            uidGenerator.setEpochStr(uidProperties.getEpochStr());
            return uidGenerator;
        }
        CachedUidGenerator uidGenerator = new CachedUidGenerator();
        uidGenerator.setWorkerIdAssigner(workerIdAssigner);
        uidGenerator.setTimeBits(uidProperties.getTimeBits());
        uidGenerator.setWorkerBits(uidProperties.getWorkerBits());
        uidGenerator.setSeqBits(uidProperties.getSeqBits());
        uidGenerator.setEpochStr(uidProperties.getEpochStr());
        return uidGenerator;
    }

    @Bean
    @ConditionalOnBean(CachedUidGenerator.class)
    public UidService uidService(CachedUidGenerator uidGenerator) {
        BaiduUidServiceImpl uidService = new BaiduUidServiceImpl(uidGenerator);
        com.github.heycm.cloudx.uid.UidGenerator.setUidService(uidService);
        return uidService;
    }

    @Bean
    @ConditionalOnBean(ShardCachedUidGenerator.class)
    public ShardUidService shardUidService(ShardCachedUidGenerator uidGenerator) {
        return new ShardUidServiceImpl(uidGenerator);
    }
}
