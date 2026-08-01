package com.box.l10n.mojito.entity;

import jakarta.persistence.EntityManager;
import java.util.List;
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

    @Override
    public List<Long> getPullRunAssetIdsByPullRunId(Long pullRunId) {
        if (pullRunId == null) {
            return List.of();
        }
        return entityManager.createQuery("SELECT e.id FROM PullRunAsset e WHERE e.pullRun.id = :pullRunId", Long.class)
                .setParameter("pullRunId", pullRunId)
                .getResultList();
    }

}
