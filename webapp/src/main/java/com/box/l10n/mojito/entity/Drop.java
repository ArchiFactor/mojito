package com.box.l10n.mojito.entity;

import com.box.l10n.mojito.entity.security.user.User;
import com.box.l10n.mojito.entity.security.user.UserService;
import com.box.l10n.mojito.rest.View;
import com.box.l10n.mojito.service.drop.exporter.DropExporterType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.ZonedDateTime;
import java.util.Set;
import org.springframework.data.annotation.CreatedBy;

/**
 * Entity that contains a Drop and the related TranslationKit associated with the drop
 *
 * @author wyau
 */
@Entity
@Table(name = "`drop`")
@NamedEntityGraph(
    name = "Drop.legacy",
    attributeNodes = {
      @NamedAttributeNode(value = "repository", subgraph = "Drop.legacy.repository"),
      @NamedAttributeNode(value = "importPollableTask", subgraph = "Drop.legacy.subTask"),
      @NamedAttributeNode(value = "exportPollableTask", subgraph = "Drop.legacy.subTask"),
      @NamedAttributeNode(value = "translationKits", subgraph = "Drop.legacy.translationKits")
    },
    subgraphs = {
      @NamedSubgraph(
          name = "Drop.legacy.subTask",
          attributeNodes = {@NamedAttributeNode("subTasks")}),
      @NamedSubgraph(
          name = "Drop.legacy.repository",
          attributeNodes = {@NamedAttributeNode("createdByUser")}),
      @NamedSubgraph(
          name = "Drop.legacy.translationKits",
          attributeNodes = {@NamedAttributeNode("locale")})
    })
public class Drop extends AuditableEntity {

  @Column(name = "drop_exporter_type")
  @Enumerated(EnumType.STRING)
  DropExporterType dropExporterType;

  @Column(name = "name")
  @JsonView(View.IdAndName.class)
  String name;

  @Column(name = "drop_exporter_config", length = Integer.MAX_VALUE)
  @JsonView(View.DropSummary.class)
  String dropExporterConfig;

  @Transient
@JsonView(View.DropSummary.class)
  PollableTask importPollableTask;

  @Column(name = "import_pollable_task_id", unique = true)
  private Long importPollableTaskId;

  @Transient
@JsonView(View.DropSummary.class)
  PollableTask exportPollableTask;

  @Column(name = "export_pollable_task_id", unique = true)
  private Long exportPollableTaskId;

  @OneToMany(mappedBy = "drop")
  @JsonView(View.DropSummary.class)
  private Set<TranslationKit> translationKits;

  @Transient
@JsonView(View.DropSummary.class)
  private Repository repository;

  @Column(name = "repository_id")
  private Long repositoryId;

  /**
   * For now we keep track of the latest import only, actually import will be additive if we track
   * the status only in one place
   */
  @Column(name = "last_imported_date")
  @JsonView(View.DropSummary.class)
  protected ZonedDateTime lastImportedDate;

  /**
   * To mark a Drop as canceled so it can be hidden in a dashboard. This shouldn't prevent to
   * perform other regular action (like import) but is just for reporting purpose.
   */
  @Column(name = "canceled")
  @JsonView(View.DropSummary.class)
  protected Boolean canceled;

  @Transient
@CreatedBy
  @JsonView(View.DropSummary.class)
  protected User createdByUser;

  @Column(name = "created_by_user_id")
  private Long createdByUserId;

  @Column(name = "exportFailed")
  @JsonView(View.DropSummary.class)
  protected Boolean exportFailed;

  @Column(name = "importFailed")
  @JsonView(View.DropSummary.class)
  protected Boolean importFailed;

  @JsonView(View.DropSummary.class)
  @Column(name = "partially_imported")
  private Boolean partiallyImported = false;

  public User getCreatedByUser() {
	if (createdByUser == null && createdByUserId != null && ServiceFactory.isContainerAvailable()) {
		UserService userService = ServiceFactory.getUserService();
		createdByUser = userService.getUserById(createdByUserId);
	}
	return createdByUser;
}

  public void setCreatedByUser(User createdByUser) {
    this.createdByUser = createdByUser;
	this.createdByUserId = createdByUser != null ? createdByUser.getId() : null;
  }

  @JsonManagedReference
  public Set<TranslationKit> getTranslationKits() {
    return translationKits;
  }

  public void setTranslationKits(Set<TranslationKit> translationKits) {
    this.translationKits = translationKits;
  }

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

  public DropExporterType getDropExporterType() {
    return dropExporterType;
  }

  public void setDropExporterType(DropExporterType dropExporterType) {
    this.dropExporterType = dropExporterType;
  }

  public String getDropExporterConfig() {
    return dropExporterConfig;
  }

  public void setDropExporterConfig(String dropExporterConfig) {
    this.dropExporterConfig = dropExporterConfig;
  }

  public ZonedDateTime getLastImportedDate() {
    return lastImportedDate;
  }

  public void setLastImportedDate(ZonedDateTime lastImportedDate) {
    this.lastImportedDate = lastImportedDate;
  }

  public Boolean getCanceled() {
    return canceled;
  }

  public void setCanceled(Boolean canceled) {
    this.canceled = canceled;
  }

  public PollableTask getImportPollableTask() {
	if (importPollableTask == null && importPollableTaskId != null && ServiceFactory.isContainerAvailable()) {
		PollableTaskService pollableTaskService = ServiceFactory.getPollableTaskService();
		importPollableTask = pollableTaskService.getPollableTaskById(importPollableTaskId);
	}
	return importPollableTask;
}

  public void setImportPollableTask(PollableTask importPollableTask) {
    this.importPollableTask = importPollableTask;
	this.importPollableTaskId = importPollableTask != null ? importPollableTask.getId() : null;
  }

  public PollableTask getExportPollableTask() {
	if (exportPollableTask == null && exportPollableTaskId != null && ServiceFactory.isContainerAvailable()) {
		PollableTaskService pollableTaskService = ServiceFactory.getPollableTaskService();
		exportPollableTask = pollableTaskService.getPollableTaskById(exportPollableTaskId);
	}
	return exportPollableTask;
}

  public void setExportPollableTask(PollableTask exportPollableTask) {
    this.exportPollableTask = exportPollableTask;
	this.exportPollableTaskId = exportPollableTask != null ? exportPollableTask.getId() : null;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getExportFailed() {
    return exportFailed;
  }

  public void setExportFailed(Boolean exportFailed) {
    this.exportFailed = exportFailed;
  }

  public Boolean getImportFailed() {
    return importFailed;
  }

  public void setImportFailed(Boolean importFailed) {
    this.importFailed = importFailed;
  }

  public Boolean getPartiallyImported() {
    return partiallyImported;
  }

  public void setPartiallyImported(Boolean partiallyImported) {
    this.partiallyImported = partiallyImported;
  }

  @PrePersist
  @PreUpdate
  private void syncUnmappedForeignKeys() {
	if (this.importPollableTask != null) {
		this.importPollableTaskId = this.importPollableTask.getId();
	}
	if (this.exportPollableTask != null) {
		this.exportPollableTaskId = this.exportPollableTask.getId();
	}
	if (this.repository != null) {
		this.repositoryId = this.repository.getId();
	}
	if (this.createdByUser != null) {
		this.createdByUserId = this.createdByUser.getId();
	}
  }
}
