package com.box.l10n.mojito.entity;

import jakarta.persistence.EntityManager;
import java.util.List;
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

    @Override
    public List<Long> getFinishedPollableTaskIds() {
        return entityManager.createQuery("SELECT e.id FROM PollableTask e WHERE e.finishedDate IS NOT NULL", Long.class)
                .getResultList();
    }

}
