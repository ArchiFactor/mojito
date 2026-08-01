package com.box.l10n.mojito.entity;

import java.util.List;

public interface TMTextUnitService {
    TMTextUnit getTMTextUnitById(Long id);

    List<TMTextUnit> getTMTextUnitsByIds(List<Long> ids);
}
