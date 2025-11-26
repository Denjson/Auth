package com.study.userservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import com.study.userservice.auditing.UserHistory;
import com.study.userservice.dto.UserRequestDTO;
import com.study.userservice.dto.UserResponseDTO;
import com.study.userservice.entity.User;
import com.study.userservice.exceptions.IdNotFoundException;
import com.study.userservice.mappers.UserMapper;
import com.study.userservice.repository.UserHistoryRepository;
import com.study.userservice.repository.UserRepository;
import com.study.userservice.service.interfaces.UserService;

public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final UserHistoryRepository userHistoryRepository;

  public UserServiceImpl(
      UserRepository userRepository,
      UserMapper userMapper,
      UserHistoryRepository userHistoryRepository) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.userHistoryRepository = userHistoryRepository;
  }

  @CacheEvict(value = "users", key = "0")
  public UserResponseDTO saveOne(UserRequestDTO userRequestDTO) {
    User user = userRepository.save(userMapper.toEntity(userRequestDTO));
    System.out.println("Saved User: " + userRequestDTO);
    return userMapper.toDTO(user);
  }

  @CacheEvict(value = "users", key = "0")
  public List<UserResponseDTO> saveMany(List<UserRequestDTO> UserRequestDTOs) {
    List<User> manyUsers = userRepository.saveAll(userMapper.manyToEntity(UserRequestDTOs));
    System.out.println("Saved Users: " + UserRequestDTOs);
    return userMapper.toDTOs(manyUsers);
  }

  @Cacheable(value = "users", key = "#id")
  public UserResponseDTO getById(Long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new IdNotFoundException("User not found with id: " + id));
    return userMapper.toDTO(user);
  }

  @Caching(
      evict = {
        @CacheEvict(value = "users", key = "#id"),
        @CacheEvict(value = "users", key = "0"),
        @CacheEvict(value = "cards", key = "0")
      })
  public UserResponseDTO deleteById(Long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new IdNotFoundException("User not found with id: " + id));
    userRepository.deleteById(id);
    return userMapper.toDTO(user);
  }

  public List<UserResponseDTO> getByIds(Set<Long> ids) {
    List<User> users = userRepository.findByIdIn(ids).get();
    if (users.isEmpty()) {
      throw new IdNotFoundException("Users not found with ids: " + ids);
    }
    System.out.println("Users found: " + users.toString());
    return userMapper.toDTOs(users);
  }

  public UserResponseDTO getByEmail(String email) {
    User user =
        userRepository
            .getByEmail(email)
            .orElseThrow(() -> new IdNotFoundException("Email not found: " + email));
    return userMapper.toDTO(user);
  }

  @CachePut(value = "users", key = "#id")
  @CacheEvict(value = "users", key = "0")
  public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new IdNotFoundException("User not found with id: " + id));
    userRequestDTO.setId(id);
    user = userMapper.toEntity(userRequestDTO);
    userRepository.save(user);
    System.out.println("User updated: " + user.toString());
    return userMapper.toDTO(user);
  }

  @Cacheable(value = "users", key = "0")
  public List<UserResponseDTO> getAllUsers() {
    List<User> users = userRepository.findAll();
    if (users.isEmpty()) {
      throw new IdNotFoundException("List of users is empty");
    }
    System.out.println("All users from UserService: " + users.toString());
    List<UserResponseDTO> userResponseDTOs = userMapper.toDTOs(users);
    return userResponseDTOs;
  }

  public UserResponseDTO getUserLast() {
    User user =
        userRepository
            .findTopByOrderByIdDesc()
            .orElseThrow(() -> new IdNotFoundException("No any users available"));
    System.out.println("Last id is " + user.getId() + user);
    UserResponseDTO userResponseDTO = userMapper.toDTO(user);
    return userResponseDTO;
  }

  @CacheEvict(value = "users", key = "0")
  public UserResponseDTO delUserLast() {
    if (userRepository.count() > 0) {
      User user = userRepository.findTopByOrderByIdDesc().get();
      System.out.println("Delete last record: " + user);
      UserResponseDTO userResponseDTO = userMapper.toDTO(user);
      userRepository.delete(user);
      return userResponseDTO;
    } else {
      throw new IdNotFoundException("Users list is empty");
    }
  }

  @CacheEvict(value = "users", key = "0")
  public List<UserResponseDTO> addTestUser() {
    System.out.println("Quantity of people: " + userRepository.count());
    User u = new User();
    u.setName("X CODE");
    u.setSurname("MANCODE");
    u.setBirth_date(LocalDateTime.now());
    u.setEmail((int) (Math.random() * 10000) + "@me.com");
    u.setActive(true);
    userRepository.save(u);
    List<User> users = userRepository.findAll();
    return userMapper.toDTOs(users);
  }

  public UserResponseDTO getRandomUser() {
    List<User> entityList = new ArrayList<>();
    userRepository.findAll().forEach(entityList::add);
    if (entityList.isEmpty()) {
      throw new IdNotFoundException("List of users is empty");
    }
    Collections.shuffle(entityList);
    User user = entityList.getFirst();
    System.out.println("From UserService - Random user: " + user.toString());
    return userMapper.toDTO(user);
  }

  public List<UserResponseDTO> getRangeIds(Integer n) {
    List<User> users = userRepository.findIdsNative(n);
    System.out.println("All users within range: " + users.toString());
    List<UserResponseDTO> userResponseDTOs = userMapper.toDTOs(users);
    return userResponseDTOs;
  }

  public List<UserResponseDTO> findByJPQL(String lastname) {
    List<User> users = userRepository.findByLastNameJPQL(lastname);
    if (users.isEmpty()) {
      throw new IdNotFoundException("Users not found with lastname: " + lastname);
    }
    System.out.println("All users with name <" + lastname + "> found: " + users.toString());
    List<UserResponseDTO> userResponseDTOs = userMapper.toDTOs(users);
    return userResponseDTOs;
  }

  public List<UserHistory> getUserLog() {
    List<UserHistory> log = userHistoryRepository.findAll();
    System.out.println("All users within range: " + log.toString());
    //    List<UserResponseDTO> userResponseDTOs = userMapper.toDTOs(users);
    return log;
  }
}
