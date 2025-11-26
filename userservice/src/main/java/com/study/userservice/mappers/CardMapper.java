package com.study.userservice.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.study.userservice.dto.CardRequestDTO;
import com.study.userservice.dto.CardResponseDTO;
import com.study.userservice.entity.Card;

@Component
public class CardMapper {

  public CardResponseDTO toDTO(Card card) {
    if (card == null) {
      return null;
    }
    return new CardResponseDTO(
        card.getId(),
        card.getUser_id(),
        card.getNumber(),
        card.getHolder(),
        card.getExpiration_date(),
        card.isActive());
  }

  public List<CardResponseDTO> toDTOs(List<Card> cards) {
    return cards.stream().map(this::toDTO).collect(Collectors.toList());
  }

  public Card toEntity(CardRequestDTO cardRequestDTO) {
    if (cardRequestDTO == null) {
      return null;
    }
    Card card = new Card();
    card.setId(cardRequestDTO.getId());
    card.setUser_id(cardRequestDTO.getUser_id());
    card.setNumber(cardRequestDTO.getNumber());
    card.setHolder(cardRequestDTO.getHolder());
    card.setExpiration_date(cardRequestDTO.getExpiration_date());
    card.setActive(cardRequestDTO.isActive());
    return card;
  }

  public List<Card> manyToEntity(List<CardRequestDTO> cardDTOs) {
    return cardDTOs.stream().map(this::toEntity).collect(Collectors.toList());
  }
}
