package com.naver.OnATrip.securingWeb;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//웹 애플리케이션은 Spring MVC를 기반으로 합니다.
//결과적으로 Spring MVC를 구성하고 이러한 템플릿을 노출하도록 뷰 컨트롤러를 설정해야 합니다.
//다음 목록은 애플리케이션에서 Spring MVC를 구성하는 클래스를 보여줍니다.
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/main").setViewName("main");
//        registry.addViewController("/").setViewName("main");
        registry.addRedirectViewController("/", "/main");
        registry.addViewController("/join").setViewName("join");
        registry.addViewController("/login").setViewName("login");
    }

}

