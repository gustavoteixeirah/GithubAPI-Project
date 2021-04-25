package dev.gustavoteixeira.githubstats.api.utils;

import dev.gustavoteixeira.githubstats.api.util.StringTransformationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StringTransformationUtilsTest {

    @Test
    public void githubURLShoulReturnRepoOwnerAndRepoName() {
        String testURL = "https://github.com/iwhrim/InMoov-URI";
        String result = StringTransformationUtils.getRootRepository(testURL);
        assertThat(result).isEqualTo("iwhrim/InMoov-URI");
    }

    @Test
    public void rawGithubURLShoulReturnRepoOwnerAndRepoName() {
        String testURL = "https://raw.githubusercontent.com/iwhrim/InMoov-URI/master/main.py";
        String result = StringTransformationUtils.getRootRepository(testURL);
        assertThat(result).isEqualTo("iwhrim/InMoov-URI");
    }



}
