package com.github.heycm.cloudx.sensitive.service;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 敏感词词库接口
 * @author heycm
 * @version 1.0
 * @since 2025/11/22 21:59
 */
public interface SensitiveWordRepository {

    /**
     * 添加敏感词
     * @param word 敏感词
     */
    void addWord(String word);

    /**
     * 批量添加敏感词
     * @param words 敏感词
     */
    void addWords(Set<String> words);

    /**
     * 删除敏感词
     * @param word 敏感词
     */
    void removeWord(String word);

    /**
     * 批量删除敏感词
     * @param words 敏感词
     */
    void removeWords(Set<String> words);

    /**
     * 加载所有敏感词，若词库庞大，慎重使用
     * @return
     */
    List<String> loadAllWords();

    /**
     * 遍历所有敏感词
     * @param forEach 遍历回调
     */
    void foreach(Consumer<String> forEach);
}
