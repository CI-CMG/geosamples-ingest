package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagingAndSortingParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalResource;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class ApprovalResourceServiceBase<I, E extends ApprovalResource<I>, S extends PagingAndSortingParameters, V, R extends JpaSpecificationExecutor<E> & JpaRepository<E, I>> extends SearchServiceBase<E, I, S, V, R> {

  protected abstract void validateParentResourceApproval(E entity);

  protected void updateEntityApproval(E entity, ApprovalView view) {
    if (entity.getApproval() != null) {
      entity.getApproval().setApprovalState(view.getApprovalState());
      entity.getApproval().setComment(view.getComment());
      if (view.getApprovalState().equals(ApprovalState.REJECTED)) {
        entity.setPublish(false);
      }
    } else {
      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(view.getApprovalState());
      approval.setComment(view.getComment());
      entity.setApproval(approval);
      entity.setPublish(false);
    }
  }

  public V updateApproval(ApprovalView view, I id) {
    E entity = getRequiredEntity(id);
    if (entity.getApproval() == null) {
      throw new ApiException(
          HttpStatus.BAD_REQUEST,
          ApiError.builder().error("Cannot update non-existent approval").build()
      );
    }
    if (view.getApprovalState().equals(ApprovalState.PENDING)) {
      if (!entity.getApproval().getApprovalState().equals(ApprovalState.REJECTED) && !entity.getApproval().getApprovalState().equals(ApprovalState.PENDING)) {
        throw new ApiException(
            HttpStatus.BAD_REQUEST,
            ApiError.builder().error("Cannot update approved item").build()
        );
      }
    }
    validateParentResourceApproval(entity);
    updateEntityApproval(entity, view);
    return toView(getRepository().save(entity));
  }

  public V createWithNewApproval(V view) {
    E entity = super.toEntity(view, null);
    ApprovalView approvalView = new ApprovalView();
    approvalView.setApprovalState(ApprovalState.PENDING);
    updateEntityApproval(entity, approvalView);
    return toView(getRepository().save(entity));
  }
}
