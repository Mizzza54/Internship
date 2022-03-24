package ru.ifmo.gerasimov.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author Michael Gerasimov
 */
public interface StatsService {

    String getNewsTitle();

    URL getRSSFeedURL() throws MalformedURLException;

    List<String> getAllWords() throws IOException, WrappedFeedException;

    List<WordStatisticPair> getMostFrequentlyWords(Integer count) throws IOException, WrappedFeedException;
}
