package com.box.l10n.mojito.entity;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PullRunTextUnitVariantServiceImpl implements PullRunTextUnitVariantService {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<PullRunTextUnitVariant> getPullRunTextUnitVariantsByPullRunAssetId(Long pullRunAssetId) {
        if (pullRunAssetId == null) {
            return List.of();
        }
        return entityManager.createQuery("SELECT e FROM PullRunTextUnitVariant e WHERE e.pullRunAssetId = :pullRunAssetId", PullRunTextUnitVariant.class)
                .setParameter("pullRunAssetId", pullRunAssetId)
                .getResultList();
    }

}
