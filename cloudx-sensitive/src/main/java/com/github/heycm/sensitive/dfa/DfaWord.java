package com.github.heycm.sensitive.dfa;

import lombok.Data;

/**
 * DFA字典词
 * @author heycm
 * @version 1.0
 * @since 2025/11/22 20:53
 */
@Data
public class DfaWord {

    /**
     * 字典词本
     */
    private final String text;

    /**
     * 起始索引，左闭右开
     */
    private final int start;

    /**
     * 结束索引，左闭右开
     */
    private final int end;

    public DfaWord(String text, int start, int end) {
        this.text = text;
        this.start = start;
        this.end = end;
    }
}
