package com.study.userservice.entity;

import java.util.Date;

// import org.hibernate.annotations.Cascade;
// import org.hibernate.annotations.CascadeType;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.study.userservice.auditing.Action;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
// import jakarta.persistence.Transient;
import lombok.Data;

/**
 * @author Someone
 */
@Data
@Entity
@Table(name = "userhistory", schema = "den_schema")
@EntityListeners(AuditingEntityListener.class)
public class UserHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  // @Cascade(CascadeType.REFRESH)
  // @Cascade(CascadeType.REMOVE)
  // @Cascade(CascadeType.ALL)
  // @Cascade(CascadeType.MERGE)
  // @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_id"))
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  //  @Transient private User user;

  //  @Column(name = "user_id")
  //  private long user_id;

  private String user_content;

  @CreatedBy private String modifiedBy;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date modifiedDate;

  @Enumerated(EnumType.STRING)
  private Action action;

  public UserHistory() {}

  public UserHistory(User user, Action action) {
    //    if (user.getId() != null) {
    //      user_id = user.getId();
    //    }
    this.user = user;
    this.user_content = user.toString();
    this.action = action;
  }
}
