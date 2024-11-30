package wkz.org.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wkz.org.backend.interceptor.ForbidInterceptor;
import wkz.org.backend.interceptor.RoleInterceptor;
import wkz.org.backend.interceptor.SessionInterceptor;

@Configuration
public class SessionConfig implements WebMvcConfigurer {
    @Autowired
    private SessionInterceptor sessionInterceptor;
    @Autowired
    private RoleInterceptor roleInterceptor;
    @Autowired
    private ForbidInterceptor forbidInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/register");

        registry.addInterceptor(roleInterceptor)
                .addPathPatterns("/admin/**");

        registry.addInterceptor(forbidInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/register");
    }
}

