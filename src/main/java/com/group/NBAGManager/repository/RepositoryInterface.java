package com.group.NBAGManager.repository;

import java.util.List;

public interface RepositoryInterface<E> {
    void save(E obj);
    E findById(int id);
    List<E> findAll();
    void update(E obj);
    void deleteById(int id);
    void delete(E obj);
    void close();
}
