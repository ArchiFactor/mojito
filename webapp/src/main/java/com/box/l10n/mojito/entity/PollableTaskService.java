package com.box.l10n.mojito.entity;

import java.util.List;

public interface PollableTaskService {
    PollableTask getPollableTaskById(Long id);

    List<Long> getFinishedPollableTaskIds();
}
