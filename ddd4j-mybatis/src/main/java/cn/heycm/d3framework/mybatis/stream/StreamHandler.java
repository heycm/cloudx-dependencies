package cn.heycm.d3framework.mybatis.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

/**
 * 流读结果批处理
 * @author heycm
 * @version 1.0
 * @since 2025/8/25 22:37
 */
public abstract class StreamHandler<T> implements ResultHandler<T> {

    /**
     * 批次大小
     */
    private final int batchSize;

    /**
     * 批次数据
     */
    private final List<T> batchData;

    /**
     * 总读行数
     */
    private int rows = 0;

    public StreamHandler(int batchSize) {
        this.batchSize = batchSize;
        this.batchData = new ArrayList<>(batchSize);
    }

    /**
     * 收集流查询结果，每读一行数据，都会调用此方法
     * @param resultContext 数据行
     */
    @Override
    public void handleResult(ResultContext<? extends T> resultContext) {
        rows++;
        batchData.add(resultContext.getResultObject());
        if (rows % batchSize == 0 || resultContext.isStopped()) {
            this.flush();
        }
    }

    /**
     * 启动流处理
     * @param streamSelect Mapper流查询
     */
    public void start(Consumer<StreamHandler<T>> streamSelect) {
        streamSelect.accept(this);
        // 流读结束后 flush 一次避免残留缓存数据未处理
        this.flush();
    }

    /**
     * 批次处理
     */
    private void flush() {
        if (batchData.isEmpty()) {
            return;
        }
        this.batchHandler(batchData);
        batchData.clear();
    }

    /**
     * 批次处理自定义业务逻辑
     * @param batchData 批次数据
     */
    protected abstract void batchHandler(List<T> batchData);
}
