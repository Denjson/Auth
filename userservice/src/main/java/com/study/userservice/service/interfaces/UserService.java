package com.study.userservice.service.interfaces;

import java.util.List;
import java.util.Set;

import com.study.userservice.auditing.UserHistory;
import com.study.userservice.dto.UserRequestDTO;
import com.study.userservice.dto.UserResponseDTO;

public interface UserService {

  public UserResponseDTO saveOne(UserRequestDTO u);

  public List<UserResponseDTO> saveMany(List<UserRequestDTO> u);

  public UserResponseDTO getById(Long id);

  public List<UserResponseDTO> getByIds(Set<Long> ids);

  public UserResponseDTO getByEmail(String email);

  public UserResponseDTO updateUser(Long id, UserRequestDTO u);

  public UserResponseDTO deleteById(Long id);

  public List<UserResponseDTO> getAllUsers();

  public UserResponseDTO getUserLast();

  public UserResponseDTO delUserLast();

  public List<UserResponseDTO> addTestUser();

  public UserResponseDTO getRandomUser();

  public List<UserResponseDTO> getRangeIds(Integer n);

  public List<UserResponseDTO> findByJPQL(String lastname);

  public List<UserHistory> getUserLog();
}
