package dev.gustavoteixeira.githubstats.api.utils;

import dev.gustavoteixeira.githubstats.api.util.TimeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TimeUtilsTest {

    @Test
    public void getTimeDifferenceShouldReturnTheTimeDifference() {
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        long end = System.currentTimeMillis();

        int minimumTime = 5;
        String result = TimeUtils.getTimeDifference(start, end);

        int parsedResult = Integer.parseInt(String.valueOf(result.charAt(20)));
        assertThat(parsedResult).isGreaterThanOrEqualTo(minimumTime);
    }


}
