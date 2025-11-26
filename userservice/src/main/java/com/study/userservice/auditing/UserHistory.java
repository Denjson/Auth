package com.study.userservice.auditing;

import java.util.Date;

// import org.hibernate.annotations.Cascade;
// import org.hibernate.annotations.CascadeType;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.study.userservice.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

/**
 * @author Someone
 */
@Entity
@Table(name = "userhistory", schema = "den_schema")
@EntityListeners(AuditingEntityListener.class)
public class UserHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  //  @ManyToOne
  // @Cascade(CascadeType.REFRESH)	// not working with DELETE operations
  // @Cascade(CascadeType.REMOVE)	// not working with DELETE operations
  // @Cascade(CascadeType.ALL) // not working POST operations, but DELETE working
  // @Cascade(CascadeType.MERGE)
  //  @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_id"))
  //  private User user;

  @Transient private User user;

  @Column(name = "user_id")
  private long user_id;

  private String user_content;

  @CreatedBy private String modified_by;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date modified_date;

  @Enumerated(EnumType.STRING)
  private Action action;

  public UserHistory() {}

  public UserHistory(User user, Action action) {
    if (user.getId() != null) {
      user_id = user.getId();
    }
    this.user = user;
    this.user_content = user.toString();
    this.action = action;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public long getUser_id() {
    return user_id;
  }

  public void setUser_id(long user_id) {
    this.user_id = user_id;
  }

  public String getUserContent() {
    return user_content;
  }

  public void setUserContent(String userContent) {
    this.user_content = userContent;
  }

  public String getModifiedBy() {
    return modified_by;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modified_by = modifiedBy;
  }

  public Date getModifiedDate() {
    return modified_date;
  }

  public void setModifiedDate(Date modifiedDate) {
    this.modified_date = modifiedDate;
  }

  public Action getAction() {
    return action;
  }

  public void setAction(Action action) {
    this.action = action;
  }
}
