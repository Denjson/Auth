package com.study.userservice.auditing;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<U> {

  @CreatedBy protected U createdBy;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  protected Date createdDate;

  @LastModifiedBy protected U lastModifiedBy;

  @LastModifiedDate
  @Temporal(TemporalType.TIMESTAMP)
  protected Date lastModifiedDate;
}
