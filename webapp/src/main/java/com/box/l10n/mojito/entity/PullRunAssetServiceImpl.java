package com.box.l10n.mojito.entity;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PullRunAssetServiceImpl implements PullRunAssetService {
    @Autowired
    private EntityManager entityManager;

    @Override
    public PullRunAsset getPullRunAssetById(Long id) {
        return entityManager.find(PullRunAsset.class, id);
    }

}
