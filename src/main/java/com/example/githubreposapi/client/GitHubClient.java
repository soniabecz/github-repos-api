package com.example.githubreposapi.client;

import com.example.githubreposapi.service.GitHubService.UserNotFoundException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class GitHubClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GITHUB_API_URL = "https://api.github.com";

    public List<GitHubRepo> fetchUserRepos(String username) throws UserNotFoundException {
        try {
            var url = GITHUB_API_URL + "/users/" + username + "/repos";
            var repos = restTemplate.getForObject(url, GitHubRepo[].class);
            return Arrays.asList(repos);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException("User not found");
            }
            throw e;
        }
    }

    public List<GitHubBranch> fetchBranches(String owner, String repo) {
        var url = GITHUB_API_URL + "/repos/" + owner + "/" + repo + "/branches";
        var branches = restTemplate.getForObject(url, GitHubBranch[].class);
        return Arrays.asList(branches);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GitHubRepo(String name, boolean fork, Owner owner) {
        public record Owner(String login) {}
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GitHubBranch(String name, Commit commit) {
        public record Commit(@JsonProperty("sha") String sha) {}
    }
}
