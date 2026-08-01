package com.box.l10n.mojito.entity;

import java.util.List;

public interface TranslationKitService {
    TranslationKit getTranslationKitById(Long id);

    List<Long> getTranslationKitIdsByLocaleId(Long localeId);
}
