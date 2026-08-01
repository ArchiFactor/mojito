package com.box.l10n.mojito.entity;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocaleServiceImpl implements LocaleService {
    @Autowired
    private EntityManager entityManager;

    @Override
    public Locale getLocaleById(Long id) {
        return entityManager.find(Locale.class, id);
    }

}
