package ru.ifmo.gerasimov.ria;

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
public class RIAService extends AbstractStatsService implements StatsService {
    private static final String NAME = "RIA";
    private static final  String RSS_FEED_URL = "https://ria.ru/export/rss2/archive/index.xml";

    @Override
    public String getNewsTitle() {
        return NAME;
    }

    @Override
    public URL getRSSFeedURL() throws MalformedURLException {
        return new URL(RSS_FEED_URL);
    }
}
