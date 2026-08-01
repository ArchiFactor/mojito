package com.box.l10n.mojito.entity;

import java.util.List;

public interface PullRunAssetService {
    PullRunAsset getPullRunAssetById(Long id);

    List<Long> getPullRunAssetIdsByPullRunId(Long pullRunId);
}
