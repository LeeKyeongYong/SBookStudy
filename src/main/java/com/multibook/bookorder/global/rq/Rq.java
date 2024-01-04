package com.multibook.bookorder.global.rq;

import com.multibook.bookorder.domain.global.exceptions.GlobalException;
import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.domain.member.member.service.MemberService;
import com.multibook.bookorder.global.rsData.RsData;
import com.multibook.bookorder.global.security.entity.SecurityUser;
import com.multibook.bookorder.util.UtZip;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Component
@RequestScope
@RequiredArgsConstructor
@Slf4j
public class Rq {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final EntityManager entityManager;
    private final MemberService memberService;
    private Member member;

    public String redirect(String url, String msg) {
        String[] urlBits = url.split("#", 2);
        url = urlBits[0];
        StringBuilder sb = new StringBuilder();

        sb.append("redirect:");
        sb.append(url);

        if (UtZip.str.hasLength(msg)) {
            msg = URLEncoder.encode(msg, StandardCharsets.UTF_8);

            if (url.contains("?")) {
                sb.append("&msg=");
            } else {
                sb.append("?msg=");
            }

            sb.append(msg);
        }

        if (urlBits.length == 2) {
            sb.append("#");
            sb.append(urlBits[1]);
        }

        return sb.toString();
    }

    public String historyBack(GlobalException ex) {
        String exStr = UtZip.exception.toString(ex);

        request.setAttribute("exStr", exStr);
        log.debug(exStr);

        return historyBack(ex.getRsData().getMsg());
    }

    public String historyBack(String msg) {
        request.setAttribute("failMsg", msg);

        return "global/js";
    }

    public String redirectOrBack(RsData<?> rs, String path) {
        if (rs.isFail()) return historyBack(rs.getMsg());

        return redirect(path, rs.getMsg());
    }

    public SecurityUser getUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(it -> it instanceof SecurityUser)
                .map(it -> (SecurityUser) it)
                .orElse(null);
    }

    public boolean isLogin() {
        return getUser() != null;
    }

    public boolean isLogout() {
        return !isLogin();
    }

    public boolean isAdmin() {
        if (isLogout()) return false;

        return getUser()
                .getAuthorities()
                .stream()
                .anyMatch(it -> it.getAuthority().equals("ROLE_ADMIN"));
    }

    public void attr(String key, Object value) {
        request.setAttribute(key, value);
    }

    public String getCurrentQueryStringWithoutParam(String paramName) {
        String queryString = request.getQueryString();

        if (queryString == null) {
            return "";
        }

        queryString = UtZip.url.deleteQueryParam(queryString, paramName);

        return queryString;
    }

    public Member getMember() {
        if (isLogout()) return null;

        if (member == null) {
            member = entityManager.getReference(Member.class, getUser().getId());
        }

        return member;
    }

    public String getProfileImgUrl() {
        return memberService.getProfileImgUrl(getMember());
    }

    public String getEncodedCurrentUrl() {
        return UtZip.url.encode(getCurrentUrl());
    }

    private String getCurrentUrl() {
        String url = request.getRequestURI();
        String queryString = request.getQueryString();

        if (queryString != null) {
            url += "?" + queryString;
        }

        return url;
    }
}