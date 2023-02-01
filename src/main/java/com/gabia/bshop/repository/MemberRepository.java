package com.gabia.bshop.repository;

import com.gabia.bshop.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

//    Optional<Member> findByEmail(String email);

    Optional<Member> findByHiworksId(String hiworksId);
}
