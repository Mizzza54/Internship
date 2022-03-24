package ru.ifmo.gerasimov.core;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Michael Gerasimov
 */
public class RSSParser {

    public static final RSSParser INSTANCE = new RSSParser();

    private RSSParser() {}

    public List<String> getAllWords(URL url) throws IOException, WrappedFeedException {
        try {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));
            List<String> titles = feed.getEntries().stream().map(SyndEntry::getTitle).collect(Collectors.toList());
            return titles
                    .stream()
                    .flatMap(x -> Arrays.stream(x.split("[,;:.!?\\s]+")).map(String::toLowerCase))
                    .collect(Collectors.toList());
        } catch (FeedException e) {
            throw new WrappedFeedException(e);
        }
    }

    public Map<String, Integer> getWordsStatistic(URL url) throws IOException, WrappedFeedException {
        List<String> words = getAllWords(url);
        return words
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(e -> 1)));
    }

    public List<WordStatisticPair> getMostFrequentlyWords(URL url, int count) throws IOException, WrappedFeedException {
        return getWordsStatistic(url)
                .entrySet()
                .stream()
                .map(x -> new WordStatisticPair(x.getKey(), x.getValue()))
                .sorted(Comparator.reverseOrder())
                .limit(count)
                .collect(Collectors.toList());
    }
}
