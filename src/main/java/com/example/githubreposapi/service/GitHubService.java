package com.example.githubreposapi.service;

import com.example.githubreposapi.client.GitHubClient;
import com.example.githubreposapi.model.BranchResponse;
import com.example.githubreposapi.model.RepositoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubService {
    private final GitHubClient gitHubClient;

    public GitHubService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public List<RepositoryResponse> getNonForkedRepos(String username) throws UserNotFoundException {
        var repos = gitHubClient.fetchUserRepos(username);

        return repos.stream()
                .filter(repo -> !repo.fork())
                .map(repo -> {
                    var branches = gitHubClient.fetchBranches(repo.owner().login(), repo.name()).stream()
                            .map(branch -> new BranchResponse(branch.name(), branch.commit().sha()))
                            .collect(Collectors.toList());
                    return new RepositoryResponse(repo.name(), repo.owner().login(), branches);
                })
                .collect(Collectors.toList());
    }

    public static class UserNotFoundException extends Exception {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
