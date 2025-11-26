package com.study.userservice.entity;

import java.time.LocalDateTime;

import com.study.userservice.auditing.UserEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(UserEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", schema = "den_schema")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "surname")
  private String surname;

  @Column(name = "birth_date")
  private LocalDateTime birth_date;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "active")
  private boolean active;
}
