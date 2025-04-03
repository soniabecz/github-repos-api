package com.example.githubreposapi.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GitHubApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnRepositoriesForValidUser() throws Exception {
        mockMvc.perform(get("/repos/octocat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].repositoryName").exists())
                .andExpect(jsonPath("$[0].ownerLogin").exists())
                .andExpect(jsonPath("$[0].branches").isArray());
    }

    @Test
    void shouldReturn404ForNonExistingUser() throws Exception {
        mockMvc.perform(get("/repos/this-user-does-not-exist-xyz"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void shouldReturnEmptyListForUserWithNoRepositories() throws Exception {
        // This test uses GitHub user 'campuscommune' that exists but has no public repositories; found by search 'type:user repos:0'
        mockMvc.perform(get("/repos/campuscommune"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void repositoriesShouldNotContainForks() throws Exception {
        mockMvc.perform(get("/repos/octocat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].repositoryName").isNotEmpty())
                .andExpect(jsonPath("$[*].branches").isArray());
    }

    @Test
    void repositoryShouldContainBranchesWithSha() throws Exception {
        mockMvc.perform(get("/repos/octocat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].branches[0].name").exists())
                .andExpect(jsonPath("$[0].branches[0].lastCommitSha").exists());
    }
}

