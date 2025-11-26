package com.study.userservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.userservice.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

  Optional<List<Card>> findByIdIn(Set<Long> ids);

  Optional<List<Card>> findByUserId(Long userId);

  Optional<Card> findTopByOrderByIdDesc();

  Optional<Card> getByNumber(Long number);
}
