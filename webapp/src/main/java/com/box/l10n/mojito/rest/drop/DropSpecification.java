package com.box.l10n.mojito.rest.drop;

import com.box.l10n.mojito.entity.Drop;
import com.box.l10n.mojito.entity.Drop_;
import com.box.l10n.mojito.entity.ServiceFactory;
import com.box.l10n.mojito.specification.SingleParamSpecification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author jaurambault
 */
public class DropSpecification {

  /**
   * A {@link Specification} to filter Drops that have been imported or not.
   *
   * @param imported {@code true} to get Drops that have been imported, {@code false} to get Drops
   *     that have not yet been imported
   * @return {@link Specification}
   */
  public static SingleParamSpecification<Drop> isImported(final Boolean imported) {
    return new SingleParamSpecification<Drop>(imported) {
      @Override
      public Predicate toPredicate(
          Root<Drop> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        // The finished state of the import pollable task has to be resolved through the
        // PollableTask service since Drop only keeps the foreign key to it.
        List<Long> finishedPollableTaskIds =
            ServiceFactory.getPollableTaskService().getFinishedPollableTaskIds();

        Predicate importPollableTaskFinished =
            finishedPollableTaskIds.isEmpty()
                ? builder.disjunction()
                : root.get(Drop_.importPollableTaskId).in(finishedPollableTaskIds);

        Predicate predicate;

        if (imported) {
          predicate =
              builder.and(
                  builder.isFalse(root.get(Drop_.partiallyImported)),
                  builder.isNotNull(root.get(Drop_.lastImportedDate)),
                  builder.or(
                      builder.isNull(root.get(Drop_.importFailed)),
                      builder.isFalse(root.get(Drop_.importFailed))),
                  builder.isNotNull(root.get(Drop_.importPollableTaskId)),
                  importPollableTaskFinished);
        } else {
          predicate =
              builder.or(
                  builder.isTrue(root.get(Drop_.partiallyImported)),
                  builder.isNull(root.get(Drop_.lastImportedDate)),
                  builder.isTrue(root.get(Drop_.importFailed)),
                  builder.isNull(root.get(Drop_.importPollableTaskId)),
                  builder.not(importPollableTaskFinished));
        }

        return predicate;
      }
    };
  }

  /**
   * A {@link Specification} to filter Drops that have been canceled or not.
   *
   * @param canceled {@code true} to get Drops that have been canceled, {@code false} to get Drops
   *     that have not been canceled
   * @return {@link Specification}
   */
  public static SingleParamSpecification<Drop> isCanceled(final Boolean canceled) {
    return new SingleParamSpecification<Drop>(canceled) {
      @Override
      public Predicate toPredicate(
          Root<Drop> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (canceled) {
          return builder.isTrue(root.get(Drop_.canceled));
        } else {
          return builder.or(
              builder.isNull(root.get(Drop_.canceled)), builder.isFalse(root.get(Drop_.canceled)));
        }
      }
    };
  }

  /**
   * A {@link Specification} the check if {@link Drop#repository} is equal.
   *
   * @param repositoryId value to check
   * @return {@link Specification}
   */
  public static SingleParamSpecification<Drop> repositoryIdEquals(final Long repositoryId) {
    return new SingleParamSpecification<Drop>(repositoryId) {
      public Predicate toPredicate(
          Root<Drop> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.equal(root.get(Drop_.repositoryId), repositoryId);
      }
    };
  }
}
