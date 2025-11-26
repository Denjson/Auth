package com.study.userservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.study.userservice.dto.CardRequestDTO;
import com.study.userservice.dto.CardResponseDTO;
import com.study.userservice.dto.UserResponseDTO;
import com.study.userservice.entity.Card;
import com.study.userservice.entity.User;
import com.study.userservice.exceptions.DuplicatedValueException;
import com.study.userservice.exceptions.IdNotFoundException;
import com.study.userservice.mappers.CardMapper;
import com.study.userservice.repository.CardRepository;
import com.study.userservice.repository.UserRepository;
import com.study.userservice.service.interfaces.CardService;

@Service
public class CardServiceImpl implements CardService {

  private final CardRepository cardRepository;
  private final UserRepository userRepository;
  private final CardMapper cardMapper;

  public CardServiceImpl(
      CardRepository cardRepository, UserRepository userRepository, CardMapper cardMapper) {
    this.cardRepository = cardRepository;
    this.userRepository = userRepository;
    this.cardMapper = cardMapper;
  }

  @CacheEvict(value = "cards", key = "0")
  public CardResponseDTO saveOne(CardRequestDTO cardRequestDTO) {
    userRepository
        .findById(cardRequestDTO.getUser_id())
        .orElseThrow(
            () ->
                new IdNotFoundException("User not found with id: " + cardRequestDTO.getUser_id()));
    if (cardRepository.getByNumber(cardRequestDTO.getNumber()).isPresent()) {
      throw new DuplicatedValueException(
          "Card number is duplicated: " + cardRequestDTO.getNumber());
    }
    Card card = cardRepository.save(cardMapper.toEntity(cardRequestDTO));
    System.out.println("User found. Card added: " + cardRequestDTO.toString());
    return cardMapper.toDTO(card);
  }

  @CacheEvict(value = "cards", key = "0")
  public List<CardResponseDTO> saveMany(List<CardRequestDTO> cardRequestDTOs) {
    List<Card> cards = cardMapper.manyToEntity(cardRequestDTOs);
    Set<Long> usersFromCards = cards.stream().map(Card::getUser_id).collect(Collectors.toSet());
    Optional<List<User>> usersFound = userRepository.findByIdIn(usersFromCards);
    if (usersFound.get().isEmpty()) {
      throw new IdNotFoundException("Users not found with ids: " + usersFromCards);
    }
    if (usersFound.get().size() == usersFromCards.size()) {
      cardRepository.saveAll(cards);
      System.out.println("All users found. Saved Cards: " + cards);
      return cardMapper.toDTOs(cards);
    } else {
      throw new IdNotFoundException("Users not found with ids: " + usersFromCards);
    }
  }

  @Cacheable(value = "cards", key = "#id")
  public CardResponseDTO getById(Long id) {
    Card card =
        cardRepository
            .findById(id)
            .orElseThrow(() -> new IdNotFoundException("Card not found with id: " + id));
    return cardMapper.toDTO(card);
  }

  public List<CardResponseDTO> getByIds(Set<Long> ids) {
    List<Card> cards = cardRepository.findByIdIn(ids).get();
    if (cards.isEmpty()) {
      throw new IdNotFoundException("Cards not found with ids: " + ids);
    }
    System.out.println("Cards found: " + cards.toString());
    return cardMapper.toDTOs(cards);
  }

  @Cacheable(value = "cards", key = "0")
  public List<CardResponseDTO> getAllCards() {
    List<Card> cards = IterableUtils.toList(cardRepository.findAll());
    if (cards.isEmpty()) {
      throw new IdNotFoundException("List of cards is empty");
    }
    List<CardResponseDTO> cardResponseDTOs = cardMapper.toDTOs(cards);
    return cardResponseDTOs;
  }

  @Caching(
      evict = {@CacheEvict(value = "cards", key = "#id"), @CacheEvict(value = "cards", key = "0")})
  public CardResponseDTO deleteById(Long id) {
    Card card =
        cardRepository
            .findById(id)
            .orElseThrow(() -> new IdNotFoundException("Card not found with id: " + id));
    cardRepository.deleteById(id);
    return cardMapper.toDTO(card);
  }

  @CacheEvict(value = "cards", key = "0")
  public CardResponseDTO delCardLast() {
    Card card =
        cardRepository
            .findTopByOrderByIdDesc()
            .orElseThrow(() -> new IdNotFoundException("Card not found"));
    System.out.println("Delete last card: " + card);
    CardResponseDTO сardResponseDTO = cardMapper.toDTO(card);
    cardRepository.delete(card);

    return сardResponseDTO;
  }

  @CachePut(value = "cards", key = "#id")
  @CacheEvict(value = "cards", key = "0")
  public CardResponseDTO updateCard(Long id, CardRequestDTO cardDetails) {
    Optional<User> userOptional = userRepository.findById(cardDetails.getUser_id());
    Card card =
        cardRepository
            .findById(id)
            .orElseThrow(() -> new IdNotFoundException("Card not found with id: " + id));
    if (userOptional.isPresent()) {
      card.setUser_id(cardDetails.getUser_id());
      card.setHolder(userOptional.get().getName());
      card.setNumber(cardDetails.getNumber());
      card.setExpiration_date(cardDetails.getExpiration_date());
      card.setActive(cardDetails.isActive());
      cardRepository.save(card);
      System.out.println("Card updated: " + card.toString());
    } else {
      System.out.println("User with ID " + cardDetails.getUser_id() + " not found.");
      throw new IdNotFoundException("User not found with id: " + cardDetails.getUser_id());
    }
    return cardMapper.toDTO(card);
  }

  @CacheEvict(value = "cards", key = "0")
  public CardResponseDTO addRandomCard(UserResponseDTO userResponseDto) {
    Card card = new Card();
    card.setUser_id(userResponseDto.getId());
    card.setNumber((int) (Math.random() * 10000000) + 1000000);
    card.setHolder(userResponseDto.getName());
    card.setExpiration_date(LocalDateTime.now());
    card.setActive(userResponseDto.isActive());
    System.out.println("From UserService - Card added to Random user: " + card.toString());
    cardRepository.save(card);
    return cardMapper.toDTO(card);
  }
}
