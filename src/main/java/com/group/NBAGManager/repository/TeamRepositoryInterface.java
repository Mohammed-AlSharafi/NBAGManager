package com.group.NBAGManager.repository;

import com.group.NBAGManager.model.Player;

import java.util.List;

public interface TeamRepositoryInterface extends RepositoryInterface<Player>{
    public List<Player> findIsInjured(boolean injured);
    public List<Player> findIsContractExtensionQueued(boolean isContractExtensionQueued);
}
