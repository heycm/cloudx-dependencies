package com.github.heycm.cloudx.sensitive.service;

import java.util.Set;

/**
 * 敏感词检测服务，对外服务接口
 * @author heycm
 * @version 1.0
 * @since 2025/11/22 22:04
 */
public interface SensitiveWordService {

    /**
     * 检测文本中是否包含敏感词
     * @param text 待检测文本
     * @return 是否包含敏感词
     */
    boolean anyMatch(String text);

    /**
     * 检测文本中任意敏感词
     * @param text 待检测文本
     * @return 敏感词
     */
    String findAny(String text);

    /**
     * 检测文本中所有敏感词
     * @param text 待检测文本
     * @return 敏感词
     */
    Set<String> findAll(String text);

    /**
     * 检测文本并替换敏感词
     * @param text 待检测文本
     * @return 过滤后的文本
     */
    String replace(String text);

    /**
     * 检测文本并替换敏感词
     * @param text        待检测文本
     * @param replacement 替换的文本
     * @return 过滤后的文本
     */
    String replace(String text, String replacement);

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
     * 刷新敏感词
     */
    void refreshFilter();
}
