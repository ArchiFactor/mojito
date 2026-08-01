package com.box.l10n.mojito.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.BatchSize;

/**
 * Maps a {@link PullRun} to a set of {@link Asset} entities.
 *
 * @author garion
 */
@Entity
@Table(
    name = "pull_run_asset",
    indexes = {
      @Index(
          name = "UK__PULL_RUN_ASSET__PULL_RUN_ID__ASSET_ID",
          columnList = "pull_run_id, asset_id",
          unique = true)
    })
@BatchSize(size = 1000)
public class PullRunAsset extends SettableAuditableEntity {
  @ManyToOne(fetch = FetchType.EAGER)
  @JsonBackReference
  @JoinColumn(
      name = "pull_run_id",
      foreignKey = @ForeignKey(name = "FK__PULL_RUN_ASSET__PULL_RUN_ID"))
  private PullRun pullRun;

  @Transient
@JsonBackReference
  private Asset asset;

  @Column(name = "asset_id")
  private Long assetId;

  @Transient
@JsonManagedReference
  private Set<PullRunTextUnitVariant> pullRunTextUnitVariants = new HashSet<>();

  public PullRun getPullRun() {
    return pullRun;
  }

  public void setPullRun(PullRun pullRun) {
    this.pullRun = pullRun;
  }

  public Asset getAsset() {
	if (asset == null && assetId != null && ServiceFactory.isContainerAvailable()) {
		AssetService assetService = ServiceFactory.getAssetService();
		asset = assetService.getAssetById(assetId);
	}
	return asset;
}

  public void setAsset(Asset asset) {
    this.asset = asset;
	this.assetId = asset != null ? asset.getId() : null;
  }

  public Set<PullRunTextUnitVariant> getPullRunTextUnitVariants() {
	if (pullRunTextUnitVariants.isEmpty() && getId() != null && ServiceFactory.isContainerAvailable()) {
		PullRunTextUnitVariantService pullRunTextUnitVariantService = ServiceFactory.getPullRunTextUnitVariantService();
		pullRunTextUnitVariants
				.addAll(pullRunTextUnitVariantService.getPullRunTextUnitVariantsByPullRunAssetId(getId()));
	}
	return pullRunTextUnitVariants;
}

  public void setPullRunTextUnitVariants(Set<PullRunTextUnitVariant> pullRunTextUnitVariants) {
    this.pullRunTextUnitVariants = pullRunTextUnitVariants;
  }

  @PrePersist
  @PreUpdate
  private void syncUnmappedForeignKeys() {
	if (this.asset != null) {
		this.assetId = this.asset.getId();
	}
  }
}
