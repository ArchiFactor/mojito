package com.box.l10n.mojito.entity;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssetServiceImpl implements AssetService {
    @Autowired
    private EntityManager entityManager;

    @Override
    public Asset getAssetById(Long id) {
        return entityManager.find(Asset.class, id);
    }

}
