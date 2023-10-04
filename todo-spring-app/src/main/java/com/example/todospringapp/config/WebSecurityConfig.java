package com.example.todospringapp.config;

import com.example.todospringapp.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    final private JwtAuthenticationFilter jwtAuthenticationFilter;
    final private ObjectMapper objectMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                // csrf 공격 방지 기능 끄기
                .csrf().disable()
                .httpBasic().disable()
                // 세션 관리 끄기
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/","/auth/**","/h2-console/**","/mail/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .headers()
                .addHeaderWriter(
                        new XFrameOptionsHeaderWriter(
                                new WhiteListedAllowFromStrategy(Arrays.asList("localhost"))
                        )
                )
                .frameOptions().sameOrigin();

        http.exceptionHandling()
                .authenticationEntryPoint((request, response, e) -> {
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("status", HttpServletResponse.SC_FORBIDDEN);
                    data.put("message", e.getMessage());

                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                    objectMapper.writeValue(response.getOutputStream(), data);
                });

        http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
        /** CorsFilter가 어디?
         * Security filter chain: [
         *   WebAsyncManagerIntegrationFilter
         *   SecurityContextPersistenceFilter
         *   HeaderWriterFilter
         *   CorsFilter
         *   LogoutFilter
         *   JwtAuthenticationFilter
         *   RequestCacheAwareFilter
         *   SecurityContextHolderAwareRequestFilter
         *   AnonymousAuthenticationFilter
         *   SessionManagementFilter
         *   ExceptionTranslationFilter
         *   FilterSecurityInterceptor
         * ]
         * */
    }
}