package cn.heycm.d3framework.mybatis;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

/**
 * SqlSession 批处理工具
 * @author heycm
 * @version 1.0
 * @since 2025/8/25 22:27
 */
public final class BatchHelper {

    private static final int DEFAULT_BATCH_SIZE = 1000;

    private static SqlSessionFactory sqlSessionFactory;

    private BatchHelper() {
        throw new UnsupportedOperationException("Utility class");
    }

    static void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        if (sqlSessionFactory == null) {
            throw new IllegalArgumentException("SqlSessionFactory is null.");
        }
        BatchHelper.sqlSessionFactory = sqlSessionFactory;
    }

    private static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            throw new IllegalStateException("SqlSessionFactory is not injected.");
        }
        return sqlSessionFactory;
    }

    /**
     * 批处理执行
     * @param coll        批处理对象集合
     * @param mapperClass Mapper类
     * @param consumer    批处理对象处理函数
     * @return 批处理结果行数
     */
    public static <E, MP> int exec(Collection<E> coll, Class<MP> mapperClass, BiConsumer<E, MP> consumer) {
        SqlSession session = null;
        int i = 0;
        int size = coll.size();
        int rows = 0;
        try {
            session = getSqlSessionFactory().openSession(ExecutorType.BATCH);
            MP mapper = session.getMapper(mapperClass);
            for (E e : coll) {
                consumer.accept(e, mapper);
                i++;
                if (i == size || i % DEFAULT_BATCH_SIZE == 0) {
                    List<BatchResult> results = session.flushStatements();
                    rows += BatchHelper.getRows(results);
                }
            }
            // 非事务环境下强制commit，事务情况下该commit会跟随事务提交
            session.commit(!TransactionSynchronizationManager.isSynchronizationActive());
        } catch (Exception e) {
            if (session != null) {
                session.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return rows;
    }

    private static int getRows(List<BatchResult> results) {
        int rows = 0;
        if (CollectionUtils.isEmpty(results)) {
            return rows;
        }
        for (BatchResult result : results) {
            if (result.getUpdateCounts() != null) {
                for (int updateCount : result.getUpdateCounts()) {
                    rows += updateCount;
                }
            }
        }
        return rows;
    }
}
