package dev.gustavoteixeira.githubstats.api.util;

import dev.gustavoteixeira.githubstats.api.exception.InvalidGitHubRepositoryURL;

public class StringTransformationUtils {

    public final static String rawGithubURL = "https://raw.githubusercontent.com/";
    public final static String normalGithubURL = "https://github.com/";

    public static String getRootRepository(String fullRepositoryURL) {
        String s;
        try {
            if (fullRepositoryURL.contains(rawGithubURL)) {
                s = fullRepositoryURL.replace(rawGithubURL, "");
            } else {
                s = fullRepositoryURL.replace(normalGithubURL, "");
            }
            return s.split("/")[0] + "/" + s.split("/")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidGitHubRepositoryURL();
        }
    }

}
