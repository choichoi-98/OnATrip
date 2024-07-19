package com.naver.OnATrip.securingWeb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        // 밑 주석은 작업 다 끝나면 다시 적용. 그 전까지는 올 퍼밋
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers( "/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN") // ADM…
                        .anyRequest().authenticated() //// 나머지 요청은 인증 필요
//                        "/member/**", "/item/**", "/images/**","/main","/location/**","/join/**","/login/**","/findPassword/**",
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .failureUrl("/login")            // 로그인 실패 후 이동 페이지
                        .usernameParameter("email") //사용자 이름 파라미터 이름 설정
                        .passwordParameter("password") // 비밀번호 파라미터 이름 설정
                        .defaultSuccessUrl("/main")  // 로그인 성공 후 이동 페이지
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/main")
                        .invalidateHttpSession(true)
                )
                .exceptionHandling((exceptions) -> exceptions
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/payment/**","/admin/**","/member/**","/member/join")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }


}
