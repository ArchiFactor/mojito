package com.box.l10n.mojito.entity;

import java.util.List;

public interface PullRunTextUnitVariantService {
    List<PullRunTextUnitVariant> getPullRunTextUnitVariantsByPullRunAssetId(Long pullRunAssetId);
}
