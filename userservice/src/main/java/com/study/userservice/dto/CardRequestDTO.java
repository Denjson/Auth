package com.study.userservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.redis.core.RedisHash;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash("cards")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardRequestDTO implements Serializable {

  private static final long serialVersionUID = 801202695987409258L;

  private Long id;

  @NotEmpty(message = "User ID can not be a null or empty")
  private Long userId;

  @NotEmpty(message = "Card can not be a null or empty")
  @Size(min = 6, max = 16, message = "Card number should be of exact size")
  private Long number;

  @NotEmpty(message = "Holder name can not be a null or empty")
  private String holder;

  private LocalDateTime expirationDate;

  private boolean active;
}
