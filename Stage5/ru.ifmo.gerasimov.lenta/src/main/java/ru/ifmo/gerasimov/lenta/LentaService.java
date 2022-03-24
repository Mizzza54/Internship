package ru.ifmo.gerasimov.lenta;

import org.osgi.service.component.annotations.Component;
import ru.ifmo.gerasimov.core.AbstractStatsService;
import ru.ifmo.gerasimov.core.StatsService;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Michael Gerasimov
 */
@Component(
        service = StatsService.class,
        immediate = true
)
public class LentaService extends AbstractStatsService implements StatsService {

    private static final String NAME = "Lenta.ru";
    private static final String RSS_FEED_URL = "https://api.lenta.ru/rss";

    @Override
    public String getNewsTitle() {
        return NAME;
    }

    @Override
    public URL getRSSFeedURL() throws MalformedURLException {
        return new URL(RSS_FEED_URL);
    }
}
