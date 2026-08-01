package com.box.l10n.mojito.entity;

import com.box.l10n.mojito.rest.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.ZonedDateTime;
import java.util.Set;
import org.hibernate.annotations.BatchSize;

/**
 * Represents a single instance/run of the push command against a repository.
 *
 * @author garion
 */
@Entity
@Table(
    name = "push_run",
    indexes = {@Index(name = "UK__PUSH_RUN__NAME", columnList = "name", unique = true)})
@BatchSize(size = 1000)
@NamedEntityGraph(
    name = "PushRun.legacy",
    attributeNodes = {
      @NamedAttributeNode(value = "pushRunAssets", subgraph = "PushRun.legacy.pushRunAssets"),
    },
    subgraphs = {
      @NamedSubgraph(
          name = "PushRun.legacy.pushRunAssets",
          attributeNodes = {@NamedAttributeNode(value = "pushRunAssetTmTextUnits")})
    })
public class PushRun extends SettableAuditableEntity {

  @Transient
@JsonIgnore
  private Repository repository;

  @Column(name = "repository_id")
  private Long repositoryId;

  /** Avoid serialization of the full Repository object, include only the IDs. */
  @JsonView(View.PushRun.class)
  @JsonProperty("repository_id")
  private Long getRepositoryId() {
    return this.getRepository().getId();
  }

  /** A unique identifier that is provided for the push run, generally a UUID. */
  @JsonView({View.CommitDetailed.class, View.PushRun.class})
  @Column(name = "name")
  private String name;

  @JsonView(View.PushRun.class)
  @OneToMany(mappedBy = "pushRun")
  @JsonManagedReference
  private Set<PushRunAsset> pushRunAssets;

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

  public ZonedDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(ZonedDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public Set<PushRunAsset> getPushRunAssets() {
    return pushRunAssets;
  }

  public void setPushRunAssets(Set<PushRunAsset> pushRunAssetSet) {
    this.pushRunAssets = pushRunAssetSet;
  }

  @PrePersist
  @PreUpdate
  private void syncUnmappedForeignKeys() {
	if (this.repository != null) {
		this.repositoryId = this.repository.getId();
	}
  }
}
