package com.study.userservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash("cards")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardResponseDTO implements Serializable {

  private static final long serialVersionUID = -3654968567818504772L;

  private Long id;

  private Long user_id;

  private Integer number;

  private String holder;

  private LocalDateTime expiration_date;

  private boolean active;
}
