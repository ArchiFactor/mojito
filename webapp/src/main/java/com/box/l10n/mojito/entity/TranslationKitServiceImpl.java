package com.box.l10n.mojito.entity;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TranslationKitServiceImpl implements TranslationKitService {
    @Autowired
    private EntityManager entityManager;

    @Override
    public TranslationKit getTranslationKitById(Long id) {
        return entityManager.find(TranslationKit.class, id);
    }

    @Override
    public List<Long> getTranslationKitIdsByLocaleId(Long localeId) {
        if (localeId == null) {
            return List.of();
        }
        return entityManager.createQuery("SELECT e.id FROM TranslationKit e WHERE e.localeId = :localeId", Long.class)
                .setParameter("localeId", localeId)
                .getResultList();
    }

}
