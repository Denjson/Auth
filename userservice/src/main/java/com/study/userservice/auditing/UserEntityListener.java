package com.study.userservice.auditing;

import com.study.userservice.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

public class UserEntityListener {

  @PrePersist
  public void prePersist(User user) {
    perform(user, Action.INSERTED);
  }

  @PreUpdate
  public void preUpdate(User user) {
    perform(user, Action.UPDATED);
  }

  @PreRemove
  public void preRemove(User user) {
    System.out.println("Auditing DELETE transaction, User: " + user);
    perform(user, Action.DELETED);
  }

  @Transactional(TxType.MANDATORY)
  private void perform(User user, Action action) {
    EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
    entityManager.persist(new UserHistory(user, action));
  }
}
