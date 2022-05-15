package ru.kinoday.front.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.kinoday.front.common.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class SessionFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    Set<String> excludeUrlPatterns = new HashSet<>();

    {
        excludeUrlPatterns.add("/css/*");
        excludeUrlPatterns.add("/images/*");
    }
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return excludeUrlPatterns.stream()
                .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getSession().getAttribute("SPRING_SECURITY_CONTEXT") != null) {
            SecurityContext securityContext = (SecurityContext) req.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
            User principal = (User) securityContext.getAuthentication().getPrincipal();
            String userCredentialMail = principal.getUsername();
            ru.kinoday.front.common.model.User userData = userService.findUserByEmail(userCredentialMail);
            if (userData != null) {
                String ip = userData.getIp();
                if (ip == null || ip == "" || ip != req.getRemoteAddr()){
                    userData.setIp(req.getRemoteAddr());
                    userService.update(userData);
                }
                req.getSession().setAttribute("user", userData);
            }
        } else {

        }
        filterChain.doFilter(req, res);
    }

    @Bean
    public FilterRegistrationBean<SessionFilter> sessionFilterReg(@Autowired SessionFilter filter) {
        FilterRegistrationBean<SessionFilter> regBean = new FilterRegistrationBean<>();
        regBean.setFilter(filter);
        regBean.addUrlPatterns("/*");
        return regBean;
    }

}