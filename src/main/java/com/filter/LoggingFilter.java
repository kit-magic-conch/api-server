package com.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
//@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@WebFilter
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CachedBodyHttpServletRequest requestToUse = new CachedBodyHttpServletRequest(request);
        StringBuilder sb = new StringBuilder();

        sb.append(requestToUse.getMethod())
                .append(' ')
                .append(requestToUse.getRequestURI());
        String queryString = requestToUse.getQueryString();
        if (queryString != null) {
            sb.append('?').append(queryString);
        }
        sb.append('\n');

        sb.append("client: ").append(requestToUse.getRemoteAddr()).append('\n');
        sb.append("accept: ").append(requestToUse.getHeader("Accept")).append('\n');

        String contentType = requestToUse.getContentType();
        if (contentType != null) {
            sb.append("Content-Type: ").append(contentType).append('\n');
        }

        HttpSession session = requestToUse.getSession(false);
        if (session != null) {
            sb.append("session: ").append(session.getId()).append('\n');
        }

        if (!contentType.startsWith("multipart")) {
            String payload = StreamUtils.copyToString(requestToUse.getInputStream(), StandardCharsets.UTF_8);
            if (StringUtils.hasLength(payload)) {
                sb.append("payload: ").append(payload);
            }
        }

        log.debug(sb.toString());

        filterChain.doFilter(requestToUse, response);
    }
}