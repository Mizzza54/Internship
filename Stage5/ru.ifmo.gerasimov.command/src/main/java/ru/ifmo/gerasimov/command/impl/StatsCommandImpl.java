package ru.ifmo.gerasimov.command.impl;

import org.osgi.service.component.annotations.*;
import ru.ifmo.gerasimov.command.StatsCommand;
import ru.ifmo.gerasimov.core.StatsService;
import ru.ifmo.gerasimov.core.WordStatisticPair;
import ru.ifmo.gerasimov.core.WrappedFeedException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Michael Gerasimov
 */
@Component(
        service = StatsCommand.class,
        immediate = true,
        property = {
                "osgi.command.scope=news",
                "osgi.command.function=stats"
        }
)
public class StatsCommandImpl implements StatsCommand {
    private final static String USAGE = """
            Usage:
                stats
                stats arg [n]
            """;

    private final Map<String, StatsService> statsServices = new HashMap<>();

    @Reference(
            service = StatsService.class,
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            bind = "bindService",
            unbind = "unbindService"
    )
    public void bindService(StatsService statsService) {
        statsServices.put(statsService.getNewsTitle(), statsService);
    }

    public void unbindService(StatsService statsService) {
        statsServices.remove(statsService.getNewsTitle());
    }

    public String stats(String[] args) {
        if (args == null) {
            return USAGE;
        }

        StringJoiner stringJoiner = new StringJoiner("\n");
        try {
            switch (args.length) {
                case 0:
                    stringJoiner.add("The following news are currently available for selection");
                    Integer lastIndex = statsServices.keySet().stream()
                            .reduce(1,
                                    (idx, x) ->
                                    {
                                        stringJoiner.add(String.format("%d) %s", idx, x));
                                        return ++idx;
                                    },
                                    (idx, x) -> ++idx);
                    stringJoiner.add(String.format("%d) %s", lastIndex, "all"));
                    return stringJoiner.toString();
                case 1:
                    if (statsServices.containsKey(args[0])) {
                        List<String> words = statsServices
                                .get(args[0])
                                .getMostFrequentlyWords(10)
                                .stream()
                                .map(WordStatisticPair::toString)
                                .collect(Collectors.toList());
                        words.forEach(stringJoiner::add);
                        return stringJoiner.toString();
                    } else {
                        if (args[0].equals("all")) {
                            return getGeneralStats(10);
                        } else {
                            return "News not found!";
                        }
                    }
                case 2:
                    try {
                        Integer count = Integer.parseInt(args[1]);
                        if (statsServices.containsKey(args[0])) {
                                List<String> words = statsServices
                                        .get(args[0])
                                        .getMostFrequentlyWords(count)
                                        .stream()
                                        .map(WordStatisticPair::toString)
                                        .collect(Collectors.toList());
                                words.forEach(stringJoiner::add);
                                return stringJoiner.toString();
                        } else {
                            if (args[0].equals("all")) {
                                return getGeneralStats(count);
                            } else {
                                return "News not found!";
                            }
                        }
                    } catch (NumberFormatException e) {
                        return "Invalid argument: second argument must be number!\n" + USAGE;
                    }
                default:
                    return USAGE;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Was thrown IOException!\nThere is a problem reading the stream of the URL";
        } catch (WrappedFeedException e) {
            e.printStackTrace();
            return "Was thrown FeedException!\nFeed could not be parsed";
        }
    }

    private String getGeneralStats(Integer count) throws IOException, WrappedFeedException {
        StringJoiner stringJoiner = new StringJoiner("\n");
        List<WordStatisticPair> pairs = new ArrayList<>();
        for (StatsService elem : statsServices.values()) {
            pairs.addAll(elem.getMostFrequentlyWords(count));
        }
        pairs
                .stream()
                .sorted(Comparator.reverseOrder())
                .map(Objects::toString)
                .limit(count)
                .collect(Collectors.toList())
                .forEach(stringJoiner::add);
        return stringJoiner.toString();
    }
}
