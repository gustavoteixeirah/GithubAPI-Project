package dev.gustavoteixeira.githubstats.api.util;

import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static String getTimeDifference(long start, long end) {
        long totalTimeInMilli = end - start;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTimeInMilli);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTimeInMilli);
        return String.format("%d minutes %d seconds %d milliseconds",
                minutes,
                minutes,
                totalTimeInMilli - (seconds * 1000 + minutes * 60000));
    }

}
