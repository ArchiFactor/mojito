package com.box.l10n.mojito.entity;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PollableTaskServiceImpl implements PollableTaskService {
    @Autowired
    private EntityManager entityManager;

    @Override
    public PollableTask getPollableTaskById(Long id) {
        return entityManager.find(PollableTask.class, id);
    }

}
