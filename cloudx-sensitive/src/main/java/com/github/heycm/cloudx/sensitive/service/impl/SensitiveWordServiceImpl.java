package com.github.heycm.cloudx.sensitive.service.impl;

import com.github.heycm.cloudx.sensitive.service.SensitiveWordRepository;
import com.github.heycm.cloudx.sensitive.service.SensitiveWordService;
import com.github.heycm.cloudx.sensitive.service.filter.SensitiveWordFilter;
import com.github.heycm.cloudx.sensitive.service.filter.SensitiveWordFilterRefresher;
import java.util.Set;

/**
 * 敏感词检测服务实现
 * @author heycm
 * @version 1.0
 * @since 2025/11/22 22:28
 */
public class SensitiveWordServiceImpl implements SensitiveWordService {

    private final SensitiveWordFilter filter;
    private final SensitiveWordFilterRefresher refresher;
    private final SensitiveWordRepository repository;

    public SensitiveWordServiceImpl(SensitiveWordFilter filter, SensitiveWordFilterRefresher refresher,
            SensitiveWordRepository repository) {
        this.filter = filter;
        this.refresher = refresher;
        this.repository = repository;
    }

    @Override
    public boolean anyMatch(String text) {
        return filter.contains(text);
    }

    @Override
    public String findAny(String text) {
        return filter.findAny(text);
    }

    @Override
    public Set<String> findAll(String text) {
        return filter.findAll(text);
    }

    @Override
    public String replace(String text) {
        return filter.replace(text);
    }

    @Override
    public String replace(String text, String replacement) {
        return filter.replace(text, replacement);
    }

    @Override
    public void addWord(String word) {
        filter.addWord(word);
        repository.addWord(word);
    }

    @Override
    public void addWords(Set<String> words) {
        filter.addWords(words);
        repository.addWords(words);
    }

    @Override
    public void removeWord(String word) {
        repository.removeWord(word);
    }

    @Override
    public void removeWords(Set<String> words) {
        repository.removeWords(words);
    }

    @Override
    public void refreshFilter() {
        refresher.refresh();
    }
}
