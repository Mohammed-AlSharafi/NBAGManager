package com.group.NBAGManager.repository;

import com.group.NBAGManager.model.User;

public interface UserRepositoryInterface extends RepositoryInterface<User> {
    User findUserByUsername(String username);
}
