package com.concrete.solution.java.test.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.concrete.solution.java.test.exception.ErrorMessage;
import com.concrete.solution.java.test.model.User;
import com.concrete.solution.java.test.repository.UserRepository;
import com.concrete.solution.java.test.service.JwtTokenService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());


    private UserRepository userRepository;

    private JwtTokenService jwtTokenService;

    private String TOKEN_HEADER = "Authorization";



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        jwtTokenService = webApplicationContext.getBean(JwtTokenService.class);
        userRepository = webApplicationContext.getBean(UserRepository.class);


        Integer error = 0;



        String authToken = request.getHeader(this.TOKEN_HEADER);
        Integer id = jwtTokenService.getIdFromToken(authToken);


        if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("checking identification for ID:  " + id);

            User user = userRepository.findOne(id.longValue());

            try {
                if (jwtTokenService.validateToken(authToken, user)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            user.getPassword()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.info("authenticated user " + user.getEmail());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                error++;
            }
        } else {
            logger.info("Token not valid");
            error++;
        }


        if (error > 0) {
            logger.error("User not authorized");

            ErrorMessage em = new ErrorMessage("Não autorizado");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(em);
            return;
        }

        chain.doFilter(request, response);
    }
}
