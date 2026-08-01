package com.box.l10n.mojito.service.pullrun;

import com.box.l10n.mojito.entity.PullRun;
import com.box.l10n.mojito.entity.PullRunTextUnitVariant;
import com.box.l10n.mojito.entity.ServiceFactory;
import com.box.l10n.mojito.entity.TMTextUnitVariant;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author garion
 */
@RepositoryRestResource(exported = false)
public interface PullRunTextUnitVariantRepository
    extends JpaRepository<PullRunTextUnitVariant, Long> {

  List<PullRunTextUnitVariant> findByPullRunAssetId(Long pullRunAssetId, Pageable pageable);

  List<PullRunTextUnitVariant> findByTmTextUnitVariant_TmTextUnitIdAndLocaleId(
      Long tmTextUnitId, Long localeId);

  List<PullRunTextUnitVariant> findByPullRunAssetIdAndLocaleIdAndOutputBcp47Tag(
      Long pullRunAssetId, Long localeId, String outputBcp47Tag);

  @Query(
      """
      select prtuv.tmTextUnitVariant from PullRunTextUnitVariant prtuv
      where prtuv.pullRunAssetId in :pullRunAssetIds
      """)
  List<TMTextUnitVariant> findByPullRunAssetIdIn(
      @Param("pullRunAssetIds") List<Long> pullRunAssetIds, Pageable pageable);

  /**
   * The PullRunAsset ids have to be resolved through the PullRunAsset service since {@link
   * PullRunTextUnitVariant} only keeps the foreign key to it.
   */
  default List<TMTextUnitVariant> findByPullRun(PullRun pullRun, Pageable pageable) {
    List<Long> pullRunAssetIds =
        ServiceFactory.getPullRunAssetService().getPullRunAssetIdsByPullRunId(pullRun.getId());

    if (pullRunAssetIds.isEmpty()) {
      return Collections.emptyList();
    }

    return findByPullRunAssetIdIn(pullRunAssetIds, pageable);
  }

  @Transactional
  void deleteByPullRunAssetId(Long pullRunAssetId);

  @Transactional
  @Modifying
  @Query(
      nativeQuery = true,
      value =
          """
          delete pull_run_text_unit_variant
          from pull_run_text_unit_variant
          join (select prtuv.id as id
            from pull_run pr
            join pull_run_asset pra on pra.pull_run_id = pr.id
            join pull_run_text_unit_variant prtuv on prtuv.pull_run_asset_id = pra.id
            where pr.created_date < :beforeDate
            limit :batchSize
          ) todelete on todelete.id = pull_run_text_unit_variant.id
          """)
  int deleteAllByPullRunWithCreatedDateBefore(
      @Param("beforeDate") ZonedDateTime beforeDate, @Param("batchSize") int batchSize);
}
