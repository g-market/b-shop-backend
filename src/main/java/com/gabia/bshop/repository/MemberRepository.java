package com.gabia.bshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.bshop.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByHiworksId(String hiworksId);
}
