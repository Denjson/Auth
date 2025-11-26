package com.study.userservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "card_info", schema = "den_schema")
public class Card {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "user_id")
  private Long user_id;

  @Column(name = "number")
  private Integer number;

  @Column(name = "holder")
  private String holder;

  @Column(name = "expiration_date")
  private LocalDateTime expiration_date;

  @Column(name = "active")
  private boolean active;
}
