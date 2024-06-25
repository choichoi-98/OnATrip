package com.naver.OnATrip.securingWeb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        // 밑 주석은 작업 다 끝나면 다시 적용. 그 전까지는 올 퍼밋
//                        .requestMatchers("/", "/main", "/css/**", "/js/**", "/images/**","/join").permitAll()
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated() //// 나머지 요청은 인증 필요
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .failureUrl("/login")	        // 로그인 실패 후 이동 페이지
                        .usernameParameter("email") //사용자 이름 파라미터 이름 설정
                        .passwordParameter("password") // 비밀번호 파라미터 이름 설정
                        .defaultSuccessUrl("/main")  // 로그인 성공 후 이동 페이지
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    //Bean UserDetailsService은 단일 사용자로 메모리 내 사용자 저장소를 설정합니다.
    // 해당 사용자에게는 사용자 이름 user, 암호 password및 역할이 부여됩니다 USER.
    // 이거라는데  이해가 안되서 주석
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("password")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
}
