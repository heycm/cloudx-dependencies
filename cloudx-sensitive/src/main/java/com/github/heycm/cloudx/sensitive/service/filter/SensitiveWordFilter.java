package com.github.heycm.cloudx.sensitive.service.filter;

import com.github.heycm.cloudx.sensitive.dfa.DfaTrie;
import com.github.heycm.cloudx.sensitive.dfa.DfaWord;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 敏感词过滤器
 * @author heycm
 * @version 1.0
 * @since 2025/11/21 17:25
 */
public class SensitiveWordFilter {

    private static final String WORD_REPLACEMENT = "*";

    /**
     * DFA字典树，匹配敏感词，时间复杂度为O(n)
     */
    private DfaTrie dfaTrie;

    /**
     * 构造函数
     */
    public SensitiveWordFilter() {
        this.dfaTrie = new DfaTrie();
    }

    /**
     * 添加敏感词
     * @param word 敏感词
     */
    public void addWord(String word) {
        dfaTrie.addWord(word);
    }

    /**
     * 批量添加敏感词
     * @param iterable 敏感词迭代器
     */
    public void addWords(Iterable<String> iterable) {
        dfaTrie.addWords(iterable);
    }

    /**
     * 判断文本是否包含敏感词
     * @param text 待检测文本
     * @return
     */
    public boolean contains(String text) {
        return dfaTrie.contains(text);
    }

    /**
     * 查找文本中所有的敏感词
     * @param text 待检测文本
     * @return 敏感词
     */
    public Set<String> findAll(String text) {
        LinkedList<DfaWord> words = dfaTrie.findAll(text);
        if (words.isEmpty()) {
            return Collections.emptySet();
        }
        return words.stream().map(DfaWord::getText).collect(Collectors.toSet());
    }

    /**
     * 查找文本中任意一个敏感词
     * @param text 待检测文本
     * @return 敏感词
     */
    public String findAny(String text) {
        DfaWord word = dfaTrie.findAny(text);
        return word == null ? null : word.getText();
    }

    /**
     * 替换文本中的敏感词
     * @param text 待检测文本
     * @return
     */
    public String replace(String text) {
        return dfaTrie.filter(text, WORD_REPLACEMENT);
    }

    /**
     * 替换文本中的敏感词
     * @param text        待检测文本
     * @param replacement 替换词
     * @return
     */
    public String replace(String text, String replacement) {
        return dfaTrie.filter(text, replacement);
    }

    /**
     * 重置过滤器
     */
    public void reset(SensitiveWordFilter filter) {
        dfaTrie = filter.dfaTrie;
    }
}
