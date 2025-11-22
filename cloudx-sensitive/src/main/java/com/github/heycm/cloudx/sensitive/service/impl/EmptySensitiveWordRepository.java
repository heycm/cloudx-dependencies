package com.github.heycm.cloudx.sensitive.service.impl;

import com.github.heycm.cloudx.sensitive.service.SensitiveWordRepository;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 空词库
 * @author heycm
 * @version 1.0
 * @since 2025/11/22 22:01
 */
public class EmptySensitiveWordRepository implements SensitiveWordRepository {

    @Override
    public void addWord(String word) {

    }

    @Override
    public void addWords(Set<String> words) {

    }

    @Override
    public void removeWord(String word) {

    }

    @Override
    public void removeWords(Set<String> words) {

    }

    @Override
    public List<String> loadAllWords() {
        return List.of();
    }

    @Override
    public void foreach(Consumer<String> forEach) {

    }
}
