package com.sparta.springcore.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FilterSkipMatcher implements RequestMatcher {

    private final OrRequestMatcher orRequestMatcher;
    private final RequestMatcher   processingMatcher;

    public FilterSkipMatcher(
            List<String> pathToSkip,
            String processingPath
    ) {
        this.orRequestMatcher = new OrRequestMatcher(pathToSkip
                .stream()
                .map(this :: httpPath)
                .collect(Collectors.toList()));
        this.processingMatcher = new AntPathRequestMatcher(processingPath);
    }

    private AntPathRequestMatcher httpPath(String skipPath) {
        log.info("httpPath");
        String[] splitStr = skipPath.split(",");

        /*
         * 배열 [1] httpMathod 방식 post get 인지 구분
         * 배열 [0] 제외하는 url
         * */
        return new AntPathRequestMatcher(
                splitStr[1],
                splitStr[0]
        );
    }

    @Override
    public boolean matches(HttpServletRequest req) {
        log.info("matches url: {}",req.getRequestURL());
        return !orRequestMatcher.matches(req) && processingMatcher.matches(req);
    }
}
