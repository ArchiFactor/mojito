package com.box.l10n.mojito.service.translationkit;

import com.box.l10n.mojito.entity.Locale;
import com.box.l10n.mojito.entity.ServiceFactory;
import com.box.l10n.mojito.entity.TMTextUnit;
import com.box.l10n.mojito.entity.TranslationKit;
import com.box.l10n.mojito.entity.TranslationKitTextUnit;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author jaurambault
 */
@RepositoryRestResource(exported = false)
public interface TranslationKitTextUnitRepository
    extends JpaRepository<TranslationKitTextUnit, Long> {

  TranslationKitTextUnit findByTranslationKitIdAndTmTextUnit(
      Long translationKitId, TMTextUnit tmTextUnit);

  /**
   * The locale of the {@link TranslationKit} has to be resolved through the TranslationKit service
   * since {@link TranslationKitTextUnit} only keeps the foreign key to the kit.
   */
  default TranslationKitTextUnit findByTranslationKitAndTmTextUnitAndTranslationKit_Locale(
      TranslationKit translationKit, TMTextUnit tmTextUnit, Locale locale) {
    List<Long> translationKitIds =
        ServiceFactory.getTranslationKitService().getTranslationKitIdsByLocaleId(locale.getId());

    if (!translationKitIds.contains(translationKit.getId())) {
      return null;
    }

    return findByTranslationKitIdAndTmTextUnit(translationKit.getId(), tmTextUnit);
  }

  int countByTranslationKitIdAndImportedTmTextUnitVariantIsNotNull(Long translationKitId);

  int countByTranslationKitIdAndSourceEqualsTargetTrue(Long translationKitId);

  @Query(
      "select count(*) from #{#entityName} tktu where tktu.translationKitId = ?1 and tktu.detectedLanguage != tktu.detectedLanguageExpected")
  int countByTranslationKitIdAndDetectedLanguageNotEqualsDetectedLanguageExpected(
      Long translationKitId);

  @EntityGraph(value = "TranslationKitTextUnit.legacy", type = EntityGraphType.FETCH)
  List<TranslationKitTextUnit> findByTranslationKitId(Long translationKitId);

  default List<TranslationKitTextUnit> findByTranslationKit(TranslationKit translationKit) {
    return findByTranslationKitId(translationKit.getId());
  }
}
