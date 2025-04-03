package com.example.githubreposapi.controller;

import com.example.githubreposapi.model.RepositoryResponse;
import com.example.githubreposapi.service.GitHubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/repos")
public class GitHubController {
    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getRepos(@PathVariable String username) {
        try {
            List<RepositoryResponse> repos = gitHubService.getNonForkedRepos(username);
            return ResponseEntity.ok(repos);
        } catch (GitHubService.UserNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(404, e.getMessage()));
        }
    }

    private record ErrorResponse(int status, String message) {}
}
