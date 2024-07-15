package com.study.event.api.event.repository;

import com.study.event.api.event.entity.EventUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventUserRepository extends JpaRepository<EventUser, String> {

    // query method로 Jpql 생성
    boolean existsByEmail(String email);

    // 조회가 안됐을 때 nullpointExeption을 피하기 위해 optional 사용
    Optional<EventUser> findByEmail(String email);


}