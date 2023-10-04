package com.example.todospringapp.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /**
     * SecurityConfig에서의 설정에 따라 특정 url은 로그인 확인을 한다.
     * `Authentication` 자체는 인증된 정보이기에 `SecurityContextHolder`가 가지고 있는 값을 통해
     * 인증이 되었는지 아닌지 확인할 수 있다. [ Authentication.isAuthenticated(); ]
     *
     * Holder는 쓰레드가 달라지면 인증 정보를 알 수 없는 상황에  런타임 중 static 등으로 context를 쓰레드에 연결시켜준다.*/
    final private TokenProvider tokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            // request에서 토큰 가져오기
            String token = parseBearerToken(request);
            log.info("Filter is running...");

            // 토큰 검사하기. JWT이므로 인가 서버에 요청하지 않고도 검증 가능
            if(token != null && !token.equalsIgnoreCase("null")){
                // userId가져오기 위조된 경우 예외처리된다.
                String userId = tokenProvider.validateAndGetUserId(token);
                log.info("Authenticated user ID : " + userId);
                // 인증 완료 : SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.

                /**
                 * 여기서 저장한 userId는 컨트롤러에 @AuthenticationPrincipal로 꺼내 쓸 수 있다.(이름 상관없이 형태만 맞추면 받아지는 듯..?)
                 * */
                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.NO_AUTHORITIES);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception ex){
            log.error("Could not set user authentication in security context",ex);
        }
        filterChain.doFilter(request,response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
