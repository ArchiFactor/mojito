package com.box.l10n.mojito.entity;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryServiceImpl implements RepositoryService {
    @Autowired
    private EntityManager entityManager;

    @Override
    public Repository getRepositoryById(Long id) {
        return entityManager.find(Repository.class, id);
    }

}
