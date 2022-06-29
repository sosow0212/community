package yoon.community.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import yoon.community.config.jwt.JwtAccessDeniedHandler;
import yoon.community.config.jwt.JwtAuthenticationEntryPoint;
import yoon.community.config.jwt.JwtSecurityConfig;
import yoon.community.config.jwt.TokenProvider;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.csrf().disable();

        http
                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)


                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .and()
                .authorizeRequests()

                .antMatchers("/swagger-ui/**", "/v3/**").permitAll() // swagger
                .antMatchers(HttpMethod.GET, "/image/**").permitAll()

                .antMatchers("/api/sign-up", "/api/sign-in", "/api/reissue").permitAll()

                .antMatchers(HttpMethod.GET, "/api/users").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, "/api/users/favorites").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, "/api/users/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT, "/api/users/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/users/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")

                .antMatchers(HttpMethod.POST, "/api/messages").authenticated()
                .antMatchers(HttpMethod.GET, "/api/messages/sender").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, "/api/messages/sender/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, "/api/messages/receiver").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, "/api/messages/receiver/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/messages/sender/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/messages/receiver/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")

                .antMatchers(HttpMethod.POST, "/api/boards").authenticated()
                .antMatchers(HttpMethod.GET, "/api/boards/").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, "/api/boards/best").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, "/api/boards/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/boards/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/boards/{id}/favorites").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT, "/api/boards/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/boards/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")

                .antMatchers(HttpMethod.GET, "/api/comments").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/comments").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/comments/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")

                .antMatchers(HttpMethod.POST, "/api/reports/users").authenticated()
                .antMatchers(HttpMethod.POST, "/api/reports/boards").authenticated()




                .anyRequest().hasAnyRole("ROLE_ADMIN")
//                .anyRequest().authenticated() // 나머지는 전부 인증 필요
//                .anyRequest().permitAll()   // 나머지는 모두 그냥 접근 가능

                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }
}