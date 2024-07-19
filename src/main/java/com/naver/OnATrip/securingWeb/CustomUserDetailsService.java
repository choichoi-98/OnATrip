//package com.naver.OnATrip.securingWeb;
//
//import com.naver.OnATrip.entity.Member;
//import com.naver.OnATrip.repository.MemberRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        logger.info("username 로그인 시 입력한 값 : " + username);
//        Member users = memberRepository.findByEmailPrincipal(username);
//
//        if(users == null) {
//            logger.info("username " + username + " not found");
//            throw new UsernameNotFoundException("username " + username + "not found");
//        }
//
//        return (UserDetails) users;
//    }
//
//}
