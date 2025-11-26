package com.study.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.userservice.entity.UserHistory;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Integer> {}
