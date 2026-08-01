package com.box.l10n.mojito.entity;

import jakarta.persistence.EntityManager;
import java.util.List;
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

    @Override
    public List<TMTextUnit> getTMTextUnitsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return entityManager.createQuery("SELECT e FROM TMTextUnit e WHERE e.id IN :ids", TMTextUnit.class)
                .setParameter("ids", ids)
                .getResultList();
    }

}
