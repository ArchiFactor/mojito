package com.box.l10n.mojito.entity;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TMTextUnitServiceImpl implements TMTextUnitService {
    @Autowired
    private EntityManager entityManager;

    @Override
    public TMTextUnit getTMTextUnitById(Long id) {
        return entityManager.find(TMTextUnit.class, id);
    }

}
