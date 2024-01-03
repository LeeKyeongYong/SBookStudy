package com.multibook.bookorder.global.security.service;

import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.domain.member.member.repository.MemberRepository;
import com.multibook.bookorder.global.security.entity.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> opMember = memberRepository.findByUsername(username);

        if (opMember.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }

        Member member = opMember.get();

        // 인수 유형 및 순서를 수정
        return new SecurityUser(
                member.getUsername(),   // username을 첫 번째 인수로
                member.getPassword(),   // password를 세 번째 인수로
                member.getAuthorities(), // authorities를 네 번째 인수로
                member.getId()          // id를 마지막 인수로
        );
    }
}