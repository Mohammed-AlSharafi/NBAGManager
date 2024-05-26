package com.group.NBAGManager.model;

import com.group.NBAGManager.repository.PlayerRepository;
import com.group.NBAGManager.repository.TeamRepository;
import com.group.NBAGManager.repository.UserRepository;

public class RepositoryHandler {
    private static RepositoryHandler instance;
    private UserRepository userRepository;
    private PlayerRepository playerRepository;
    private TeamRepository teamRepository;

    private RepositoryHandler() {
        userRepository = new UserRepository();
        playerRepository = new PlayerRepository();
        teamRepository = new TeamRepository();
    }

    public static RepositoryHandler getInstance() {
        if (instance == null) {
            instance = new RepositoryHandler();
        }
        return instance;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public PlayerRepository getPlayerRepository() {
        return playerRepository;
    }

    public TeamRepository getTeamRepository() {
        return teamRepository;
    }

    public void closeAll() {
        userRepository.close();
        playerRepository.close();
        teamRepository.close();
        System.out.println("closed all repositories");
    }
}
