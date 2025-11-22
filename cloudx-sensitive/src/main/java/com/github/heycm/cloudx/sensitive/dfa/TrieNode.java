package com.github.heycm.cloudx.sensitive.dfa;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;

/**
 * DFA字典树节点
 * @author heycm
 * @version 1.0
 * @since 2025/11/22 14:06
 */
public class TrieNode {

    /**
     * 子节点
     */
    private final Map<Character, TrieNode> children = new ConcurrentHashMap<>();

    /**
     * 是否是敏感词的结尾
     */
    @Getter
    @Setter
    private boolean isEnd = false;

    /**
     * 获取子节点
     * @param c 子节点字符
     * @return 子节点
     */
    public TrieNode getChild(Character c) {
        return children.get(c);
    }

    /**
     * 添加子节点
     * @param c 子节点字符
     * @return 子节点
     */
    public TrieNode addChild(Character c) {
        return children.computeIfAbsent(c, k -> new TrieNode());
    }

    public void setEnd() {
        setEnd(true);
    }

}
