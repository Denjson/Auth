package com.study.userservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash("users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO implements Serializable {

  private static final long serialVersionUID = 4648357076042140597L;

  private Long id;

  private String name;

  private String surname;

  private LocalDateTime birthDate;

  private String email;

  private boolean active;
}
