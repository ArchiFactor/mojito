package com.box.l10n.mojito.entity;

import jakarta.persistence.EntityManager;
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

}
