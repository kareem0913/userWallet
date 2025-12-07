package org.demo.userwallet.filter;

import org.demo.userwallet.model.JwtToken;
import org.demo.userwallet.repository.JwtTokenRepository;
import org.demo.userwallet.util.JwUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebFilter("/*")
public class JwtAuthFilter implements Filter {
    private JwtTokenRepository jwtTokenRepository;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.jwtTokenRepository = new JwtTokenRepository();
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ") || !JwUtil.validateToken(token.substring(7))) {
            String baseUrl = request.getRequestURL().toString();
            if(baseUrl.contains("/login") || baseUrl.contains("/register")) {
                filterChain.doFilter(request, response);
                return;
            }

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        Optional<JwtToken> jwtToken = jwtTokenRepository.findTokenByToken(token.substring(7));
        if(!jwtToken.isPresent()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
