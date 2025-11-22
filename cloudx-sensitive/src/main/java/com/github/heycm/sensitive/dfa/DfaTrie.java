package com.github.heycm.sensitive.dfa;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.util.StringUtils;

/**
 * DFA字典树
 * @author heycm
 * @version 1.0
 * @since 2025/11/22 14:10
 */
public class DfaTrie {

    private static final String WORD_REPLACEMENT = "*";

    /**
     * 根节点
     */
    private final TrieNode root = new TrieNode();

    /**
     * 最小字典词长度，小于此长度的字典词可不用检测，默认为 1，在加入字典词时更新此值
     */
    private final AtomicInteger minimumWordLength = new AtomicInteger(1);

    /**
     * 添加字典词
     * @param word 字典词
     */
    public void addWord(String word) {
        if (!StringUtils.hasText(word)) {
            return;
        }
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.addChild(c);
        }
        node.setEnd();
        updateMinimumWordLength(word.length());
    }

    /**
     * 批量添加字典词
     * @param iterable 字典词迭代器
     */
    public void addWords(Iterable<String> iterable) {
        iterable.forEach(this::addWord);
    }

    /**
     * 判断文本是否包含字典词
     * @param text 待检测文本
     * @return 是否包含字典词
     */
    public boolean contains(String text) {
        if (!StringUtils.hasText(text) || text.length() < minimumWordLength.get()) {
            return false;
        }
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (dfaMatch(chars, i) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查找文本中的所有字典词
     * @param text 待检测文本
     * @return 匹配到的字典词
     */
    public LinkedList<DfaWord> findAll(String text) {
        LinkedList<DfaWord> result = new LinkedList<>();
        if (!StringUtils.hasText(text) || text.length() < minimumWordLength.get()) {
            return result;
        }
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; ) {
            DfaWord word = dfaWordMatch(chars, i);
            if (word != null) {
                result.add(word);

                // 匹配到的字符串时索引右移
                i = Math.max(i + 1, word.getEnd());
            } else {
                i++;
            }

        }
        return result;
    }

    /**
     * 找到文本中的任意字典词
     * @param text 待检测文本
     * @return 匹配到的字典词
     */
    public DfaWord findAny(String text) {
        if (!StringUtils.hasText(text) || text.length() < minimumWordLength.get()) {
            return null;
        }
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            DfaWord word = dfaWordMatch(chars, i);
            if (word != null) {
                return word;
            }
        }
        return null;
    }

    /**
     * 查找并替换文本中的字典词
     * @param text 待检测文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        return filter(text, WORD_REPLACEMENT);
    }

    /**
     * 查找并替换文本中的字典词
     * @param text        待检测文本
     * @param replacement 替换的字符
     * @return 过滤后的文本
     */
    private String filter(String text, String replacement) {
        // 查找所有字典词
        LinkedList<DfaWord> words = findAll(text);
        if (words.isEmpty()) {
            return text;
        }
        // 注意：词替换必须逆序进行，避免替换后索引位置失效
        // LinkedList 为双向链表，逆序遍历时间复杂度为O(n)，但要使用专门的倒序迭代器，若使用 fori 逆序循环，时间复杂度为O(n^2)
        Iterator<DfaWord> iterator = words.descendingIterator();
        StringBuilder result = new StringBuilder(text);
        replacement = replacement == null ? WORD_REPLACEMENT : replacement;
        while (iterator.hasNext()) {
            DfaWord word = iterator.next();
            String placeholder = replacement.repeat(word.getEnd() - word.getStart());
            result.replace(word.getStart(), word.getEnd(), placeholder);
        }
        return result.toString();
    }

    /**
     * DFA匹配
     * @param chars 待检测字符数组
     * @param start 待检测字符数组的起始索引
     * @return 返回-1表示未匹配到字典词，否则返回匹配到的字典词右区间索引
     */
    private int dfaMatch(char[] chars, int start) {
        TrieNode node = root;
        for (int i = start; i < chars.length; i++) {
            char c = chars[i];
            node = node.getChild(c);
            if (node == null) {
                return -1;
            }
            if (node.isEnd()) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * DFA匹配
     * @param chars 待检测字符数组
     * @param start 待检测字符数组的起始索引
     * @return 匹配到的字典词
     */
    private DfaWord dfaWordMatch(char[] chars, int start) {
        TrieNode node = root;
        for (int i = start; i < chars.length; i++) {
            char c = chars[i];
            node = node.getChild(c);
            if (node == null) {
                return null;
            }
            if (node.isEnd()) {
                int end = i + 1;
                return new DfaWord(new String(chars, start, end - start), start, end);
            }
        }
        return null;
    }

    /**
     * 更新最小字典词长度
     * @param wordLength 新字典词长度
     */
    private void updateMinimumWordLength(int wordLength) {
        while (true) {
            int current = minimumWordLength.get();
            if (current >= wordLength) {
                return;
            }
            if (minimumWordLength.compareAndSet(current, wordLength)) {
                return;
            }
        }
    }
}