package com.box.l10n.mojito.entity;

import com.box.l10n.mojito.rest.View;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
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
import java.util.Set;
import org.hibernate.annotations.BatchSize;

/**
 * Represents a single instance/run of the pull command against a repository.
 *
 * @author garion
 */
@Entity
@Table(
    name = "pull_run",
    indexes = {@Index(name = "UK__PULL_RUN__NAME", columnList = "name", unique = true)})
@BatchSize(size = 1000)
public class PullRun extends SettableAuditableEntity {
  @Transient
private Repository repository;

  @Column(name = "repository_id")
  private Long repositoryId;

  /** A unique identifier that is provided for the pull run, generally a UUID. */
  @JsonView(View.CommitDetailed.class)
  @Column(name = "name")
  private String name;

  @OneToMany(mappedBy = "pullRun")
  @JsonManagedReference
  private Set<PullRunAsset> pullRunAssets;

  public Repository getRepository() {
	if (repository == null && repositoryId != null && ServiceFactory.isContainerAvailable()) {
		RepositoryService repositoryService = ServiceFactory.getRepositoryService();
		repository = repositoryService.getRepositoryById(repositoryId);
	}
	return repository;
}

  public void setRepository(Repository repository) {
    this.repository = repository;
	this.repositoryId = repository != null ? repository.getId() : null;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<PullRunAsset> getPullRunAssets() {
    return pullRunAssets;
  }

  public void setPullRunAssets(Set<PullRunAsset> pullRunAssets) {
    this.pullRunAssets = pullRunAssets;
  }

  @PrePersist
  @PreUpdate
  private void syncUnmappedForeignKeys() {
	if (this.repository != null) {
		this.repositoryId = this.repository.getId();
	}
  }
}
