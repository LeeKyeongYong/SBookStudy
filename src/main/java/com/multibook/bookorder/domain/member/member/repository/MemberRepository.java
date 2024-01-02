package com.multibook.bookorder.domain.member.member.repository;

import com.multibook.bookorder.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
