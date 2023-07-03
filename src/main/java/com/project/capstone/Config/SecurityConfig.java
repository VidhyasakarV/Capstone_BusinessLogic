package com.project.capstone.Config;

import com.project.capstone.Utils.JwtToken;
import com.project.capstone.Utils.JwtFilterRequest;
import com.project.capstone.Utils.UserServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserServiceUtils userServiceUtils;
    @Autowired
    CustomOAuth2UserService oAuth2UserService;
    @Autowired
    JwtToken jwtToken;
    @Autowired
    JwtFilterRequest jwtFilterRequest;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceUtils).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/restapi/**","/user/**","/admin/**","/feed/**","/oauth2/**","/login/**","/Oauth/**")
                .permitAll()
                .antMatchers("/v3/api-docs","/v2/api-docs","/swagger-resources/**","/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .oauth2Login()
                .loginPage("/restapi/verify")
                .userInfoEndpoint()
                .userService(oAuth2UserService).and()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {
                        CustomOauth2User oauthUser = (CustomOauth2User) authentication.getPrincipal();
                        userServiceUtils.processOAuthPostLogin(oauthUser.getEmail(), authentication.getName(),"GOOGLE");
                        final UserDetails userDetails = userServiceUtils.loadUserByUsername(oauthUser.getEmail());
                        final String jwt = jwtToken.generateToken(userDetails);
                        response.sendRedirect("/Oauth/oauthsucess/"+jwt);
                    }
                })
                .and()
                .logout().logoutSuccessUrl("/").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/Oauth/oauthUnsuces")
        ;
        http.addFilterBefore(jwtFilterRequest, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
