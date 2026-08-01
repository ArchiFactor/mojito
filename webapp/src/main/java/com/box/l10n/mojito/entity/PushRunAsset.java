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
 * Maps a {@link PushRun} to a set of {@link Asset} entities.
 *
 * @author garion
 */
@Entity
@Table(
    name = "push_run_asset",
    indexes = {
      @Index(
          name = "UK__PUSH_RUN_ASSET__PUSH_RUN_ID__ASSET_ID",
          columnList = "push_run_id, asset_id",
          unique = true)
    })
@BatchSize(size = 1000)
public class PushRunAsset extends SettableAuditableEntity {
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference
  @JoinColumn(
      name = "push_run_id",
      foreignKey = @ForeignKey(name = "FK__PUSH_RUN_ASSET__PUSH_RUN_ID"))
  private PushRun pushRun;

  @Transient
private Asset asset;

  @Column(name = "asset_id")
  private Long assetId;

  @JsonManagedReference
  @OneToMany(mappedBy = "pushRunAsset")
  private Set<PushRunAssetTmTextUnit> pushRunAssetTmTextUnits = new HashSet<>();

  public PushRun getPushRun() {
    return pushRun;
  }

  public void setPushRun(PushRun pushRun) {
    this.pushRun = pushRun;
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

  public Set<PushRunAssetTmTextUnit> getPushRunAssetTmTextUnits() {
    return pushRunAssetTmTextUnits;
  }

  public void setPushRunAssetTmTextUnits(Set<PushRunAssetTmTextUnit> pushRunAssetTmTextUnits) {
    this.pushRunAssetTmTextUnits = pushRunAssetTmTextUnits;
  }

  @PrePersist
  @PreUpdate
  private void syncUnmappedForeignKeys() {
	if (this.asset != null) {
		this.assetId = this.asset.getId();
	}
  }
}
