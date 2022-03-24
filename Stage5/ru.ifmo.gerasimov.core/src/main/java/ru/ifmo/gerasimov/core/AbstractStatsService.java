package ru.ifmo.gerasimov.core;

import java.io.IOException;
import java.util.List;

/**
 * @author Michael Gerasimov
 */
public abstract class AbstractStatsService implements StatsService {
    @Override
    public List<String> getAllWords() throws IOException, WrappedFeedException {
        return RSSParser.INSTANCE.getAllWords(getRSSFeedURL());
    }

    @Override
    public List<WordStatisticPair> getMostFrequentlyWords(Integer count) throws IOException, WrappedFeedException {
        return RSSParser.INSTANCE.getMostFrequentlyWords(getRSSFeedURL(), count);
    }
}
